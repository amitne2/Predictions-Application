package action;

import context.Context;
import entity.definition.EntityDefinition;
import entity.instance.EntityInstanceImpl;
import property.EntityDefProperty;
import property.EntityProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReplaceAction extends Action {

    private String killEntityName;
    private String createEntityName;
    private String mode;

    public ReplaceAction (String actionType ,String killEntityName, String createEntityName, String mode) {
        super(actionType);
        this.killEntityName = killEntityName;
        this.createEntityName = createEntityName;
        this.mode = mode;
    }

    public ReplaceAction (String actionType ,String killEntityName, String createEntityName, String mode, SeconderyEntity seconderyEntity) {
        super(actionType, seconderyEntity);
        this.killEntityName = killEntityName;
        this.createEntityName = createEntityName;
        this.mode = mode;
    }

    @Override
    public void doAction(Context context) throws Exception {
        Random random = new Random();
        if(mode.equals("scratch")) { //kill entity and create new from scratch
            //kill the instance and free his location on grid
            context.getPrimaryEntityInstance().KillInstance();
            context.getGridInstances().freeLocationOnGrid(context.getPrimaryEntityInstance().getPositionOnGrid());

            //create new instance by the value in the entity create definition
            EntityDefinition entityToCreate = context.getWorld().getEntityByName(createEntityName);
            int row = random.nextInt(context.getWorld().getRowsGrid());
            int col = random.nextInt(context.getWorld().getColsGrid());

            while(!(context.getGridInstances().isThisPositionFree(row, col))) {
                row = random.nextInt(context.getWorld().getRowsGrid());
                col = random.nextInt(context.getWorld().getColsGrid());
            }
            EntityInstanceImpl entityInstance = context.getEntityInstanceManager().create(entityToCreate, row, col);
            context.getGridInstances().setInstanceOnLocation(entityInstance);

        } else if(mode.equals("derived")) { //take from kill entity the values of property if the same type and name to the new entity

            EntityDefinition entityToCreate = context.getWorld().getEntityByName(createEntityName);
            List<EntityDefProperty> entityCreatedProperties = entityToCreate.getProperties();

            List<EntityDefProperty> newPropertiesInEntityCreated = new ArrayList<>();
            //create new list from the original list so we can change the element if needed
            for(EntityDefProperty entityDefProperty: entityCreatedProperties) {
                newPropertiesInEntityCreated.add(new EntityDefProperty(entityDefProperty));
            }

            for(EntityDefProperty property: newPropertiesInEntityCreated) {
                //check if this property exists in kill entity
                if(context.getPrimaryEntityInstance().getPropertyByName(property.getName()) != null) { //exists property with the same name
                    EntityProperty propertyToCopy = context.getPrimaryEntityInstance().getPropertyByName(property.getName());
                    if(propertyToCopy.getType() == property.getType()) { //the same property name and type
                        Object value = propertyToCopy.getValue();//take value
                        property.setRandomInitialize(false);
                        property.setInitValue(value);
                    }
                }
            }
            //steal location of kill entity
            int row = context.getPrimaryEntityInstance().getPositionOnGrid().getRowLocation();
            int col = context.getPrimaryEntityInstance().getPositionOnGrid().getColLocation();
            context.getPrimaryEntityInstance().KillInstance(); //kill the primary instance

            EntityInstanceImpl entityInstance = context.getEntityInstanceManager().createWithPropertiesList(entityToCreate, entityCreatedProperties, row, col);
            context.getGridInstances().setInstanceOnLocation(entityInstance);//update entity in grid

        } else {
            throw new Exception("Mode in replace action is undefined. The mode need to be scratch/derived");
        }
    }

    @Override
    protected Object getValue(Map<String, EntityProperty> propertyMap, String by) throws Exception {
        return null;
    }

    @Override
    public String getActionName() {
        return "replace";
    }

    @Override
    public String getActiveName() {
        return super.getActiveName();
    }

    @Override
    public String getEntityName() {
        return killEntityName;
    }

    public String getCreateEntityName() {
        return createEntityName;
    }

    public String getKillEntityName() {
        return killEntityName;
    }

    public String getMode() {
        return mode;
    }
}
