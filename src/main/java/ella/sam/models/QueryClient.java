package ella.sam.models;

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

    @PreDestroy
    private void close() {
        try {
            queryClient.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close elasticsearch client", e);
        }
    }

}
