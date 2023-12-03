package dto.HistoryDataDTO;

import java.util.Map;

public abstract class HistoryDataDTO {
    private String entityName;

    public abstract Map<Object, Integer> getHistogramDetailOnPropery(int entityIndex, int propertyIndex);

    }
