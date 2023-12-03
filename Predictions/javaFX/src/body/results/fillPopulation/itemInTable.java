package body.results.fillPopulation;

import javafx.beans.property.SimpleIntegerProperty;

public class itemInTable {
    private String entityName;
    private SimpleIntegerProperty population;

    public itemInTable(String entityName) {
        this.entityName=entityName;
        this.population = new SimpleIntegerProperty();
    }

    public String getEntityName() {
        return entityName;
    }

    public int getPopulation(){
        return population.get();
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public SimpleIntegerProperty poIntegerProperty(){
        return population;
    }
}
