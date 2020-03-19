package ella.sam.models;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Aggregation {
    private String field;
    private String operation;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, Object> jsonMap() {
        Map<String, Object> jsonMap = new HashMap<>();
        if (StringUtils.isEmpty(field)) {
            throw new RuntimeException("There should be field name in aggregation");
        }
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("field", field);
        if (StringUtils.isEmpty(operation)) {
            throw new RuntimeException("There should be operation name in aggregation");
        }
        Map<String, Object> operationMap = new HashMap<>();
        operationMap.put(operation, fieldMap);
        jsonMap.put(field, operationMap);
        return jsonMap;
    }


}
