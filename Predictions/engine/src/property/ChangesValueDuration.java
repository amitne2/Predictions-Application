package property;

public class ChangesValueDuration {
    private long startTick;
    private long endTick;
    private Object valueDuringThisTime;

    public ChangesValueDuration(long startTick, Object value) {
        this.startTick= startTick;
        this.endTick = startTick;
        this.valueDuringThisTime = value;
    }

    public long getEndTick() {
        return endTick;
    }

    public long getStartTick() {
        return startTick;
    }

    public void setEndTick(long endTick) {
        this.endTick= endTick;
    }

}
