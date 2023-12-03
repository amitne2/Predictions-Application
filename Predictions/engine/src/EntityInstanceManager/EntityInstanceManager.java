package EntityInstanceManager;

import entity.definition.EntityDefinition;
import entity.instance.EntityInstanceImpl;

import java.util.Collection;

public interface EntityInstanceManager {

    public void killEntity(int id);

    public EntityInstanceImpl create(EntityDefinition entityDefinition, int row, int col);
}
