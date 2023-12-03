package entity.definition;

import property.EntityDefProperty;

import java.util.List;

public interface EntityDefinition {

    public int getPopulation();
    public List<EntityDefProperty> getProperties();
    public String getName();



}
