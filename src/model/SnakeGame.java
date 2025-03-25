package model;

import java.util.ArrayList;
import java.util.Random;

import strategie.AutomaticControl;
import strategie.HumanControl;
import strategie.Strategie;
import utils.AgentAction;
import utils.FeaturesItem;
import utils.FeaturesSnake;
import utils.ItemType;
import utils.Position;
import view.PanelSnakeGame;

public class SnakeGame extends Game{

    private InputMap map;
    private ArrayList<Snake> snakeList;
    private ArrayList<FeaturesSnake> featuresSnakesList;
    private ArrayList<FeaturesItem> itemsList;
    private AgentAction currentAgentAction;
    private long time;
    private int currentSnake;
    private String filename;
    private double proba;
    private Snake humanControlSnake;
    private boolean mode;

    public SnakeGame(int maxturn, InputMap map, PanelSnakeGame panel, String filename, double proba, boolean mode) {
        super(maxturn);
        this.map = map;
        this.currentAgentAction = AgentAction.MOVE_DOWN; //mouvement choisi par défaut si aucune autre direction n'est indiquée
        this.currentSnake = 0;
        this.snakeList = new ArrayList<>();
        this.featuresSnakesList = new ArrayList<>();
        this.itemsList = new ArrayList<>();
        this.filename = filename;
        this.time = 1000;
        this.proba = proba;
        this.mode = mode;
        initializeGame();
        setChanged();
        notifyObservers();
    }


    @Override
    public void init(){
        super.init();
        initializeGame();
        setChanged();
        notifyObservers();
    }

    @Override
    public void step(){
        super.step();
        setChanged();
        notifyObservers();
    }

    @Override
    public void pause(){
        super.pause();
        setChanged();
        notifyObservers();
    }

