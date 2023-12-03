package dto;

import rule.Activation;

public class ActivationDTO {

    private int ticks;
    private double probability; // number between 0 to 1

    public ActivationDTO(int ticks, double probability) {
        this.ticks = ticks;
        this.probability = probability;
    }

    public int getTicks() { return ticks; }

    public double getProbability() { return probability; }


}
