package entity;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Random;

public enum Type {
    DECIMAL{
        @Override
        public String toString() {
            return "decimal";
        }
    }, STRING {
        @Override
        public String toString() {
            return "string";
        }
    }, FLOAT{
        @Override
        public String toString() {
            return "float";
        }
    }, BOOLEAN{
        @Override
        public String toString() {
            return "boolean";
        }
    };


    public static Type getType(String name) {
        switch (name) {
            case "decimal":
                return DECIMAL;
            case "string":
                return STRING;
            case "float":
                return FLOAT;
            case "boolean":
                return BOOLEAN;
            default: {
                throw new IllegalArgumentException("Unknown data type: " + name);
            }

        }
    }


    // Return after parsing the init as string to the right rype
    public static Object getInitValue(Type type, String init) {
        Object res=null;
        switch (type) {
            case DECIMAL:
                try {
                    res = Integer.parseInt(init);
                } catch (NumberFormatException e) {
                    //need to handle
                }
                break;
            case FLOAT:
                try {
                    res = Float.parseFloat(init);
                } catch (NumberFormatException e) {
                    //need to handle
                }
                break;
            case STRING:
                try {
                    res = (String) init;
                } catch (NumberFormatException e) {
                    //need to handle
                }
                break;
            case BOOLEAN:
                try {
                    res = Boolean.parseBoolean(init);
                } catch (NumberFormatException e) {
                    //need to handle
                }

        }
        return res;
    }

    public static Object getRandomValue(Type type, float from, float to, boolean rangeExist) {
        Random random = new Random();
        Object res = null;
        switch (type) {
            case FLOAT:
                if(rangeExist) { //random from range
                    res = from + random.nextInt(((int)to - (int)from));
                }
                else { //random without range
                    res = random.nextFloat();
                }
                break;

            case DECIMAL:
                if(rangeExist) {
                    res = (int)from + random.nextInt(((int)to - (int)from));

                } else {
                    res = random.nextInt();
                }
                break;

            case BOOLEAN:
                res = random.nextBoolean();
                break;

            case STRING:
                int size = random.nextInt(50); //random size between 1 to 50
                String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+~`|}{[]:;?><,./-= ";
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < size; i++) {
                    int randomIndex = random.nextInt(characters.length());
                    stringBuilder.append(characters.charAt(randomIndex));
                }
                res = stringBuilder.toString();
                break;

            default:
                throw new IllegalArgumentException("Unexcepted error");
        }
        return res;
    }

    public static String getTypeFormatName(Type type)  {
        switch (type) {
            case DECIMAL:
                return "decimal";
            case FLOAT:
                return "float";
            case BOOLEAN:
                return "boolean";
            case STRING:
                return "string";
            default:
                return null;
        }
    }

}
