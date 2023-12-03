package dto;

import javafx.util.Pair;

import java.util.Map;

public class EnvironmentDataDTO {

    private Map<String, Object> envValues;

    public EnvironmentDataDTO(Map<String, Object> data) {
        this.envValues = data;
    }

    public Map<String, Object> getEnvValues() {
        return envValues;
    }

    public Object getValue(String envName) {
        return envValues.get(envName);
    }

}
