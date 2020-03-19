package ella.sam.models;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Sort {
    private String field;
    private String order;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Map<String, String> jsonMap() {
        Map<String, String> jsonMap = new HashMap<>();
        if (StringUtils.isEmpty(field) || StringUtils.isEmpty(order)) {
            throw new RuntimeException("Either field or order is blank");
        }
        jsonMap.put(field, order);
        return jsonMap;
    }
}
