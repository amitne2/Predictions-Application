package rule;

public class Activation {
    private int ticks;
    private double probability; // number between 0 to 1

    public Activation() { //default choice
        ticks = 1;
        probability = 1;
    }

    public Activation(int ticks) { //default for probability
        this.ticks = ticks;
        probability = 1;
    }

    public Activation(double probability) { //default for ticks
        this.probability = probability;
        ticks = 1;
    }

    public Activation(int ticks, double probability) { //enter values for both
        this.ticks = ticks;
        this.probability = probability;
    }

    public int getTicks() { return ticks; }

    public double getProbability() { return probability; }

    @Override
    public String toString() {
        return "Ticks: " + ticks + ", Probability: " + probability;
    }
}
