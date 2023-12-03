package environment.helperFunctions.evaluate;

import context.Context;
import property.EntityProperty;

public class ActiveEvaluateImpl {

    public static Object getEvaluateProperty(Context context, String entity, String property) throws Exception {
        Object resValue = null;
        if(context.getPrimaryEntityInstance().getName().equals(entity)) {
            if(context.getPrimaryEntityInstance().getPropertyByName(property) != null) {
                resValue = context.getPrimaryEntityInstance().getPropertyByName(property).getValue();
            } else { //no property with this name in entity
                throw new Exception("Entity " + entity + " doesn't property in name: " + property + ". It's necessary for function evaluate.");
            }

        } else if(context.getSecondaryEntityInstance()!=null) {
            if(context.getSecondaryEntityInstance().getName().equals(entity)) {
            if(context.getSecondaryEntityInstance().getPropertyByName(property) !=  null)
                resValue = context.getPrimaryEntityInstance().getPropertyByName(property).getValue();
             else //no property with this name in entity
                throw new Exception("Entity " + entity + " doesn't property in name: " + property + ". It's necessary for function evaluate.");
            }
        }
        else {
            String entityPrimary = context.getPrimaryEntityInstance().getName();
            String entitySecondary = context.getSecondaryEntityInstance().getName();
            throw new Exception("No exists entity instance, require: " + entity + " but only exist in this instance: "
                    + entityPrimary + " and " + entitySecondary);
        }
        return resValue;
    }
}
