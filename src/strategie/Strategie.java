package strategie;

import utils.AgentAction;

public interface Strategie {
    public abstract AgentAction getAction();
    public abstract void setAction(AgentAction action);

}
