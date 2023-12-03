package dto;


import java.util.*;

public class EntityInstanceDTO {

    private String name;
    private int id;
    private Map<String, EntityPropertyInstanceDTO> entityProperiesDetails;

    public EntityInstanceDTO(String name, int id, List<EntityPropertyInstanceDTO> entityPropertyInstanceDTO) {
        this.name = name;
        this.id = id;
        this.entityProperiesDetails = new HashMap<>();
        for(EntityPropertyInstanceDTO property: entityPropertyInstanceDTO) {
            entityProperiesDetails.put(property.getName(), property);
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<String> getProperiesNames() {
        List<String> names = new ArrayList<>();
        for(EntityPropertyInstanceDTO entityPropertyInstanceDTO: entityProperiesDetails.values()) {
            names.add(entityPropertyInstanceDTO.getName());
        }
        return names;
    }
    public Collection<EntityPropertyInstanceDTO> getEntityProperiesDetails() {
        return entityProperiesDetails.values();
    }

    public EntityPropertyInstanceDTO  getProperty(String name) {
        return entityProperiesDetails.get(name);
    }

    public EntityPropertyInstanceDTO getEntityPropertyInstanceDTO(String propertyName) {
        return entityProperiesDetails.get(propertyName);
    }
}
