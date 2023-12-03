package dto.HistoryDataDTO;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class CountEntitiesDTO extends HistoryDataDTO {
    private Map<String, Pair<Integer, Integer>> entitiesHistoryCount;

    public CountEntitiesDTO() {
        entitiesHistoryCount = new HashMap<>();
    }
    public void putEntityData(String name, int before, int after) {
        entitiesHistoryCount.put(name, new Pair<>(before, after));
    }

    public Pair<Integer, Integer> getDetailOnEntity(String name) {
        return entitiesHistoryCount.get(name);
    }

    public Map<String, Pair<Integer, Integer>> getEntitiesHistoryCount() {
        return entitiesHistoryCount;
    }

    @Override
    public Map<Object, Integer> getHistogramDetailOnPropery(int entityIndex,int index) {
        return null;
    }
}
