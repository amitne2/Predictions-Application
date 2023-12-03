package entity.instance;

import action.Action;
import environment.EnvVariableManagerImpl;
import property.EntityProperty;
import rule.Rule;

import java.util.Map;

public interface EntityInstance {

    public EntityProperty getDetailOfProperty(String name) throws Exception;


    public Map<String, EntityProperty> getProperiesMap();
    public void addPropertyInstance(EntityProperty entityProperty);

    public int getId();

    public String getName();

    }