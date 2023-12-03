package task;

import dto.FinishSimulationDataDTO;
import javafx.scene.layout.VBox;

public class TaskHistogramData implements Runnable{
    private VBox diaplayPropertiesDetailsVbox;
    private  FinishSimulationDataDTO finishSimulationDataDTO;

    public TaskHistogramData(FinishSimulationDataDTO finishSimulationDataDTO, VBox diaplayPropertiesDetailsVbox) {
        this.finishSimulationDataDTO = finishSimulationDataDTO;
        this.diaplayPropertiesDetailsVbox = diaplayPropertiesDetailsVbox;
    }

    @Override
    public void run() {

    }
}
