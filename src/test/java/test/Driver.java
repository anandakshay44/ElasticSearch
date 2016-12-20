package test;

import org.elasticsearch.client.Client;

import au.com.bytecode.opencsv.CSVReader;
import client.SearchClientServiceImpl;
import data.Bug;
import data.DataParsing;
import index.IndexProductDataImpl;
import search.SearchQuery;
import search.SearchQueryImpl;

public class Driver {
   private static Client client;
   private static SearchQueryImpl searchQuery;
   private static IndexProductDataImpl indexOpr;
   private static String bugsCSVFilename = "C:/logs_collect/bug_details.csv";

   public static void main(String[] args) {
      client = new SearchClientServiceImpl().getClient();
      searchQuery = new SearchQueryImpl();
      indexOpr = new IndexProductDataImpl();
      if (client != null) {
         System.out.println(client);
      }
      try {
         // Process Bugs and create index for each record in Elastic Search
         DataParsing.processBugs(bugsCSVFilename, client);
      } catch (Exception e) {
         e.printStackTrace();
      }
      //starting  with a sample search for bug id 1525121
      String searchIdResponse =
            searchQuery.fieldQuery("_id", "1525121", client);
      System.out.println("\nSearching for _id 1525121");
      System.out.println("=============================");
      Bug bug_1525121 =
            searchQuery.getSearchResults(searchIdResponse, client).get(0);
      System.out.println("Document returned: " + bug_1525121.toString());
      System.out.println("=============================");

      //starting with a sample search of keyword NULLPointerException
      searchIdResponse =
            searchQuery.fieldQuery("_summary", "NULLPointerException", client);

   }

}
