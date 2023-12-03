package environment.definition;

import dto.EnvVariableManagerDefinitionDTO;
import dto.PropertyDTO;
import property.EntityProperty;
import property.Property;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EnvVariableManagerDefinition {
    private Map<String, Property> envVariables;

    public EnvVariableManagerDefinition(){
        envVariables = new HashMap<>();
    }

    public Property getProperty(String name) {
        if (!envVariables.containsKey(name)) {
            throw new IllegalArgumentException("Can't find env variable with name " + name);
        }
        return envVariables.get(name);
    }

    public void addProperty(Property property) {
        envVariables.put(property.getName(), property);

    }

    public Collection<Property> getEnvVariables() {
        return envVariables.values();
    }

    public void setEnvVariables(Map<String, Property> envVariables) {
        this.envVariables = envVariables;
    }

    public EnvVariableManagerDefinitionDTO creEnvVariableManagerDefinitionDTO(){
        Map <String, PropertyDTO> envVariablesDTO = new HashMap<>();
        int index=1;
        for (Property property: envVariables.values()) {
            PropertyDTO propertyDTO = new PropertyDTO(property.getName(), property.getType().toString(),
                    property.isExistRange(), property.getFromRange(), property.getToRange());
            envVariablesDTO.put(property.getName(), propertyDTO);
            index++;
        }
        EnvVariableManagerDefinitionDTO envVariableManagerDefinitionDTO = new EnvVariableManagerDefinitionDTO(envVariablesDTO);
        return  envVariableManagerDefinitionDTO;
    }
}
