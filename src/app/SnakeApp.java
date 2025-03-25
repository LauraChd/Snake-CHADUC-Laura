package app;
import controller.ControllerSnakeGame;

public class SnakeApp {

    public static void main(String[] args) {
        
        //CREATION DU CONTROLEUR
        ControllerSnakeGame controller = new ControllerSnakeGame("layouts/smallArenaNoWall.lay", 0.5, 100, true);
    }
}
