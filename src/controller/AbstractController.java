package controller;

import model.Game;

/*
 * Lien entre les vues et le modèle(jeu)
 */
public abstract class AbstractController {

    public abstract void restart();
    public abstract void step();
    public abstract void play();
    public abstract void pause();
    public abstract void setSpeed(long speed);
    public abstract Game getGame();

}
