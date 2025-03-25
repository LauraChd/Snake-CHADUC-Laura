package model;

import strategie.Strategie;
import utils.FeaturesSnake;

public interface AgentFactory {

    public abstract Snake creerAgent(FeaturesSnake s, Strategie strat);

}
