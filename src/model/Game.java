package model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import states.State;

/*
 * PATRON DE METHODE
 * Méthode principale du jeu
 * PATTERN OBSERVATEUR
 * Observable  
 */
public abstract class Game extends Observable implements Runnable {

    private int turn;
    private int maxTurn; //on considérera que le turn = maxTurn sera le dernier joué
    private boolean isRunning; //savoir si le jeu a été mis en pause
    private Thread thread;
    private long time;
    private ArrayList<Observer> listObservers;
    private State state;

    
    public int getTurn() {
        return turn;
    }

    public int getMaxTurn() {
        return maxTurn;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public long getTime() {
        return time;
    }

    public Game(int maxTurn){
        this.maxTurn = maxTurn;
        this.time = 1000;
    }

    public void init(){
        this.turn = 0;
        this.isRunning = true;
        initializeGame();
        this.time = 1000;
        this.setChanged();
        notifyObservers();
    }

    public abstract void initializeGame(); //implémentée par les enfants

    public void step(){
        this.turn++;
        if(gameContinue() && (this.turn<=this.maxTurn)){
            takeTurn();
        } 
        else{
            this.isRunning = false;
            gameOver();
        }
        this.setChanged();
        notifyObservers();
    }

    public abstract void takeTurn();//implémentée par les enfants


    public abstract boolean gameContinue();

    /**
     * affichera un message de fin de jeu
     */
    public abstract void gameOver(); //implémentée par les enfants

    public void pause(){
        this.isRunning = false;
    }

    public abstract void run();

    public void launch(){
        this.isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void setTime(long time) {
        this.time = time;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    
    
}
