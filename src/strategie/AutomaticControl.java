package strategie;

import java.util.Random;

import utils.AgentAction;

public class AutomaticControl implements Strategie{
    private AgentAction currentAction;

    public AutomaticControl(){
        this.currentAction = AgentAction.MOVE_DOWN;
    }

    public void setAction(AgentAction action){
        this.currentAction = action;
    }

    @Override
    public AgentAction getAction() {
        Random random = new Random();
        switch(random.nextInt(4)+1){
            case 1 : 
                return AgentAction.MOVE_LEFT;
            case 2 : 
                return AgentAction.MOVE_RIGHT;
            case 3 : 
                return AgentAction.MOVE_UP;
            case 4 : 
            default : 
                return AgentAction.MOVE_DOWN;
        }
    }
}
