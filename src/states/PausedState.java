package states;
import view.ViewCommand;

public class PausedState extends State{

    public PausedState(ViewCommand viewCommand) {
        super(viewCommand);
    }

    @Override
    public void runningStateActivation() {
        System.out.println("Jeu redémarré");
        viewCommand.setState(new RunningState(viewCommand));
    }

    @Override
    public void readyStateActivation() {
        System.out.println("Jeu réinitalisé");
        viewCommand.setState(new ReadyState(viewCommand));
    }

    @Override
    public void pausedStateActivation() {
        System.out.println("Impossible de mettre le jeu en pause : jeu déjà en pause");
    }

    @Override
    public String getState(){
        return "PausedState";
    }

}
