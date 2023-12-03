package environment.helperFunctions.percent;

public class ActivePercentImpl {

    public static Object getPercent(Object arg1, Object arg2) throws Exception {
        Object res= null;
        if(!(arg1 instanceof Number)) {
            throw new Exception(arg1 + "is not a number, can't play percent function.");
        }
        if(!(arg2 instanceof Number)) {
            throw new Exception(arg2 + "is not a number, can't play percent function.");
        } try {
            res = ((float) arg1 * (float) arg2) / 100;
        } catch (Exception e) {
            throw new Exception("An error was happened in function percent.");
        }
        return res;
    }
}
