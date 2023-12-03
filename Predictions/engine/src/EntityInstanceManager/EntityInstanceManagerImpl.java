package EntityInstanceManager;

import dto.EntityInstanceDTO;
import dto.EntityPropertyInstanceDTO;
import dto.HistoryDataDTO.HistogramProperiesDTO;
import entity.definition.EntityDefinition;
import entity.instance.EntityInstanceImpl;
import property.EntityDefProperty;
import property.EntityProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class EntityInstanceManagerImpl implements EntityInstanceManager {
    private int count;
    private HashMap<String, List<EntityInstanceImpl>> instances;

    public EntityInstanceManagerImpl() {
        count = 0;
        instances = new HashMap<>();
    }

    public EntityInstanceImpl create(EntityDefinition entityDefinition, int row, int col) {
        count++;
        EntityInstanceImpl newEntityInstance=null;
         newEntityInstance = new EntityInstanceImpl(entityDefinition.getName(),entityDefinition.getProperties(), count, row, col);
        if(instances.get(entityDefinition.getName()) !=  null ){
            instances.get(entityDefinition.getName()).add(newEntityInstance);
        } else {
            List<EntityInstanceImpl> instanceList = new ArrayList<>();
            instanceList.add(newEntityInstance);
            instances.put(entityDefinition.getName(), instanceList);
        }
        return newEntityInstance;
    }

    public EntityInstanceImpl createWithPropertiesList(EntityDefinition entityDefinition, List <EntityDefProperty> propertiesList, int row, int col) {
        count++;
        EntityInstanceImpl newEntityInstance = null;
         newEntityInstance = new EntityInstanceImpl(entityDefinition.getName(),propertiesList, count, row, col);

        if(instances.get(entityDefinition.getName()) !=  null ){
            instances.get(entityDefinition.getName()).add(newEntityInstance);
        } else {
            List<EntityInstanceImpl> instanceList = new ArrayList<>();
            instanceList.add(newEntityInstance);
            instances.put(entityDefinition.getName(), instanceList);
        }
        return newEntityInstance;
    }

    public HashMap<String, List<EntityInstanceImpl>> getInstances() {
        return instances;
    }

    @Override
    public void killEntity(int id) {
       for(List<EntityInstanceImpl> entityInstanceList: instances.values()) {
           for(EntityInstanceImpl entityInstance: entityInstanceList) {
               if (entityInstance.getId() == id) {
                   instances.remove(entityInstance);
                   break;
               }
           }
       }
    }

    public int getCountOfInstances(){
        int size = 0;
            for (List<EntityInstanceImpl> entityInstanceList : instances.values()) {
                size += entityInstanceList.size();
            }
        return size;
    }

    public HistogramProperiesDTO createHistogramPropertiesDTO(int entityNum) {
        HistogramProperiesDTO histogramProperiesDTO = new HistogramProperiesDTO();
        for(List<EntityInstanceImpl> entityInstanceList: instances.values()) {
            for (EntityInstanceImpl entityInstance : entityInstanceList) {
                List<EntityPropertyInstanceDTO> propertyInstanceDTOS = new ArrayList<>();
                for (EntityProperty property : entityInstance.getProperiesMap().values()) {
                    EntityPropertyInstanceDTO propertyInstanceDTO = new EntityPropertyInstanceDTO(property.getName(), property.getType().toString(), property.isExistRange(),
                            property.getFromRange(), property.getToRange(), property.getValue());
                    propertyInstanceDTOS.add(propertyInstanceDTO);
                }
                EntityInstanceDTO entityInstanceDTO = new EntityInstanceDTO(entityInstance.getName(), entityInstance.getId(), propertyInstanceDTOS);
                histogramProperiesDTO.setEntitiesInstances(entityInstanceDTO);
            }
        }
        return histogramProperiesDTO;
    }

    public List<EntityInstanceImpl> getInstancesListByName(String name) {
        return instances.get(name);
    }

    public void setInstances(HashMap<String, List<EntityInstanceImpl>> entityInstanceList) {
        this.instances = entityInstanceList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void removeInstance(EntityInstanceImpl entityInstance, int i) {
        String entityInstanceName = entityInstance.getName();
        List<EntityInstanceImpl> entityInstanceList = instances.get(entityInstanceName);
        entityInstanceList.remove(i); //return to it!!
    }

    public HashMap<String, Integer> getSimulationEntitiesProcess() {
        HashMap<String, Integer> entitiesData = new HashMap<>();
        for(String name: instances.keySet()) {
            entitiesData.put(name, instances.get(name).size());
        }
        return entitiesData;
    }

    public Integer getTotalInstaces() {
        int count =0;
        for(String name: instances.keySet()) {
            count += instances.get(name).size();
        }
        return count;
    }

    public int getCountOfEntityByName(String name) {
        return instances.get(name).size();
    }
}
