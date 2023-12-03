package environment.helperFunctions.random;

import java.util.Random;

public class ActiveRandomImpl {

    public static int random(Object value) {
        Random random = new Random();
        if (!(value instanceof Integer)) {
            throw new IllegalArgumentException("arg value in random function is not an integer. must be a integer.");
        } else {
            return random.nextInt((int)value);
        }
    }
}
