package search;

import java.util.List;
import org.elasticsearch.client.Client;
import data.Bug;

public interface SearchQuery {
   public String fieldQuery(String field, String queryParam,Client client);
   public  String rangeQuery(String field, int from, int to,Client client);
   public  List<Bug> getSearchResults(String Response,Client client);
}
