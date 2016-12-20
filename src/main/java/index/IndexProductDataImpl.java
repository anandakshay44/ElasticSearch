package index;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;

import common.Constant;
import data.Bug;

public class IndexProductDataImpl implements IndexProductData {

   public void createIndex(Bug bug, Client client) {
      try {
         IndexResponse response = client
               .prepareIndex(Constant.INDEX_NAME, Constant.TYPE_NAME,
                     bug.getBug_id())
               .setSource(convertContribution(bug)).execute().actionGet();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }


   private String convertContribution(Bug bug) {
      String json = "";
      ObjectMapper mapper = new ObjectMapper();
      try {
         json = mapper.writeValueAsString(bug);
      } catch (JsonGenerationException e) {
         System.out.println("ERROR: " + e.getMessage());
      } catch (JsonMappingException e) {
         System.out.println("ERROR: " + e.getMessage());
      } catch (IOException e) {
         System.out.println("ERROR: " + e.getMessage());
      }
      return json;
   }

   public void deleteIndex(Bug bug, Client client) {
      @SuppressWarnings("unused")
      DeleteResponse response = client.prepareDelete(Constant.INDEX_NAME,
            Constant.TYPE_NAME, bug.getBug_id()).execute().actionGet();
      refreshIndex(client);

   }

   public void refreshIndex(Client client) {
      System.out.println("just refreshing the index and moving out");
      client.admin().indices().prepareRefresh().execute().actionGet();
   }


   public void updateIndex(Bug contrib, Client client) {
      UpdateRequest updateRequest = new UpdateRequest();
      updateRequest.index(Constant.INDEX_NAME);
      updateRequest.type(Constant.TYPE_NAME);
      updateRequest.id(contrib.getBug_id());
      updateRequest.doc(convertContribution(contrib));
      try {
         client.update(updateRequest).get();
      } catch (InterruptedException e) {
         System.out.println("ERROR: " + e.getMessage());
      } catch (ExecutionException e) {
         System.out.println("ERROR: " + e.getMessage());
      }
      refreshIndex(client);
   }
}
