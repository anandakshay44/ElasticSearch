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
    * This method is used to process each of the contributions and creating
    * index in ES
    * 
    * @param csvFilename
    * @param mappedStates
    */
   public static void processBugs(String csvFilename, Client client) {
      contribsCSV = readCSV(csvFilename);
      String[] nextLine;
      try {
         int count = 0;
         while ((nextLine = contribsCSV.readNext()) != null) {
            if (count > 0) {
               // Read each line of data csv and store in object
               Bug bug = bugFactory(nextLine);
               try {
                  indexOpr = new IndexProductDataImpl();
                  indexOpr.createIndex(bug, client);
                  System.out.println(
                        "(" + count + ") Document created: " + bug.getBug_id());
               } catch (Exception e) {
                  System.out.println("(" + count + ") Document NOT created: "
                        + bug.getBug_id());
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
    * This method is used to build a new Bug object and map the values
    * of the CSV onto this object
    * 
    * @param row
    * @param states
    * @return
    */
   private static Bug bugFactory(String[] row) {
      Bug bug = new Bug();
      bug.setBug_id(row[0]);
      bug.setSeverity(row[1]);
      bug.setPriority(row[2]);
      bug.setStatus(row[3]);
      bug.setAssignee(row[4]);
      bug.setReporter(row[5]);
      bug.setCategory(row[6]);
      bug.setComponent(row[7]);
      bug.setSummary(row[8]);
      return bug;
   }

   /**
    * This method is used to convert a bugs object into a JSON string
    * 
    * @param contrib
    * @return
    */
   private static String convertContribution(Bug contrib) {
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
