package ella.sam.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ella.sam.models.MovieDTO;
import ella.sam.models.QueryClient;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class MovieQuerier {
    @Autowired
    private QueryClient queryClient;


    public List<MovieDTO> browseMovie(int size, int from) {
        Request request = new Request("GET", "/_search");
        if (size <= 0 || from < 0) {
            throw new RuntimeException(String.format("'size' %d cannot lesseq than 0 and 'from' %d cannot less than 0", size, from));
        }
        request.addParameter("from", Objects.toString(from));
        request.addParameter("size", Objects.toString(size));
        Response response = queryClient.perform(request);
        ObjectMapper objectMapper = new ObjectMapper();
        String responseString;
        try {
            responseString = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("Can't read response", e);
        }
        try {
            return objectMapper.readValue(responseString, new TypeReference<List<MovieDTO>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Can't convert response to MovieDTO");
        }

    }


}
