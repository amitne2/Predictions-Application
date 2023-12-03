package environment;

import property.EntityProperty;

import java.util.Collection;

public interface EnvVariablesManager {
    public EntityProperty getProperty(String name);
    public void addProperty(EntityProperty property);
    public Collection<EntityProperty> getEnvVariables();


    }
