package dto;

public class TerminationDTO {
    private int ticksCount;
    private int secondsCount;
    private boolean isTicksExist;
    private boolean isSecondsExist;
    private boolean isUserCanStop;
    public TerminationDTO(int ticksCount, int secondsCount, boolean isTicksExist, boolean isSecondExist, boolean isTheUserCanStop) {
        this.ticksCount= ticksCount;
        this.secondsCount= secondsCount;
        this.isSecondsExist = isSecondExist;
        this.isTicksExist = isTicksExist;
        this.isUserCanStop= isTheUserCanStop;
    }
    public int getSecondsCount() {
        return secondsCount;
    }

    public int getTicksCount() {
        return ticksCount;
    }

    public boolean isSecondsExist() {
        return isSecondsExist;
    }

    public boolean isTicksExist() {
        return isTicksExist;
    }

    public boolean isUserCanStop() {
        return isUserCanStop;
    }
}
