package environment.helperFunctions.environment;

import entity.Type;
import environment.EnvVariablesManager;
import property.EntityProperty;

public class ActiveEnvironmentImpl {

    public static Object getEnvValue(EnvVariablesManager envVariablesManager, String name) throws Exception {
        EntityProperty envProperty = envVariablesManager.getProperty(name);
        if (envProperty == null) {
            throw new Exception("The name" + name + "doesn't exist in environment list");
        } else {
            try {
                switch (envProperty.getType()) {
                    case DECIMAL:
                        return (int) envProperty.getValue();
                    case FLOAT:
                        return (float) envProperty.getValue();
                    case STRING:
                        return (String) envProperty.getValue();
                    case BOOLEAN:
                        return (boolean) envProperty.getValue();
                }
            } catch (Exception e) {
                throw new Exception("can't convert the value in env: " + name + "for type: " + envProperty.getType());
            }
        }
        return null;
    }

    public static Object converteEnvValueifNeeded(Object resValue, Type propertyType) {
        if((resValue instanceof Integer) && propertyType == Type.FLOAT) {
            return (float) resValue;
        }
         else if(resValue instanceof String && propertyType==Type.BOOLEAN) {
            if (((String) resValue).equalsIgnoreCase("true"))
                return (boolean) true;
            else if (((String) resValue).equalsIgnoreCase("false"))
                return (boolean) false;
        }
         return resValue;
    }
}
