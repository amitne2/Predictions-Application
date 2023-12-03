package dto;


import entity.Type;
import entity.instance.EntityInstanceImpl;
import javafx.util.Pair;
import property.ChangesValueDuration;
import property.EntityDefProperty;
import property.EntityProperty;
import simulations.simulationExecution.simulation.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinishSimulationDataDTO {
    private HashMap<Object, Integer> propertyHistogram;
    private HashMap<String, List<PropertyFinishDataDTO>> propertiesData;

    private long consistency;
    private boolean isNumber;
    private float averageValue;

    public FinishSimulationDataDTO() {
        propertyHistogram = new HashMap<>();
        propertiesData = new HashMap<>();
    }

    public void setPropertiesHistogram(Simulation simulation, String entityName, String propertyName) {
        propertyHistogram = new HashMap<>();
        for(String entity: simulation.getEntityInstanceManager().getInstances().keySet()) {
            if(entity.equals(entityName)) {
                for(EntityInstanceImpl entityInstance: simulation.getEntityInstanceManager().getInstancesListByName(entityName)) {
                    EntityProperty property = entityInstance.getPropertyByName(propertyName);
                    if(property != null) {
                        if(propertyHistogram.get(property.getValue())!= null) {
                            Integer counter = propertyHistogram.get(property.getValue());
                            propertyHistogram.put(property.getValue(), counter + 1);
                        }else { //add to hashmap
                            propertyHistogram.put(property.getValue(), 1);
                        }
                    }
                }
            }
        }
    }

    /*public void setPropertyDetails(Simulation simulation) {
        long numOfTicksWithoutChanges = 0;
        long numOfTicksPerInstance = 0;
        boolean isNumber = false;
        float value = 0;

        //for each entity name
        for(String entityName: simulation.getWorld().getEntityDefinitionMap().keySet()) {
            List <PropertyFinishDataDTO> propertyFinishDataDTOList = new ArrayList<>();
            //for each property in entity
            for(EntityDefProperty entityProperty: simulation.getWorld().getEntity(entityName).getProperties()) {
                numOfTicksWithoutChanges = 0;
                isNumber = false;
                if(entityProperty.getType() == Type.FLOAT || entityProperty.getType() == Type.DECIMAL)
                    isNumber = true;

                //for each instance with the same name as entityName
                for(EntityInstanceImpl entityInstance: simulation.getEntityInstanceManager().getInstancesListByName(entityName)) {
                    EntityProperty property = entityInstance.getPropertyByName(entityProperty.getName());
                    numOfTicksPerInstance =0;

                    if(isNumber) { //get the value for average
                        if(property.getType() == Type.DECIMAL)
                            value += (int)property.getValue();
                        else
                            value +=(float) property.getValue();
                         }

                    //for each changes in changesValueDuration list
                    for(ChangesValueDuration changesValueDuration: property.getChangesValueList()) {
                        numOfTicksPerInstance += (changesValueDuration.getEndTick() - changesValueDuration.getStartTick());
                    }
                    numOfTicksWithoutChanges += numOfTicksPerInstance / property.getChangesValueList().size();

                    } //for all instances from the same entity
                value = value / simulation.getEntityInstanceManager().getInstances().get(entityName).size();
                PropertyFinishDataDTO propertyFinishDataDTO = new PropertyFinishDataDTO(numOfTicksWithoutChanges, isNumber, value);
                propertyFinishDataDTOList.add(propertyFinishDataDTO);
                numOfTicksWithoutChanges =0;
                } //for each property

            propertiesData.put(entityName, propertyFinishDataDTOList);
            } //for each entity
        }*/

    public HashMap<Object, Integer> getPropertyHistogram() {
        return propertyHistogram;
    }

    public HashMap<String, List<PropertyFinishDataDTO>> getPropertiesData() {
        return propertiesData;
    }

    public void setDataForProperty(Simulation simulation, String entityName, String propertyName, long totalTicks){
        setPropertiesHistogram(simulation, entityName, propertyName);
        setConsistencyAndAverageForProperty(simulation, entityName, propertyName, totalTicks);
    }

    public void setConsistencyAndAverageForProperty(Simulation simulation, String entityName, String propertyName, long totalTicks) {
        consistency = 0;
        averageValue = 0;
        float totalValue =0 ;
        int count =0;
        isNumber = false;
        double tickHistorySum =0;
                for(EntityInstanceImpl entityInstance: simulation.getEntityInstanceManager().getInstancesListByName(entityName)) {
                    EntityProperty property = entityInstance.getPropertyByName(propertyName);
                    for(ChangesValueDuration changesValueDuration: property.getChangesValueList()) {
                        tickHistorySum += (changesValueDuration.getEndTick() - changesValueDuration.getStartTick());
                    }
                    if(property.getChangesValueList().size() > 1)
                        consistency += (tickHistorySum / property.getChangesValueList().size());

                   tickHistorySum = 0;

                   String type = property.getType().toString();
                    if(type.equals("decimal")) {
                        isNumber = true;
                        totalValue += (int)property.getValue();
                        count++;
                    } else if(type.equals("float")) {
                        isNumber = true;
                        totalValue +=(float) property.getValue();
                        count++;
                    }
        }
        averageValue = totalValue/count;
    }

    public float getAverageValue() {
        return averageValue;
    }

    public long getConsistency() {
        return consistency;
    }

    public boolean isNumber() {
        return isNumber;
    }
}
