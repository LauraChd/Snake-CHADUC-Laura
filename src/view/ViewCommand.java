package view;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.AbstractController;
import model.Game;
import states.PausedState;
import states.ReadyState;
import states.RunningState;
import states.State;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

//VUE -> OBSERVATEUR
public class ViewCommand implements Observer{

    //FRAME
    JFrame jFrame;

    //LABELS
    JLabel sliderLabel;
    JLabel turnsLabel;

    //BOUTONS
    Icon restartIcon = new ImageIcon("icons/icon_restart.png");
    JButton restartButton = new JButton(restartIcon);

    Icon playIcon = new ImageIcon("icons/icon_play.png");
    JButton playButton = new JButton(playIcon);

    Icon pauseIcon = new ImageIcon("icons/icon_pause.png");
    JButton pauseButton = new JButton(pauseIcon);

    Icon stepIcon = new ImageIcon("icons/icon_step.png");
    JButton stepButton = new JButton(stepIcon);

    //PANELS
    JPanel upperPannel;
    JPanel belowPanel;
    JPanel sliderPanel;

    //SLIDER
    JSlider jSlider;
    int jSliderValue;

    AbstractController controller;
    boolean endGame;
    private boolean isUpdatingButton = false;
    State state;

    public ViewCommand(AbstractController controller, Observable game){
    
        this.controller = controller;
        game.addObserver(this);
        this.createUserFrame((Game)game);
        this.jSliderValue = jSlider.getValue();
        this.endGame = false;

        restartButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evenement){
                controller.restart();
                readyStateActivation();
                jFrame.setFocusable(false);
            }
        });

        playButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evenement){
                controller.play();
                runningStateActivation();
                jFrame.setFocusable(false);
            }
        });

        pauseButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evenement){
                controller.pause();
                pausedStateActivation();
                jFrame.setFocusable(false);
            }
        });

        stepButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evenement){
                controller.step();
                jFrame.setFocusable(false);
            }
        });

        jSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evenement){
                JSlider source = (JSlider)evenement.getSource();
                if(!source.getValueIsAdjusting()){
                    long speed = source.getValue();
                    controller.setSpeed(speed);
                    jFrame.setFocusable(false);
                }
            }
        });

        this.state = new ReadyState(this);
        readyStateActivation();
        controller.getGame().notifyObservers();
        jFrame.setFocusable(false);

    }


    @Override
    public void update(Observable obs, Object arg) {

        Game game = (Game)obs;
        turnsLabel.setText("Tour courant : " + game.getTurn());
        this.jSliderValue = this.jSlider.getValue();
        updateButton();
        jFrame.setFocusable(false);
        
    }

    public void setState(State newState){
        this.state = newState;
        jFrame.setFocusable(false);
    }
    
    public void runningStateActivation(){
        state.runningStateActivation();
        updateButton();
        jFrame.setFocusable(false);
    }

    public void readyStateActivation() {
        state.readyStateActivation();
        if (endGame) { 
            resetButtons();
            endGame = false; 
        }
        updateButton();
        jFrame.setFocusable(false);
    }
    

    public void pausedStateActivation(){
        state.pausedStateActivation();
        updateButton();
        jFrame.setFocusable(false);

    }

     private void updateButton() {
        if (isUpdatingButton) return; 
    
        isUpdatingButton = true;
    
        Game game = controller.getGame();
    
        if (!game.gameContinue()) {
            stepButton.setEnabled(false);
            playButton.setEnabled(false);
            restartButton.setEnabled(true);
            pauseButton.setEnabled(false);
            readyStateActivation();
        } else {
            if (this.state.getClass().equals(PausedState.class)) {
                stepButton.setEnabled(true);
                playButton.setEnabled(true);
                restartButton.setEnabled(true);
                pauseButton.setEnabled(false);
            } else if (this.state.getClass().equals(ReadyState.class)) {
                stepButton.setEnabled(true);
                playButton.setEnabled(true);
                restartButton.setEnabled(false);
                pauseButton.setEnabled(false);
            } else if (this.state.getClass().equals(RunningState.class)) {
                stepButton.setEnabled(false);
                playButton.setEnabled(false);
                restartButton.setEnabled(false);
                pauseButton.setEnabled(true);
            }
        }
    
        isUpdatingButton = false;
        jFrame.setFocusable(false);
    }
    

    private void resetButtons() {
        stepButton.setEnabled(false);
        playButton.setEnabled(false);
        restartButton.setEnabled(true);
        pauseButton.setEnabled(false);
        jFrame.setFocusable(false);
    }

    private void createUserFrame(Game game) {

        //FRAME
        jFrame = new JFrame();
        jFrame.setTitle("Commandes");
        jFrame.setSize(new Dimension(700, 300));
        Dimension windowSize = jFrame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x- windowSize.width / 2 ;
        int dy = centerPoint.y- windowSize.height / 2-350;
        jFrame.setLocation(dx, dy);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        jFrame.setLayout(new GridLayout(2,1));
        jFrame.setFocusable(false);

    //UPPERPANEL
        upperPannel = new JPanel(new GridLayout(1, 4));
        upperPannel.setFocusable(false);
        jFrame.add(upperPannel);

        //AJOUT DES BOUTONS
        upperPannel.add(restartButton);
        upperPannel.add(playButton);
        upperPannel.add(stepButton);
        upperPannel.add(pauseButton);

    //BELOWPANEL
        belowPanel = new JPanel(new GridLayout(1,2));
        belowPanel.setFocusable(false);
        jFrame.add(belowPanel);

        sliderPanel = new JPanel(new GridLayout(2,1));
        sliderPanel.setFocusable(false);

        //AJOUT DU SLIDER
        jSlider = new JSlider(1,10,1);
        jSlider.setPaintTrack(true); 
        jSlider.setPaintTicks(true); 
        jSlider.setPaintLabels(true); 

        jSlider.setMajorTickSpacing(1); 
        jSlider.setMinorTickSpacing(1); 

        jSlider.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("LEFT"), "none");
        jSlider.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("RIGHT"), "none");
        jSlider.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "none");
        jSlider.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "none");


        sliderLabel = new JLabel("Number of turns per second", JLabel.CENTER);
        
        sliderPanel.add(sliderLabel);
        sliderPanel.add(jSlider);
        belowPanel.add(sliderPanel, BorderLayout.CENTER);
        
        //AJOUT DU TEXTE
        if(game.gameContinue()) turnsLabel = new JLabel("Tour courant : " + game.getTurn(), JLabel.CENTER);
        else turnsLabel = new JLabel("Fin de la partie", JLabel.CENTER);
        belowPanel.add(turnsLabel);
        jFrame.setFocusable(false);
    }


}

