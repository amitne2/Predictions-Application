package dto.HistoryDataDTO;

import dto.EntityDefPropertyDTO;
import dto.EntityInstanceDTO;
import dto.EntityPropertyInstanceDTO;
import property.EntityProperty;

import java.util.*;


public class HistogramProperiesDTO extends HistoryDataDTO {

        private List<EntityInstanceDTO> entitiesInstances;

    public HistogramProperiesDTO() {
        entitiesInstances = new ArrayList<>();
    }

    public void setEntitiesInstances(EntityInstanceDTO entityInstanceDTO) {
        entitiesInstances.add(entityInstanceDTO);
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for(EntityInstanceDTO entityInstanceDTO: entitiesInstances) {
            names.add(entityInstanceDTO.getName());
        }
        return names;
    }

    public Collection<EntityPropertyInstanceDTO> getListProperties(int index) {
        return entitiesInstances.get(index).getEntityProperiesDetails();
    }

    public int getSizeOfInstances() {
        return entitiesInstances.size();
    }
    @Override
    public Map<Object, Integer> getHistogramDetailOnPropery(int entityIndex,int propertyIndex) {
        int count;
        Map<Object, Integer> data = new HashMap<>();
        EntityInstanceDTO entityInstanceDTO = entitiesInstances.get(entityIndex);
        String propertyName = entityInstanceDTO.getProperiesNames().get(propertyIndex);
        EntityPropertyInstanceDTO propertyInstanceDTO = entityInstanceDTO.getProperty(propertyName);
        String type = propertyInstanceDTO.getType();

        for(EntityInstanceDTO instanceDTO: entitiesInstances) {
            Object value = instanceDTO.getEntityPropertyInstanceDTO(propertyName).getValue();
            if(data.containsKey(value)) {
                count = data.get(value);
                count++;
                data.replace(value, count);
            } else { //new value
                data.put(value, 1);
            }
        }
        return data;
    }
}
