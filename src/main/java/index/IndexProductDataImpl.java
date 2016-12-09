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
import data.Contribution;

public class IndexProductDataImpl implements IndexProductData {

   public void createIndex(Contribution contrib, Client client) {
      try {
         IndexResponse response = client
               .prepareIndex(Constant.INDEX_NAME, Constant.TYPE_NAME, contrib.getId())
               .setSource(convertContribution(contrib)).execute().actionGet();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }


   private String convertContribution(Contribution contrib) {
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

   public void deleteIndex(Contribution contrib, Client client) {
      @SuppressWarnings("unused")
      DeleteResponse response =
            client.prepareDelete(Constant.INDEX_NAME, Constant.TYPE_NAME, contrib.getId()).execute()
                  .actionGet();
      refreshIndex(client);

   }


   public void refreshIndex(Client client) {
      System.out.println("just refreshing the index and moving out");
      client.admin().indices().prepareRefresh().execute().actionGet();
   }


   public void updateIndex(Contribution contrib, Client client) {
      UpdateRequest updateRequest = new UpdateRequest();
      updateRequest.index(Constant.INDEX_NAME);
      updateRequest.type(Constant.TYPE_NAME);
      updateRequest.id(contrib.getId());
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
