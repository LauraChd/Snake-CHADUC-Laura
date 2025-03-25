package model;
import java.util.ArrayList;
import strategie.Strategie;
import utils.AgentAction;
import utils.FeaturesSnake;
import utils.Position;

public class Snake {

    private FeaturesSnake featuresSnake;
    private Position tete;
    private ArrayList<Position> corps;
    private Strategie strategie; 
    private int itemEffect;

    public Snake(FeaturesSnake s, Position tete, ArrayList<Position> corps, Strategie strat){
        this.featuresSnake = s;
        this.tete = tete;
        this.corps = corps;
        this.strategie = strat;
        this.itemEffect = 0;
    }

    public Position getTete() {
        return tete;
    }

    public ArrayList<Position> getCorps() {
        return corps;
    }

    public void setTete(Position tete) {
        this.tete = tete;
        //MAJ DU FEATURES SNAKE
        ArrayList<Position> listPos = this.featuresSnake.getPositions();
        ArrayList<Position> newListPos = new ArrayList<>();
        for(int i =0; i<listPos.size()-1;i++){
            if(i==0){
                newListPos.add(tete);
            }
            else{
                newListPos.add(listPos.get(i));
            }
        }
        this.featuresSnake.setPositions(newListPos);
    }

    public void setCorps(ArrayList<Position> corps) {
        this.corps = corps;
        //MAJ DU FEATURES SNAKE
        ArrayList<Position> listPos = this.featuresSnake.getPositions();
        ArrayList<Position> newListPos = new ArrayList<>();
        for(int i =0; i<listPos.size()-1;i++){
            if(i==0){
                newListPos.add(listPos.get(i));
            }
            else{
                newListPos.add(corps.get(i));
            }
        }
        this.featuresSnake.setPositions(newListPos);
    }

/**
 * 
 * @param action direction choisie pour le prochain mouvement du snake
 * @return true si la direction choisie est en accord avec le snake et sa position
 */
    public boolean isLegalMove(AgentAction action){
        if(this.getFeaturesSnake().getPositions().size() == 1){
            return true; //si il n'y a qu'une tête, pas de problème de direction
        }
        else{
            Position temp;
            switch(action){
                case MOVE_DOWN : 
                    temp = new Position(this.getTete().getX(), this.getTete().getY()+1);
                    for(Position p : this.getCorps()){
                        if(p.getX() == temp.getX() && p.getY() == temp.getY()){System.out.println("MOVE_DOWN interdit"); return false;}
                    }
                    break;

                case MOVE_LEFT : 
                    temp = new Position(this.getTete().getX()-1, this.getTete().getY());
                    for(Position p : this.getCorps()){
                        if(p.getX() == temp.getX() && p.getY() == temp.getY()){System.out.println("MOVE_LEFT interdit"); return false;}
                    }
                    break;

                case MOVE_RIGHT : 
                    temp = new Position(this.getTete().getX()+1, this.getTete().getY());
                    for(Position p : this.getCorps()){
                        if(p.getX() == temp.getX() && p.getY() == temp.getY()){System.out.println("MOVE_RIGHT interdit"); return false;}
                    }
                    break;

                case MOVE_UP : 
                    temp = new Position(this.getTete().getX(), this.getTete().getY()+1);
                    for(Position p : this.getCorps()){
                        if(p.getX() == temp.getX() && p.getY() == temp.getY()){System.out.println("MOVE_UP interdit"); return false;}
                    }
                    break;

                default : 
                    return false;
            }
        }
        return true;
    }

    public FeaturesSnake getFeaturesSnake() {
        return featuresSnake;
    }

    public void setFeaturesSnake(FeaturesSnake featuresSnake) {
        this.featuresSnake = featuresSnake;
    }

    @Override
    public String toString(){
        String res = "Snake " + featuresSnake.getColorSnake().toString();
        return res;
    }

    public Strategie getStrategie() {
        return strategie;
    }

    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }

    public int getItemEffect() {
        return itemEffect;
    }

    public void setItemEffect(int itemEffect) {
        this.itemEffect = itemEffect;
    }

    public void reinitItemEffect(){
        this.itemEffect = 0;
    }  
}
