package ella.sam.models;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiMatch {
    String keyword;
    Set<String> fields;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public Map<String, Object> jsonMap() {
        Map<String, Object> jsonMap = new HashMap<>();
        if (StringUtils.isEmpty(keyword) || CollectionUtils.isEmpty(fields)){
            throw new RuntimeException("Either keyword or fields is emtpyt");
        }
        Map<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put("query", keyword);
        fieldsMap.put("fields", fields);
        fieldsMap.put("type", "phrase");
        fieldsMap.put("slop", 2);
        jsonMap.put("multi_match", fieldsMap);
        return jsonMap;
    }
}
