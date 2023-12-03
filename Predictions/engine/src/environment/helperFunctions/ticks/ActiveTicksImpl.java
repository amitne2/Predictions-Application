package environment.helperFunctions.ticks;

import context.Context;
import property.ChangesValueDuration;
import property.EntityProperty;

import java.util.List;

public class ActiveTicksImpl {

public static long getTicksOfChangeActive(Context context, String entityName, String propertyName) throws Exception {
    long currTick = context.getCurrTick();
    long lastChangesTick = 0;
    if(context.getPrimaryEntityInstance() != null) {
        if(context.getPrimaryEntityInstance().getName().equals(entityName)) {
            if (context.getPrimaryEntityInstance().getTypeOfPropertyByName(propertyName) != null) {
                EntityProperty entityProperty = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
                List<ChangesValueDuration> propertyHistory = entityProperty.getChangesValueList();
                lastChangesTick = propertyHistory.get(propertyHistory.size() - 1).getStartTick();
            } else
                throw new Exception("The entity: " + entityName + " doesn't have property in name: "
                        + propertyName + ". error in function ticks.");
            }
        } else if(context.getSecondaryEntityInstance() != null) {
        if (context.getSecondaryEntityInstance() != null) {
            if (context.getSecondaryEntityInstance().getName().equals(entityName)) {
                if (context.getSecondaryEntityInstance().getTypeOfPropertyByName(propertyName) != null) {
                    EntityProperty entityProperty = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
                    List<ChangesValueDuration> propertyHistory = entityProperty.getChangesValueList();
                    lastChangesTick = propertyHistory.get(propertyHistory.size() - 1).getStartTick();
                } else
                    throw new Exception("The entity: " + entityName + " doesn't have property in name: "
                            + propertyName + ". error in function ticks.");
            }
        }
    }
            else
                throw new Exception("Doesn't have entity in name: " + entityName + " on this instance. error in ticks function.");

        return currTick - lastChangesTick;
    }
}
