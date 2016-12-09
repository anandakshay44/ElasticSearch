package index;

import org.elasticsearch.client.Client;

import data.Contribution;

public interface IndexProductData {
   void createIndex(Contribution config,Client client);
   void deleteIndex(Contribution config,Client client);
   void refreshIndex(Client client);
   void updateIndex(Contribution config,Client client);

}
