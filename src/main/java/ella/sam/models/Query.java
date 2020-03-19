package ella.sam.models;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Query {
    private String field;
    private String operation;
    private Object opValue;

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

    public Object getOpValue() {
        return opValue;
    }

    public void setOpValue(Object opValue) {
        this.opValue = opValue;
    }

    public Map<String, Object> jsonMap() {
        Map<String, Object> jsonMap = new HashMap<>();
        if (StringUtils.isEmpty(field)) {
            throw new RuntimeException("There should be filed name in query");
        }
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put(field, opValue);
        if (StringUtils.isEmpty(operation)) {
            throw new RuntimeException("There should be operation name in query");
        }
        jsonMap.put(operation, fieldMap);
        return jsonMap;
    }

}
