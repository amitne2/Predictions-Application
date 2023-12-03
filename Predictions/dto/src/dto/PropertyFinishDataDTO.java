package dto;

public class PropertyFinishDataDTO {

    private long consistency;
    private boolean isNumber;
    private Object averageValue;


    public PropertyFinishDataDTO(long numOfTicksWithoutChanges, boolean isNumber, float value) {
        this.consistency = numOfTicksWithoutChanges;
        this.isNumber = isNumber;
        this.averageValue = value;
    }


}
