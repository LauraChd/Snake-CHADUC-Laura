package states;
import view.ViewCommand;

public class RunningState extends State {

    public RunningState(ViewCommand viewCommand) {
        super(viewCommand);
    }

    @Override
    public void runningStateActivation() {
        System.out.println("Jeu déjà en cours");
    }

    @Override
    public void readyStateActivation() {
        System.out.println("Jeu réinitialisé");
        viewCommand.setState(new ReadyState(viewCommand));
    }

    @Override
    public void pausedStateActivation() {
        System.out.println("Jeu mis en pause");
        viewCommand.setState(new PausedState(viewCommand));
    }
    @Override
    public String getState(){
        return "RunningState";
    }

}
