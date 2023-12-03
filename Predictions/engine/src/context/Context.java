package context;

import EntityInstanceManager.EntityInstanceManager;
import EntityInstanceManager.EntityInstanceManagerImpl;
import entity.instance.EntityInstanceImpl;
import environment.EnvVariableManagerImpl;
import grid.GridInstances;
import property.EntityProperty;
import world.World;

import java.util.List;

public class Context {
    private EntityInstanceImpl primaryEntityInstance;
    private EntityInstanceImpl secondaryEntityInstance;
    private EnvVariableManagerImpl  envVariableManager;
    private EntityInstanceManagerImpl entityInstanceManager;
    private long currTick;

    private World world;

    private GridInstances gridInstances;


    //without secandary
    public Context(EntityInstanceImpl primaryEntityInstance, EnvVariableManagerImpl envVariableManager,EntityInstanceManagerImpl entityInstanceManager,
                   long currTick, World world, GridInstances gridInstances)
    {
    this.primaryEntityInstance = primaryEntityInstance;
    this.envVariableManager = envVariableManager;
    this.entityInstanceManager = entityInstanceManager;
    this.currTick = currTick;
    this.world = world;
    this.gridInstances = gridInstances;
    }

    // with secandary
    public Context(EntityInstanceImpl primaryEntityInstance,EntityInstanceImpl secondaryEntityInstance, EnvVariableManagerImpl envVariableManager,EntityInstanceManagerImpl entityInstanceManager,
                   long currTick, World world, GridInstances gridInstances) {
        this.primaryEntityInstance = primaryEntityInstance;
        this.secondaryEntityInstance = secondaryEntityInstance;
        this.envVariableManager = envVariableManager;
        this.entityInstanceManager = entityInstanceManager;
        this.currTick = currTick;
        this.world = world;
        this.gridInstances = gridInstances;
    }

    public EntityInstanceImpl getPrimaryEntityInstance() {
        return primaryEntityInstance;
    }

    public EntityProperty getEnvironmentVariable(String name) {
        return envVariableManager.getProperty(name);
    }

    public long getCurrTick() {
        return currTick;
    }

    public EntityInstanceManagerImpl getEntityInstanceManager() {
        return entityInstanceManager;
    }

    public EntityInstanceImpl getSecondaryEntityInstance() {
        return secondaryEntityInstance;
    }

    public void setSecondaryEntityInstance(EntityInstanceImpl entityInstance) {
        this.secondaryEntityInstance = entityInstance;
    }

    public EnvVariableManagerImpl getEnvVariableManager() {
        return envVariableManager;
    }

    public GridInstances getGridInstances() {
        return gridInstances;
    }

    public World getWorld(){ return world; }
}
