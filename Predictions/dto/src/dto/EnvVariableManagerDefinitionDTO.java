package dto;

import java.util.Collection;
import java.util.Map;

public class EnvVariableManagerDefinitionDTO {

    private Map<String, PropertyDTO> envVariables;

    public EnvVariableManagerDefinitionDTO(Map<String, PropertyDTO> envVariables) {
        this.envVariables = envVariables;
    }

    public Map<String, PropertyDTO> getEnvVariables(){
        return envVariables;
    }

    public Collection<PropertyDTO> getPropertiesData(){
        return envVariables.values();
    }

    public void putElement(String name, PropertyDTO propertyDTO) {
        envVariables.put(name, propertyDTO);
    }

    public PropertyDTO getEnvProperty(String name) {
        return envVariables.get(name);
    }

}
