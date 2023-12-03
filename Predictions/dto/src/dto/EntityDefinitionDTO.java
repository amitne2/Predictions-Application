package dto;

import entity.Type;
import entity.definition.EntityDefinitionImpl;
import property.EntityDefProperty;

import java.util.ArrayList;
import java.util.List;

public class EntityDefinitionDTO {

    private String name;
    private int population;
    private List<EntityDefPropertyDTO> properties;

    public EntityDefinitionDTO(String name, int population, List <EntityDefPropertyDTO> properties) {
        this.name=name;
        this.population = population;
        this.properties = properties;
    }

    public int getPopulation() { return population;}

    public List <EntityDefPropertyDTO> getProperties() { return properties; }

    public String getName() { return name; }

    public EntityDefPropertyDTO getPropertyByName(String name){
        for(EntityDefPropertyDTO entityDefPropertyDTO: properties) {
            if(entityDefPropertyDTO.getName().equals(name)) {
                return entityDefPropertyDTO;
            }
        }
        return null;
    }




}
