package client;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class SearchClientServiceImpl implements SearchClientService {

   private Client client;

   public Client getClient() {
      if (client == null) {
         client = createClient();
      }

      return client;
   }

   //@PostConstruct
   protected Client createClient() {
      if (client == null) {
         //Try starting search client at context loading
         try {
            Settings settings = ImmutableSettings.settingsBuilder()
                  .put("cluster.name", "elasticsearch").build();

            TransportClient transportClient = new TransportClient(settings);

            transportClient = transportClient.addTransportAddress(
                  new InetSocketTransportAddress("127.0.0.1", 9300));

            if (transportClient.connectedNodes().size() == 0) {
               System.out.println("sizeeeeee");
            }
            client = transportClient;
         } catch (Exception ex) {
            ex.printStackTrace();
         }
      }
      return client;
   }

   public void addNewNode(String name) {
      TransportClient transportClient = (TransportClient) client;
      transportClient
            .addTransportAddress(new InetSocketTransportAddress(name, 9300));
   }

   public void removeNode(String name) {
      TransportClient transportClient = (TransportClient) client;
      transportClient
            .removeTransportAddress(new InetSocketTransportAddress(name, 9300));
   }
}
