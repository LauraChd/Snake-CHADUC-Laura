package controller;

import model.InputMap;
import model.SnakeGame;
import view.PanelSnakeGame;
import view.ViewCommand;
import view.ViewSnakeGame;

public class ControllerSnakeGame extends AbstractController{ 

    private SnakeGame snakeGame;
    private ViewCommand viewCommand;
    private ViewSnakeGame viewSnakeGame;
    private InputMap map;

    public ControllerSnakeGame(String filepath, double proba, int maxTurn, boolean mode){
        //RECUPERATION DE LA CARTE DE JEU 
        try{
            map = new InputMap(filepath);
        }catch (Exception e){
			System.out.println("Erreur : "+e.getMessage());
            map = null;
		}
        //CREATION DU PANEL QUI GERE L'AFFICHAGE DE LA CARTE DE JEU 
        PanelSnakeGame panel = new PanelSnakeGame(map.getSizeX(), map.getSizeY(), map.get_walls(), map.getStart_snakes(), map.getStart_items());
        //CREATION DU JEU
        this.snakeGame = new SnakeGame(maxTurn, map, panel, filepath, proba, mode);
        //CREATION DE LA VUE DES COMMANDES
        this.viewCommand = new ViewCommand(this, snakeGame);
        //CREATION DE LA VUE QUI S'OCCUPE DE LA CARTE DE JEU
        this.viewSnakeGame = new ViewSnakeGame(snakeGame, panel);
    }

    public void launchSnakeGame(){
        this.snakeGame.launch();
    }

    @Override
    public void restart() {
        this.snakeGame.restart();
    }

    @Override
    public void step() {
        this.snakeGame.step();
    }

    @Override
    public void play() {
        this.snakeGame.launch();
    }

    @Override
    public void pause() {
        this.snakeGame.pause();
    }

    @Override
    public void setSpeed(long speed) {
        this.snakeGame.setTime(1000/speed);
    }

    @Override
    public SnakeGame getGame() {
        return snakeGame;
    }

    

}
