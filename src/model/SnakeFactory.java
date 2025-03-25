package model;

import java.util.ArrayList;

import strategie.Strategie;
import utils.FeaturesSnake;
import utils.Position;

public class SnakeFactory implements AgentFactory{

    @Override
    public Snake creerAgent(FeaturesSnake s, Strategie strat) {
        ArrayList<Position> corps = new ArrayList<>();
        if(s.getPositions().size()>1){
            for(int i=1; i<=s.getPositions().size(); i++){
                corps.add(s.getPositions().get(i));
            }
        }
        Snake snake = new Snake(s, s.getPositions().get(0), corps, strat);
        return snake;
        
    }


}
