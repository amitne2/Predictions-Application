package termination;

import dto.TerminationDTO;
import org.omg.CORBA.INTERNAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Termination {
    private  boolean isTicksExist;
    private boolean isSecondsExist;
    private int ticksCount;
    private int secondsCount;

    private boolean isTheUserCanStop;
    private boolean isStopped;

    public Termination(int ticks, int seconds, boolean isTicksExist, boolean isSecondsExist) {
        this.ticksCount = ticks;
        this.secondsCount = seconds;
        this.isTicksExist = isTicksExist;
        this.isSecondsExist= isSecondsExist;
    }

    public Termination(boolean isTheUserCanStop) {
        this.isTicksExist = false;
        this.isSecondsExist = false;
        this.isTheUserCanStop = true;
        this.isStopped = false;
    }

    public  Termination(Termination termination) {
        this.isStopped = termination.isStopped;
        this.isSecondsExist= termination.isSecondsExist;
        this.isTicksExist = termination.isTicksExist;
        this.secondsCount = termination.secondsCount;
        this.ticksCount = termination.ticksCount;
        this.isTheUserCanStop = termination.isTheUserCanStop;
    }

    public boolean isTicksExist() {
        return isTicksExist;
    }

    public boolean isSecondsExist() {
        return isSecondsExist;
    }

    public int getSecondsCount() {
        return secondsCount;
    }

    public int getTicksCount() {
        return ticksCount;
    }

    public Map<String, Integer> getStopConditions(){
        Map<String, Integer>  stopConditions = new HashMap<>();
        stopConditions.put("Ticks", ticksCount);
        stopConditions.put("Seconds", secondsCount);
        return  stopConditions;

    }

    public TerminationDTO creTerminationDTO(){
        TerminationDTO terminationDTO= new TerminationDTO(ticksCount, secondsCount, isTicksExist, isSecondsExist, isTheUserCanStop);
        return terminationDTO;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean isTheUserCanStop() {
        return isTheUserCanStop;
    }

    public void setUserStopped() {
        this.isStopped = true;
    }
}
