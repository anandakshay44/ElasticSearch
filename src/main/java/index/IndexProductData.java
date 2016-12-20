package index;

import org.elasticsearch.client.Client;

import data.Bug;

public interface IndexProductData {
   void createIndex(Bug bug,Client client);
   void deleteIndex(Bug bug,Client client);
   void refreshIndex(Client client);
   void updateIndex(Bug bug,Client client);
}
