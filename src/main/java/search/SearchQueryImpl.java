package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONArray;
import org.json.JSONObject;
import common.Constant;
import data.Bug;

public class SearchQueryImpl implements SearchQuery {

   public String fieldQuery(String field, String queryParam, Client client) {
      SearchResponse response = client.prepareSearch(Constant.INDEX_NAME)
            .setTypes(Constant.TYPE_NAME)
            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            .setQuery(QueryBuilders.termQuery(field, queryParam)).setFrom(0)
            .setSize(10).setExplain(true).execute().actionGet();
      return response.toString();
   }

   public String matchQuery(String field, String queryParam, Client client) {
      SearchResponse response = client.prepareSearch(Constant.INDEX_NAME)
            .setTypes(Constant.TYPE_NAME)
            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            .setQuery(QueryBuilders.matchQuery(field, queryParam)).setFrom(0)
            .setSize(10).setExplain(true).execute().actionGet();
      return response.toString();
   }

   public String rangeQuery(String field, int from, int to, Client client) {
      SearchResponse response =
            client.prepareSearch(Constant.INDEX_NAME)
                  .setTypes(Constant.TYPE_NAME)
                  .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                  .setPostFilter(
                        FilterBuilders.rangeFilter(field).from(from).to(to))
            .setFrom(0).setSize(10).setExplain(true).execute().actionGet();
      return response.toString();
   }

   public List<Bug> getSearchResults(String response, Client client) {
      JSONObject obj = new JSONObject(response);
      JSONArray results = obj.getJSONObject("hits").getJSONArray("hits");
      List<Bug> bugs = new ArrayList<Bug>();
      ObjectMapper mapper = new ObjectMapper();
      for (int i = 0; i < results.length(); i++) {
         String result = results.getJSONObject(i).get("_source").toString();
         try {
            Bug r = mapper.readValue(result, Bug.class);
            bugs.add(r);
         } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            return null;
         }
      }
      return bugs;
   }

}
