package Game.Actors.Player;

import Game.Utilities.Util;
import Game.Actors.Cars.Car;
import Game.Actors.Grid.Grid;
import Game.Utilities.MovementDir;
import org.academiadecodigo.simplegraphics.pictures.Picture;

/**
 * Used to create our main player
 */
public class Player {
    protected Picture playerPic;
    private Picture[] picArray;
    private static int playerSpeed = 64;
    private boolean isAlive;
    private Car[] carArray;
    protected Score scoreboard;

    public Player(Car[] carArray) {
        this.carArray = carArray;
        this.scoreboard = new Score(this);
        this.isAlive = true;
        loadPictures();
        showPlayer();
    }

    public Car[] getCarArray() {
        return carArray;
    }

    private void showPlayer() {
        int[] initialPos = Util.toIso(Grid.COLS / 2, Grid.ROWS - 2);

        playerPic = picArray[0];
        playerPic.transport(initialPos[0], initialPos[1]);
        playerPic.draw();
    }

    private void loadPictures(){
        this.picArray = new Picture[]{
                new Picture(0, 0, "resources/Fox_Up.png"),
                new Picture(0, 0, "resources/Fox_Down.png"),
                new Picture(0, 0, "resources/Fox_Up_Left.png"),
                new Picture(0, 0, "resources/Fox_Down_Right.png")
        };
    }

    public void moveUp() {

        picArray[0].transport(playerPic.getX(), playerPic.getY());
        playerPic.delete();
        playerPic = picArray[0];
        playerPic.draw();

        if (Util.gridLimitsUp(playerPic, playerSpeed)) {
            double[] diff = Util.translateMovement(playerPic, MovementDir.UP, playerSpeed);
            playerPic.translate(diff[0], diff[1]);
        }
    }

    public Picture getPlayerPic() {

        if(playerPic == null)
            return picArray[0];

        return playerPic;
    }

    public void moveDown() {

        this.picArray[1].transport(playerPic.getX(), playerPic.getY());
        this.playerPic.delete();
        this.playerPic = picArray[1];
        this.playerPic.draw();

        if (Util.gridLimitsDown(playerPic, playerSpeed)) {
            double[] diff = Util.translateMovement(playerPic, MovementDir.DOWN,playerSpeed);

            playerPic.translate(diff[0], diff[1]);
        }
    }

    public void moveLeft() {

        picArray[2].transport(playerPic.getX(), playerPic.getY());
        playerPic.delete();
        playerPic = picArray[2];
        playerPic.draw();

        if(Util.gridLimitsLeft(playerPic, playerSpeed)) // Left limit
        {
            double[] diff = Util.translateMovement(playerPic, MovementDir.LEFT, playerSpeed);
            playerPic.translate(diff[0], diff[1]);
        }
    }

    public void moveRight() {

        picArray[3].transport(playerPic.getX(), playerPic.getY());
        playerPic.delete();
        playerPic = picArray[3];
        playerPic.draw();

        if(Util.gridLimitsRight(playerPic, playerSpeed)){
            double[] diff = Util.translateMovement(playerPic, MovementDir.RIGHT, playerSpeed);
            playerPic.translate(diff[0], diff[1]);
        }
    }

    public void resetPlayerPosition(){
        int[] initialPos = Util.toIso(Grid.COLS / 2, Grid.ROWS - 2);
        playerPic.transport(initialPos[0], initialPos[1]);
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void resetScore(){
        this.scoreboard.resetScore();
    }

    public void updateScore(){
        this.scoreboard.updateScore();
    }

    public int getScore(){
        return this.scoreboard.getScore();
    }
}
