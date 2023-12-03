package simulations.simulationExecution;

public class TicksPopulation {
    private long tick;
    private int countEntities;

    public TicksPopulation(long tick, int countEntities) {
        this.tick = tick;
        this.countEntities = countEntities;
    }

    public long getTick() {
        return tick;
    }

    public int getCountEntities() {
        return countEntities;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }
}
