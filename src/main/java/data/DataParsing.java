package data;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.client.Client;

import au.com.bytecode.opencsv.CSVReader;
import index.IndexProductDataImpl;

public class DataParsing {
   private static CSVReader statesCSV;
   private static CSVReader contribsCSV;
   private static IndexProductDataImpl indexOpr;

   /**
    * This method is used to read the states CSV file
    * 
    * @return
    */
   public static Map<String, String> readStates(String csvFilename) {
      Map<String, String> stat = new HashMap<String, String>();
      statesCSV = readCSV(csvFilename);
      String[] nextLine;
      try {
         int count = 0;
         while ((nextLine = statesCSV.readNext()) != null) {
            if (count > 0) {
               stat.put(nextLine[1], nextLine[0]);
            }
            count++;
         }
      } catch (Exception e) {
         System.out.println("ERROR: " + e.getMessage());
      }
      return stat;
   }

   /**
    * This method is used to setup the CSVReader and obtain the file data
    * 
    * @param filename
    * @return
    */
   private static CSVReader readCSV(String filename) {
      CSVReader reader = null;
      try {
         reader = new CSVReader(new FileReader(filename));
      } catch (Exception e) {
         System.out.println("ERROR: " + e.getMessage());
      }
      return reader;
   }

   /**
    * This method is used to process each of the contributions and creating index
    * in ES
    * 
    * @param csvFilename
    * @param mappedStates
    */
   public static void processContributions(String csvFilename,
         Map<String, String> mappedStates, Client client) {
      contribsCSV = readCSV(csvFilename);
      String[] nextLine;
      try {
         int count = 0;
         while ((nextLine = contribsCSV.readNext()) != null) {
            if (count > 0) {
               // Read each line of data csv and store in object
               Contribution contrib =
                     contributionFactory(nextLine, mappedStates);
               try {
                  indexOpr = new IndexProductDataImpl();
                  indexOpr.createIndex(contrib, client);
                  System.out.println(
                        "(" + count + ") Document created: " + contrib.getId());
               } catch (Exception e) {
                  System.out.println("(" + count + ") Document NOT created: "
                        + contrib.getId());
                  e.printStackTrace();
               }
            }
            count++;
         }
      } catch (Exception e) {
         System.out.println("ERROR: " + e.getMessage());
      }
      indexOpr.refreshIndex(client);
   }

   /**
    * This method is used to build a new Contribution object and map the values
    * of the CSV onto this object
    * 
    * @param row
    * @param states
    * @return
    */
   private static Contribution contributionFactory(String[] row,
         Map<String, String> states) {
      Contribution contrib = new Contribution();
      contrib.setId(row[16]); //tran_id
      contrib.setCandidateName(row[2]); // cand_nm
      contrib.setContributorName(row[3]); // contbr_nm
      contrib.setContributorCity(row[4]); // contbr_city
      contrib.setContributorStateCode(row[5]); // contbr_st

      contrib.setContributorState(states.get(contrib.getContributorStateCode()) // mapped state
      );

      contrib.setContributorZip(row[6]); // contbr_zip
      contrib.setContributorEmployer(row[7]); // contbr_employer
      contrib.setContributorOccupation(row[8]); // contbr_occupation
      contrib.setAmount(Double.parseDouble(row[9])); // contbr_receipt_amt
      contrib.setDate(row[10]); // contbr_receipt_dt

      return contrib;
   }

   /**
    * This method is used to convert a Contribution object into a JSON string
    * 
    * @param contrib
    * @return
    */
   private static String convertContribution(Contribution contrib) {
      String json = "";
      ObjectMapper mapper = new ObjectMapper();
      try {
         json = mapper.writeValueAsString(contrib);
      } catch (JsonGenerationException e) {
         System.out.println("ERROR: " + e.getMessage());
      } catch (JsonMappingException e) {
         System.out.println("ERROR: " + e.getMessage());
      } catch (IOException e) {
         System.out.println("ERROR: " + e.getMessage());
      }
      return json;
   }
}
