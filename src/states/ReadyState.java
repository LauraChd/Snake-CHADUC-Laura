package states;
import view.ViewCommand;

public class ReadyState extends State {

    public ReadyState(ViewCommand viewCommand) {
        super(viewCommand);
    }

    @Override
    public void runningStateActivation() {
        System.out.println("Jeu démarré");
        viewCommand.setState(new RunningState(viewCommand));
    }

    @Override
    public void readyStateActivation() {
        //System.out.println("Jeu déjà prêt à être lancé");
    }

    @Override
    public void pausedStateActivation() {
        System.out.println("Impossible de mettre le jeu en pause : jeu non démarré");
    }
    @Override
    public String getState(){
        return "ReadyState";
    }

}
