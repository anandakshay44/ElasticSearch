package test;

import java.util.Map;

import org.elasticsearch.client.Client;

import au.com.bytecode.opencsv.CSVReader;
import client.SearchClientServiceImpl;
import data.Contribution;
import data.DataParsing;
import index.IndexProductDataImpl;
import search.SearchQuery;
import search.SearchQueryImpl;

public class Driver {
   private static Client client;
   private static SearchQueryImpl searchQuery;
   private static CSVReader statesCSV;
   private static CSVReader contribsCSV;
   private static IndexProductDataImpl indexOpr;
   private static Map<String, String> mappedStates = null;
   private static String statesCSVFilename =
         "C:/Users/ananda/Downloads/elasticsearch_presidential_contrib-master/Data/states.csv";
/*   private static String contribsCSVFilename =
        "C:/Users/ananda/Downloads/elasticsearch_presidential_contrib-master/Data/2008_Pres_Contrib_subset.csv";*/
   private static String contribsCSVFilename =
         "C:/Users/ananda/Downloads/2008_Pres_Contrib.csv";


   public static void main(String[] args) {

      client = new SearchClientServiceImpl().getClient();
      searchQuery = new SearchQueryImpl();
      indexOpr = new IndexProductDataImpl();
      if (client != null) {
         System.out.println(client);
      }
      try {
         // Read States CSV and store in KeyValue pair
         mappedStates = DataParsing.readStates(statesCSVFilename);
      } catch (Exception e) {
         e.printStackTrace();
      }
      try {
         // Process contributions and create index for each record in ES
         DataParsing.processContributions(contribsCSVFilename, mappedStates,
               client);
      } catch (Exception e) {
         e.printStackTrace();
      }
      //starting  with a sample search
      String searchIdResponse = searchQuery.fieldQuery("_id", "SA17.190072",client);
      System.out.println("\nSearching for _id SA17A.5211");
      System.out.println("=============================");
      Contribution contrib_SA17A5211 = searchQuery.getSearchResults(searchIdResponse,client).get(0);
      System.out.println("Document returned: " + contrib_SA17A5211.toString());
      System.out.println("=============================");
      
   // Perform range query
      String searchRangeResponse = searchQuery.rangeQuery("amount", 100, 200,client);
      System.out.println("\nSearching for Amount between 100 and 200");
      System.out.println("========================================");
      for(Contribution c : searchQuery.getSearchResults(searchRangeResponse,client)) {
              System.out.println("Document returned: " + c.toString());
      }
      System.out.println("========================================");
      
   // Update contrib_SA17A5211 value
      Double orig = contrib_SA17A5211.getAmount();
      Double val = contrib_SA17A5211.getAmount()+1;
      contrib_SA17A5211.setAmount(val);
      
      indexOpr.updateIndex(contrib_SA17A5211,client);
      System.out.println("\n=============================");
      System.out.println("Updating Value of SA17A.5211 from $" +orig +"to "+ val);
      System.out.println("=============================");
      
   // Perform simple query again on contribution id SA17A5211
      String searchIdResponseAgain = searchQuery.fieldQuery("_id", "SA17A.5211",client);
      System.out.println("\nSearching for _id SA17A.5211 AGAIN");
      System.out.println("=============================");
      Contribution contrib_SA17A5211_updated = searchQuery.getSearchResults(searchIdResponseAgain,client).get(0);
      System.out.println("Document returned: " + contrib_SA17A5211_updated.toString());
      System.out.println("=============================");
      
      System.out.println("\n=============================");
      System.out.println("Deleting document SA17A.5211");
      System.out.println("=============================");
      indexOpr.deleteIndex(contrib_SA17A5211,client);
      // Perform simple query again on contribution id SA17A5211
      String searchIdResponseDeleted = searchQuery.fieldQuery("_id", "SA17A.5211",client);
      System.out.println("\nSearching for _id SA17A.5211 AGAIN");
      System.out.println("=============================");
      int returnedDocs = searchQuery.getSearchResults(searchIdResponseDeleted,client).size();
      System.out.println("Document count matching search for Id SA17A.5211: " + returnedDocs);
      System.out.println("============================="); 

   }

}
