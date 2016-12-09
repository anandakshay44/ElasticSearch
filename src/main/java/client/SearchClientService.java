package client;

import org.elasticsearch.client.Client;

public interface SearchClientService {
	Client getClient();

    void addNewNode(String name);

    void removeNode(String nodeName);

}
