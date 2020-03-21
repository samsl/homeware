package ella.sam.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ella.sam.models.MovieDTO;
import ella.sam.models.QueryBody;
import ella.sam.models.QueryClient;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieQuerier {
    @Autowired
    private QueryClient queryClient;


    public List<MovieDTO> browseMovie(Integer from, Integer size, QueryBody queryBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = makeRequest(from, size, queryBody.jsonString());
        if (responseMap.containsKey("hits")) {
            List<Map<String, Object>> hitsList = (List<Map<String, Object>>) ((Map<String, Object>) responseMap.get("hits")).get("hits");
            List<Map<String, Object>> moviesMap = hitsList.stream().map(e -> (Map<String, Object>) e.get("_source")).collect(Collectors.toList());
            return moviesMap.stream().map(m -> objectMapper.convertValue(m, MovieDTO.class)).collect(Collectors.toList());
        } else {
            throw new RuntimeException("Can't find movies");
        }

    }

    private Map<String, Object> makeRequest(Integer from, Integer size, String jsonString) {
        Request request = new Request("GET", "/_search");
        if (size != null && size > 0 && from != null && from > 0) {
            request.addParameter("from", Objects.toString(from));
            request.addParameter("size", Objects.toString(size));
        }
        request.setJsonEntity(jsonString);
        Response response = queryClient.perform(request);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseString;
        try {
            responseString = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("Can't read response", e);
        }
        try {
            return objectMapper.readValue(responseString, new TypeReference<Map<String, Object>>() {
            });

        } catch (IOException e) {
            throw new RuntimeException("Can't convert response to map");
        }
    }


    public Map<String, Class> getFieldMapping() {
        Map<String, Object> mapping = this.queryClient.getMapping();
        Map<String, Class> typeMapping = new HashMap<>();
        if (mapping.containsKey("properties")) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) mapping.get("properties")).entrySet()) {
                String field = entry.getKey();
                String type = (String) ((Map<String, Object>) entry.getValue()).get("type");
                if ("keyword".equalsIgnoreCase(type) || "text".equalsIgnoreCase(type)) {
                    typeMapping.put(field, String.class);
                } else if ("float".equalsIgnoreCase(type)) {
                    typeMapping.put(field, Float.class);
                } else {
                    typeMapping.put(field, Integer.class);
                }

            }
        }
        return typeMapping;
    }


    public Set<Object> getFieldSet(Integer from, Integer size, QueryBody queryBody) {

        String field = queryBody.getAggregation().getField();
        Map<String, Object> responseMap = makeRequest(from, size, queryBody.jsonString());
        if (responseMap.containsKey("aggregations")) {
            List<Map<String, Object>> buckets = (List<Map<String, Object>>) ((Map<String, Object>) ((Map<String, Object>) responseMap.get("aggregations")).get(field)).get("buckets");
            return buckets.stream().map(e -> e.get("key")).collect(Collectors.toSet());

        } else {
            throw new RuntimeException("No aggregations result in response");
        }


    }


}
