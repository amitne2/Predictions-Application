package environment;

import dto.EnvVariablesIncstanceDTO;
import dto.EnvironmentDataDTO;
import entity.Type;
import javafx.util.Pair;
import property.EntityProperty;
import property.Property;

import java.util.*;

public class EnvVariableManagerImpl implements EnvVariablesManager {

    private Map<String, EntityProperty> envVariables;
    private Map<String, Boolean> envValueWasRandomMap;

    public EnvVariableManagerImpl() {
        envVariables = new HashMap<>();
        envValueWasRandomMap = new HashMap<>();
    }

    @Override
    public EntityProperty getProperty(String name) {
        if (!envVariables.containsKey(name)) {
            throw new IllegalArgumentException("Can't find env variable with name " + name);
        }
        return envVariables.get(name);
    }

    @Override
    public void addProperty(EntityProperty property) {
        envVariables.put(property.getName(), property);

    }

    @Override
    public Collection<EntityProperty> getEnvVariables() {
        return envVariables.values();
    }

    public void setValue(Object value, String name, EntityProperty entityProperty) throws Exception {
        if (entityProperty.getType() == Type.DECIMAL) {
            try {
                int res = (int) value;
                entityProperty.setValue(res);
            } catch (Exception e) {
                throw new Exception("Invalid value. need to enter a int value " +
                        "for environment property name: " + entityProperty.getName());
            }
        } else if (entityProperty.getType() == Type.FLOAT) {
            try {
                float res = (float) value;
                entityProperty.setValue(res);
            } catch (Exception e) {
                throw new Exception("Invalid value. need to enter a float value " +
                        "for environment property name: " + entityProperty.getName());
            }
        } else if (entityProperty.getType() == Type.BOOLEAN) {
            try {
                if ((boolean) value) {
                    entityProperty.setValue(true);
                } else if (!(boolean) value) {
                    entityProperty.setValue(false);
                }
            } catch (Exception e) {
                throw new Exception("Invalid value. need to enter a boolean value (true/false) " +
                        "for environment property name: " + entityProperty.getName());
            }
        } else {
            entityProperty.setValue(value);
        }

        envVariables.put(name, entityProperty);
    }

    public void setValueRandom(String name, EntityProperty entityProperty) throws Exception {
        Random random = new Random();
        if (entityProperty.getType() == Type.DECIMAL) {
            try {
                if (entityProperty.isExistRange()) {
                    int res = (int) entityProperty.getFromRange() +
                            random.nextInt((int) entityProperty.getToRange() - (int) entityProperty.getFromRange() + 1);
                    entityProperty.setValue(res);
                } else {
                    int res = random.nextInt();
                    entityProperty.setValue(res);
                }
            } catch (Exception e) {
                throw new Exception("error while random int for property: " + entityProperty.getName());
            }
        } else if (entityProperty.getType() == Type.FLOAT) {
            try {
                if (entityProperty.isExistRange()) {
                    float res = entityProperty.getFromRange() +
                            random.nextFloat() * (entityProperty.getToRange() - entityProperty.getFromRange());
                    entityProperty.setValue(res);
                }
            } catch (Exception e) {
                throw new Exception("error while random float for property: " + entityProperty.getName());
            }
        } else if (entityProperty.getType() == Type.BOOLEAN) {
            try {
                boolean res = random.nextBoolean();
                entityProperty.setValue(res);
            } catch (Exception e) {
                throw new Exception("error while random boolean for property: " + entityProperty.getName());
            }
        } else if (entityProperty.getType() == Type.STRING) {
            try {
                int size = random.nextInt(50); //random size between 1 to 50
                String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+~`|}{[]:;?><,./-= ";
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < size; i++) {
                    int randomIndex = random.nextInt(characters.length());
                    stringBuilder.append(characters.charAt(randomIndex));
                }
                String res = stringBuilder.toString();
                entityProperty.setValue(res);
            } catch (Exception e) {
                throw new Exception("error while random string for property: " + entityProperty.getName());
            }
        } else {
            throw new Exception("unknown issue for property name: " + entityProperty.getName());

        }
    }

    public void setEnvVariables(Map<String, EntityProperty> envVariables) {
        this.envVariables = envVariables;
    }

    public void createEnvInstances(Collection<Property> envVariableManagerDefinition, Map<String, EnvVariablesIncstanceDTO> environmentDataDTO) throws Exception {
        Object value;
        for(Property property: envVariableManagerDefinition) {
            value = environmentDataDTO.get(property.getName()).getValue();
            EntityProperty envProperty = new EntityProperty(property, value);
            envVariables.put(property.getName(), envProperty);
            envValueWasRandomMap.put(property.getName(),environmentDataDTO.get(property.getName()).isWasRandom());
        }
    }

    public boolean envWasRandom(String envName) {
        return envValueWasRandomMap.get(envName);
    }
    public Object getEnvValue(String envName) {
        return envVariables.get(envName).getValue();
    }
}