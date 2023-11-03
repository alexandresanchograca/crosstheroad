package Game;

import Game.Actors.Cars.Car;
import Game.Actors.Grid.Grid;
import Game.Actors.Grid.Hill;
import Game.Actors.Grid.Props;
import Game.Actors.Grid.Road;
import Game.Actors.Player.Controller;
import Game.Actors.Player.Player;
import Game.Factories.CarFactory;
import Game.Factories.RoadFactory;
import Game.Utilities.Difficulty;
import Game.Utilities.MovementDir;
import Game.Utilities.Util;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.io.IOException;
import java.util.*;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Main game logic */
public class Game implements KeyboardHandler {
    private Player player;
    private Controller playerController;
    private List<Car> isoCars;
    private Car[] carArray;
    private LinkedList<Road> roads;
    private Hill hills;
    private Keyboard keyboard;
    private boolean clicked = false;
    private Grid currentGrid;
    private boolean endGame;
    private Props props;

    //private boolean newLevel;
    private Difficulty difficulty;
    public static String playerUser = "P1";

    public Game(String name){
        Menu menu = new Menu(this);
        while (!clicked) {
            try {
                Thread.sleep(25);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        init();

        //Init multiplayer settings
        playerUser = name;

        this.endGame = false;
        this.difficulty = Difficulty.HARD;
        this.currentGrid = new Grid(difficulty);
        this.roads = RoadFactory.createRoads(difficulty);
        this.carArray = CarFactory.createAllCars(difficulty);
        this.hills = new Hill(difficulty);
        //this.props = new Props(difficulty);
        this.isoCars = Collections.synchronizedList(new LinkedList<Car>());
        this.player = new Player(carArray);
        this.playerController = Controller.getInstance();
        this.playerController.setPlayerOwner(player, true);
    }

    public void run(String serverIp, int serverPort) throws IOException {
        //Creates the connection to the server
        ClientConnection clientConnection = new ClientConnection(player, carArray, serverIp, serverPort);

        //Send packet thread
        ExecutorService sendPacketThread = Executors.newSingleThreadExecutor();
        ClientConnection.sendPacket clientSendPacket = clientConnection.new sendPacket();
        sendPacketThread.submit(clientSendPacket);

        //Receive packet thread
        ExecutorService receivePacketThread = Executors.newSingleThreadExecutor();
        ClientConnection.receivePacket clientReceivePacket = clientConnection.new receivePacket();
        receivePacketThread.submit(clientReceivePacket);

        while (!endGame) {

                for(Car c : carArray){ //Checks for collisions
                    if( c.checkCollision(player) || !player.isAlive()) {
                        player.resetPlayerPosition();
                        player.setAlive(true);
                        player.resetScore();
                    }
                }

                for (int i = 0; i < carArray.length; i++) {

                    if (carArray[i].getMoveDir().equals(MovementDir.RIGHT) && !Util.gridLimitsRight(carArray[i].getCarPic()) && carArray[i].isDrawn()) {
                        carArray[i].reset();
                    }
                    else if (carArray[i].getMoveDir().equals(MovementDir.LEFT) && !Util.gridLimitsLeft(carArray[i].getCarPic()) && carArray[i].isDrawn()) {
                        carArray[i].reset();
                    }
                    else if(Util.gridLimitsLeft(carArray[i].getCarPic()) && Util.gridLimitsRight(carArray[i].getCarPic()) && !carArray[i].isDrawn()){
                        carArray[i].setDrawn(true);
                        carArray[i].getCarPic().draw();
                    }
                }

                //Check if player has won this round
                if(  checkPlayerWinCondition()  )  {
                    player.updateScore();
                    player.resetPlayerPosition();
                }

                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        receivePacketThread.shutdown();
        sendPacketThread.shutdown();
    }

    private void init(){
        keyboard = new Keyboard(this);
        KeyboardEvent quitGame = new KeyboardEvent();
        quitGame.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        quitGame.setKey(KeyboardEvent.KEY_SPACE);
        keyboard.addEventListener(quitGame);
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        System.exit(1);
    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    //Level functionalities
    public void deleteLevel() throws Exception{

        //Deletes grid tiles
        Picture[][] tiles =  currentGrid.isoGrid;
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                tiles[i][j].delete();
            }
        }

        //Deletes the roads
        while(!this.roads.isEmpty()){

            Picture[] roadTilesLeft = this.roads.getFirst().getLeftRoadGrid();
            for(int i = 0; i < roadTilesLeft.length; i++){
                roadTilesLeft[i].delete();
            }

            Picture[] roadTilesRight = this.roads.getFirst().getRightRoadGrid();
            for(int i = 0; i < roadTilesRight.length; i++){
                roadTilesRight[i].delete();
            }

            this.roads.removeFirst();
        }

        //Deletes hills
        LinkedList<Picture> hillsList = this.hills.getHills();
        while(!hillsList.isEmpty()){
            hillsList.getFirst().delete();
            hillsList.removeFirst();
        }

        //Deletes Cars
        while(!this.isoCars.isEmpty()){
            isoCars.get(0).deleteCar();
            isoCars.remove(0);
        }

        playerController.setPlayerOwner(null, true);
        player.setAlive(false);
        player.getPlayerPic().delete();
    }


    public void createLevel(){
        this.currentGrid = new Grid(difficulty);
        this.roads = RoadFactory.createRoads(difficulty);
        this.hills = new Hill(difficulty);
        this.isoCars = Collections.synchronizedList(new LinkedList<Car>());
        this.player = new Player(carArray);
        playerController.setPlayerOwner(this.player, true);
    }

    public boolean checkPlayerWinCondition(){
        double playerWidth = player.getPlayerPic().getWidth();
        double playerHeight = player.getPlayerPic().getHeight();
        double playerMiddleX = player.getPlayerPic().getX() + playerWidth / 2;
        double playerMiddleY = player.getPlayerPic().getY() + playerHeight / 1.25;

        int[] gridPosPlayer = Util.toGrid(playerMiddleX, playerMiddleY);

        return gridPosPlayer[1] < 2;
    }

    public void reDrawPlayer(){
            player.getPlayerPic().delete();
            player.getPlayerPic().draw();
    }
}
