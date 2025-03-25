package states;

import view.ViewCommand;

public abstract class State {

    protected ViewCommand viewCommand;

    public State(ViewCommand viewCommand){
        this.viewCommand = viewCommand;
    }

    public abstract void runningStateActivation();

    public abstract void readyStateActivation();

    public abstract void pausedStateActivation();

    public abstract String getState();

}
