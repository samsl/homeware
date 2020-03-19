package ella.sam.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryBody {
    private MultiMatch multiMatch;
    private List<Query> filter;
    private Aggregation aggregation;
    private Sort sort;

    public MultiMatch getMultiMatch() {
        return multiMatch;
    }

    public void setMultiMatch(MultiMatch multiMatch) {
        this.multiMatch = multiMatch;
    }

    public List<Query> getFilter() {
        return filter;
    }

    public void setFilter(List<Query> filter) {
        this.filter = filter;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public void setAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public String jsonString() {
        Map<String, Object> jsonMap = new HashMap<>();

        if (multiMatch != null || filter !=null) {
            Map<String, Object> queryMap = new HashMap<>();
            Map<String, Object> boolMap = new HashMap<>();
            if (multiMatch != null) {
                Map<String, Object> multiMatchMap = multiMatch.jsonMap();
                boolMap.put("must", multiMatchMap);
            }
            if (filter != null) {
                List<Map<String, Object>> filterMap = filter.stream().map(Query::jsonMap).collect(Collectors.toList());
                boolMap.put("filter", filterMap);
            }
            queryMap.put("bool", boolMap);
            jsonMap.put("query", queryMap);
        }
        if (aggregation != null) {
            if (CollectionUtils.isEmpty(jsonMap)) {
                jsonMap.put("size", 0);
            }
            Map<String, Object> aggregationMap = aggregation.jsonMap();
            jsonMap.put("aggs", aggregationMap);
        }
        if (sort != null) {
            Map<String, String> sortMap = sort.jsonMap();
            jsonMap.put("sort", sortMap);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't convert json map to string");
        }

    }
}
