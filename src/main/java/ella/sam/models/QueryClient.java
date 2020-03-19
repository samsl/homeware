package ella.sam.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.elasticsearch.client.RestClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class QueryClient {
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.mapping}")
    private String mapping;

    private RestClient queryClient;

    public QueryClient() {

    }

    @PostConstruct
    private void init() {
        queryClient = RestClient.builder(new HttpHost(host, port, "http")).build();
    }

    public Response perform(Request request) {
        try {
            return queryClient.performRequest(request);
        } catch (IOException e) {
            throw new RuntimeException("Can't send request", e);
        }
    }

    public Map<String, Object> getMapping() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(mapping, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Can't read mapping correctly " + mapping);
        }
    }

    @PreDestroy
    private void close() {
        try {
            queryClient.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close elasticsearch client", e);
        }
    }

}
