package strategie;

import utils.AgentAction;

public class HumanControl implements Strategie {
    private AgentAction currentAction;

    public HumanControl() {
        this.currentAction = AgentAction.MOVE_DOWN; 
    }

    public void setAction(AgentAction action) {
        this.currentAction = action;
    }

    @Override
    public AgentAction getAction() {
        return currentAction;
    }
}