    @Override
    public void run(){
        while(gameContinue()){
            if(this.isRunning()){
                step();
                try {
                    Thread.sleep(this.getTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public void initializeGame() {
        //Vide la liste des snakes
        this.snakeList.clear();
        //Refresh la carte
        try {
            this.map = new InputMap(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Créé les snakes
        SnakeFactory snakeFactory = new SnakeFactory();
        Strategie strat = null;
        if(mode){
            strat = new HumanControl();
        }
        else strat = new AutomaticControl();
        for(FeaturesSnake s : map.getStart_snakes()){
            this.snakeList.add(snakeFactory.creerAgent(s, strat));
            strat = new AutomaticControl();
        }
        this.humanControlSnake = snakeList.get(0);
        //Récupère les items
        this.itemsList = map.getStart_items();
        setChanged();
        notifyObservers();
    }

    @Override
    public void takeTurn() {
        if(gameContinue()){
            ArrayList<Snake> deadSnakes = new ArrayList<>();
            //On bouge le snake et on l'ajoute à la liste des snakes mort s'il meurt
            ArrayList<Snake> updatedSnakes = new ArrayList<>();
            ArrayList<FeaturesSnake> updatedFeaturesSnakes =  new ArrayList<>();
            for(Snake s : snakeList){
                if(moveAgent(s, s.getStrategie().getAction())) deadSnakes.add(s);
                updatedSnakes.add(s);
                updatedFeaturesSnakes.add(s.getFeaturesSnake());
            }
            setSnakeList(updatedSnakes);
            setFeaturesSnakesList(updatedFeaturesSnakes);
            //On supprime tous les snakes morts lors de ce tour
            for(Snake deadS : deadSnakes){
                snakeList.remove(deadS);
            }

        }
        else{
            gameOver();
        }
        setChanged();
        notifyObservers();
    }

    public void setCurrentSnake(int currentSnake) {
        this.currentSnake = currentSnake;
    }

    public InputMap getMap() {
        return map;
    }

    public void setMap(InputMap map) {
        this.map = map;
    }

    public ArrayList<Snake> getSnakeList() {
        return snakeList;
    }

    public void setSnakeList(ArrayList<Snake> snakeList) {
        this.snakeList = snakeList;
    }

    public ArrayList<FeaturesItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(ArrayList<FeaturesItem> itemsList) {
        this.itemsList = itemsList;
    }

    public int getCurrentSnake() {
        return currentSnake;
    }

    @Override
    public boolean gameContinue() {
        //Le jeu s'arrête si il ne reste plus que 1 snake pour une partie duo et 0 snake pour une partie solo
        //ou si le nombre de tour max est dépassé
        return snakeList.size()>0 && this.getTurn() <= this.getMaxTurn();
    }

    @Override
    public void gameOver() {
        System.out.println("----Fin du jeu----");
        setChanged();
        notifyObservers();

    }

    public void restart() {
        super.init();
        setChanged();
        notifyObservers();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 
     * @param snake le snake qui doit bouger
     * @param action la direction seléctionnée
     * @return true si le snake est mort suite à son déplacement
     */
    public boolean moveAgent(Snake snake, AgentAction action){
        boolean deadSnake = false;
        //Positions actuelles
        Position lastHead = snake.getTete();
        ArrayList<Position> lastBody = snake.getCorps();
        //Positions après déplacement
        Position newTete = null;
        ArrayList<Position> newCorps = new ArrayList<Position>();
        //Si la direction choisie n'est pas admissible, on continue dans la même direction que le dernier mouvement
        if(!snake.isLegalMove(action)) action = snake.getFeaturesSnake().getLastAction();
        //MOUVEMENT
        //Donne la position de la tête après le déplacement selon l'action choisie
        newTete = getPosAfterMoove(action, lastHead);
        snake.setTete(newTete);

        //Donne la position du corps après le déplacement selon l'action choisie
        if(snake.getCorps().size()>0){
            newCorps.add(lastHead);
            for(int i = 0; i<snake.getCorps().size()-1;i++){
                newCorps.add(snake.getCorps().get(i));
            }
            snake.setCorps(newCorps);
        }
        //VERIFICATIONS
        //Check if the snake die
        deadSnake = snakeDeath(snake);
        //EFFETS
        //Continue si le snake est toujours vivant
        if(!deadSnake){
            snakeMeal(snake, lastHead, lastBody);
        }

        snake.getFeaturesSnake().setLastAction(action);
        return deadSnake;
    }

    /**
     * 
     * @param action la direction selectionnée
     * @param lastHead la position de la tete avant le déplacement
     * @return la position de la tete après le déplacement dans la direction action
     */
    public Position getPosAfterMoove(AgentAction action, Position lastHead){
        //CHANGE TETE POSITION
        Position newHead = null;
        switch (action) {
            case MOVE_DOWN :
                newHead = new Position(lastHead.getX(), lastHead.getY()+1);
                if(newHead.getY()>=map.getSizeY()) newHead = new Position(lastHead.getX(), 0); 
                break;
        
            case MOVE_LEFT :
                newHead = new Position(lastHead.getX()-1, lastHead.getY());
                if(newHead.getX()<0) newHead = new Position(map.getSizeX()-1, lastHead.getY());
                break;
            
            case MOVE_RIGHT : 
                newHead = new Position(lastHead.getX()+1, lastHead.getY());
                if(newHead.getX()>=map.getSizeX()) newHead = new Position(0, lastHead.getY());
                break;

            case MOVE_UP : 
                newHead = new Position(lastHead.getX(), lastHead.getY()-1);
                if(newHead.getY()<0) newHead = new Position(lastHead.getX(), map.getSizeY()-1);
                break;
            
            default : 
                newHead = null;
        }
        return newHead;
    }

    /**
     * 
     * @param snake le snake courant
     * @return true si le snake est mort
     */
    public boolean snakeDeath(Snake snake){
        if(checkWall(snake) && !snake.getFeaturesSnake().isInvincible()){
            snake.setTete(null);
            System.out.println("Le " + snake.toString() + " est mort");
            return true;
        }
        else{
            switch (checkSnake(snake)) {
                case 1:
                    snake.setTete(null);
                    System.out.println("Le " + snake.toString() + " est mort");
                    return true;
                case 2: 
                    snake.setTete(null);
                    System.out.println("Le " + snake.toString() + " est mort");
                    return true;
                case 3 :
                    killTheAnotherSnake(snake);
                case -1 :
                default:
                    break;
            }
        }
        return false;
    }

    /**
     * 
     * @param snake le snake courant
     */
    public void killTheAnotherSnake(Snake snake){
        for(Snake s : snakeList){
            if(!s.getFeaturesSnake().equals(snake.getFeaturesSnake())){
                s.setTete(null);
                System.out.println("Le " + snake.toString() + " a été mangé");
            }
        }
    }

    /**
     * 
     * @param snake le snake courant
     * @param head 
     * @param body
     */
    public void snakeMeal(Snake snake, Position head, ArrayList<Position> body){
        int itemPos = checkItem(snake);
        if(itemPos!=-1 && !snake.getFeaturesSnake().isSick()){
            Position addBody = null;
            if(snake.getCorps().size()<=0) addBody = head;
            else addBody = body.get(body.size()-1);
            doItemStuff(snake, itemPos, addBody);
        }
        popNewItem(0.3); //proba qu'un nouvel item apparaisse à chaque tour
        if(itemsList.size()>3) depopItem();
        updateItemEffect(snake);
    }

    public void updateItemEffect(Snake snake){
        if(snake.getItemEffect() == 20){
            snake.reinitItemEffect();
            if(snake.getFeaturesSnake().isInvincible()) snake.getFeaturesSnake().setInvincible(false);
            if(snake.getFeaturesSnake().isSick()) snake.getFeaturesSnake().setSick(false);
        }
        if(snake.getItemEffect()>0 && snake.getItemEffect()<20){
            snake.setItemEffect(snake.getItemEffect() +1);
        }
    }
    
    /**
     * 
     * @param snake snake courant
     * @return true si il y a un mur à la position de la tete du snake
     */
    public boolean checkWall(Snake snake){
        int xPos = snake.getTete().getX();
        int yPos = snake.getTete().getY();
        if(map.get_walls()[xPos][yPos]){
            return true;
        }
        return false;
    }
    /**
     * 
     * @param snake snake courant
     * @return la position de l'item à la position de la tête du snake dans la liste d'item, -1 sinon
     */
    public int checkItem(Snake snake){
        int xPos = snake.getTete().getX();
        int yPos = snake.getTete().getY();
        int cpt=0;
        for(FeaturesItem i : itemsList){
            if(i.getX() == xPos && i.getY() == yPos){
                return cpt;
            }
            cpt++;
        }
        return -1;
    }

    /**
     * 
     * @param snake snake courant
     * @return si il y a un snake à la position de la tete du snake courant
     */
    public boolean checkBody(Snake snake){
        int xPos = snake.getTete().getX();
        int yPos = snake.getTete().getY();
        for(Position p : snake.getCorps()){
            if(p.getX()==xPos && p.getY()==yPos){
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param snake
     * @return -1 si pas de collision, 1 si le snake courant meurt, 2 si les deux snakes meurent et 3 si le snake ennemi meurt
     */
    public int checkSnake(Snake snake){
        try {
            int xPos = snake.getTete().getX();
        int yPos = snake.getTete().getY();
        for(Snake s : snakeList){
            if(xPos == s.getTete().getX() && yPos == s.getTete().getY() && !s.getFeaturesSnake().equals(snake.getFeaturesSnake())){
                if(snake.getCorps().size() > s.getCorps().size() && !s.getFeaturesSnake().isInvincible()){
                    return 3;
                }
                else if(snake.getCorps().size() == s.getCorps().size()){
                    if(!s.getFeaturesSnake().isInvincible()){
                        if(!snake.getFeaturesSnake().isInvincible()){
                            return 2;
                        }
                        else return 3;
                    }
                    else{
                        if(!snake.getFeaturesSnake().isInvincible()){
                            return 1;
                        }
                        else return -1;
                    }
                }
                else return 1;
            }
            for(Position p : s.getCorps()){
                if(xPos == p.getX() && yPos == p.getY()){
                    if(s.getFeaturesSnake().equals(snake.getFeaturesSnake())){
                        return 1;
                    }
                    else if(snake.getCorps().size() > s.getCorps().size()){
                        return 3;
                    }
                    else if(snake.getCorps().size() == s.getCorps().size()){
                        return 2;
                    }
                    else return 1;
                }
            }
        }
        return -1;
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 
     * @param proba probabilité d'apparition d'un item lorsqu'un item est mangé
     */
    public void popNewItem(double proba){
        Random random = new Random();
        double prob = random.nextDouble();
        boolean alreadyItem = false;
        try{
            if (prob < proba) {
                int type = random.nextInt(4) + 1;
                    int xPos = random.nextInt(map.getSizeX()-1);
                    int yPos = random.nextInt(map.getSizeY()-1);
                //Verif bonne position de l'item
                for(FeaturesItem i : itemsList) { //pas 2 items l'un sur l'autre
                    if(i.getX() == xPos && i.getY() == yPos) alreadyItem = true;
                }
                if(map.get_walls()[xPos][yPos]) alreadyItem = true; //pas d'item sur les murs
                for(Snake s : snakeList){ //pas d'item sur une position avec un snake
                    if(s.getTete() != null && s.getTete().getX() == xPos && s.getTete().getY() == yPos) alreadyItem = true;
                    for(Position p : s.getCorps()){
                        if(p.getX() == xPos && p.getY() == yPos) alreadyItem = true;
                    }
                }
                if(!alreadyItem){
                    FeaturesItem newItem = null;
                    switch (type) {
                        case 1:
                            newItem = new FeaturesItem(xPos, yPos, ItemType.APPLE);
                            break;
                        case 2 :
                            newItem = new FeaturesItem(xPos, yPos, ItemType.BOX);
                            break;
                        case 3 :
                            newItem = new FeaturesItem(xPos, yPos, ItemType.SICK_BALL);
                            break;
                        case 4 :
                            newItem = new FeaturesItem(xPos, yPos, ItemType.INVINCIBILITY_BALL);
                            break;
                        default:
                            break;
                    }
                    itemsList.add(newItem);
                }
            }
        }
        catch (Exception e) {}
    }

    /**
     * enlève un item de la liste
     */
    public void depopItem(){
        Random random = new Random();
        itemsList.remove(random.nextInt(itemsList.size()-1));
    }

    /**
     * 
     * @param snake snake courant
     * @param itemPos position de l'item mangé
     * @param body positions du corps
     */
    public void doItemStuff(Snake snake, int itemPos, Position body){
        FeaturesItem item = itemsList.get(itemPos);
        ItemType itemType = item.getItemType();
        switch(itemType){
            case APPLE:
                eatApple(snake, body);
                break;
            case BOX:
                Random random = new Random();
                switch(random.nextInt(2)+1){
                    case 1 : 
                        eatInvincibleBall(snake);
                        break;
                    case 2 : 
                        eatSickBall(snake);
                        break;
                }
                break;
            case INVINCIBILITY_BALL:
                eatInvincibleBall(snake);
                break;
            case SICK_BALL:
                eatSickBall(snake);
                break;
            default:
                break;
            
        }
        itemsList.remove(itemPos);
        popNewItem(this.proba);
    }

    /**
     * rend le snake malade
     * @param snake
     */
    public void eatSickBall(Snake snake){
        if(snake.getItemEffect() == 0){
            snake.getFeaturesSnake().setSick(true);
            snake.setItemEffect(1);
        }
    }

    /**
     * rend le snake invincible
     * @param snake
     */
    public void eatInvincibleBall(Snake snake){
        if(snake.getItemEffect() == 0){
            snake.getFeaturesSnake().setInvincible(true);
            snake.setItemEffect(1);
        }
        
    }

    /**
     * 
     * @param snake
     * @param part
     */
    public void eatApple(Snake snake, Position part){
        snake.getCorps().add(part);
    }

    public AgentAction getCurrentAgentAction() {
        return currentAgentAction;
    }

    public void setCurrentAgentAction(AgentAction currentAgentAction) {
        this.currentAgentAction = currentAgentAction;
    }

    public ArrayList<FeaturesSnake> getFeaturesSnakesList() {
        return featuresSnakesList;
    }

    public void setFeaturesSnakesList(ArrayList<FeaturesSnake> featuresSnakesList) {
        this.featuresSnakesList = featuresSnakesList;
    }


    public Snake getHumanControlSnake() {
        return humanControlSnake;
    }


    public void setHumanControlSnake(Snake humanControlSnake) {
        this.humanControlSnake = humanControlSnake;
    }
}
