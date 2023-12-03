package entity.definition;

import dto.EntityDefPropertyDTO;
import dto.EntityDefinitionDTO;
import property.EntityDefProperty;

import java.util.ArrayList;
import java.util.List;

public class EntityDefinitionImpl implements EntityDefinition {
    private String name;
    private int population;
    private List<EntityDefProperty> properties;

    public EntityDefinitionImpl(String name, int population, List<EntityDefProperty> entityProperties) {
        this.name = name;
        this.population = population;
        properties = new ArrayList<>();
        for(EntityDefProperty property: entityProperties) {
            properties.add(property);
        }
    }

    @Override
    public int getPopulation() { return population;}

    @Override
    public List <EntityDefProperty> getProperties() { return properties; }

    @Override
    public String toString() {
        String res=  "Name Entity:" + name + ", List properties: ";
        for(EntityDefProperty property: properties) {
            res = res + property.toString();
        }
        return res;
    }

    @Override
    public String getName() { return name; }

    public EntityDefinitionDTO createEntityDefinitionDTO(){
        List <EntityDefPropertyDTO> entityDefPropertyDTOList = new ArrayList<>();
        for(EntityDefProperty property: properties) {
            entityDefPropertyDTOList.add(property.createEntityDefPropertyDTO());
        }
        EntityDefinitionDTO entityDefinitionDTO = new EntityDefinitionDTO(name, population, entityDefPropertyDTOList);
        return entityDefinitionDTO;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
