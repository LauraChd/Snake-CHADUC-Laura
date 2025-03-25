package view;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


import javax.swing.JFrame;

import model.Snake;
import model.SnakeGame;
import utils.AgentAction;
import utils.FeaturesSnake;
import utils.Position;

public class ViewSnakeGame extends JFrame implements Observer, KeyListener{

    private PanelSnakeGame panelSnakeGame;
    private SnakeGame snakeGame;

    public ViewSnakeGame(SnakeGame game, PanelSnakeGame panel){
        this.snakeGame = game;
        this.panelSnakeGame = panel;
        panelSnakeGame.setFocusable(false);
        game.addObserver(this);
        displayPlayingMap();
    }

    @Override
    public void update(Observable o, Object arg) {
        SnakeGame snakeGame = (SnakeGame)o;

        ArrayList<FeaturesSnake> featuresSnakesList = new ArrayList<>();
        
        for(Snake s : snakeGame.getSnakeList()){
            ArrayList<Position> newFeatureSnake = new ArrayList<>();
            newFeatureSnake.add(s.getTete());
            for(Position pos : s.getCorps()){
                newFeatureSnake.add(pos);
            }
            s.getFeaturesSnake().setPositions(newFeatureSnake);
            featuresSnakesList.add(s.getFeaturesSnake());
        }
        snakeGame.setFeaturesSnakesList(featuresSnakesList);
        this.panelSnakeGame.updateInfoGame(featuresSnakesList, snakeGame.getItemsList());
        this.panelSnakeGame.repaint();
        
    }

    private void displayPlayingMap(){
        //AFFICHAGE DE LA CARTE DE JEU
        JFrame frame = new JFrame();
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this.panelSnakeGame);
        frame.setSize(new Dimension(this.panelSnakeGame.getSizeX()*50, this.panelSnakeGame.getSizeY()*50));
        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x- windowSize.width / 2 ;
        int dy = centerPoint.y- windowSize.height / 2+100;
        frame.setLocation(dx, dy);
        frame.setVisible(true);
    }



    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                snakeGame.getHumanControlSnake().getStrategie().setAction(AgentAction.MOVE_UP);
                break;
            case KeyEvent.VK_DOWN:
                snakeGame.getHumanControlSnake().getStrategie().setAction(AgentAction.MOVE_DOWN);
                break;
            case KeyEvent.VK_LEFT:
                snakeGame.getHumanControlSnake().getStrategie().setAction(AgentAction.MOVE_LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snakeGame.getHumanControlSnake().getStrategie().setAction(AgentAction.MOVE_RIGHT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
