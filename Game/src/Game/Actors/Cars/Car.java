package Game.Actors.Cars;

import Game.Actors.Player.Player;
import Game.Utilities.Util;
import Game.Utilities.MovementDir;
import org.academiadecodigo.simplegraphics.pictures.Picture;

/** Used to create a car */
public class Car {

    private Picture carPic;
    private MovementDir moveDir;
    private final int[] savedGridPos;
    private int carSpeed;
    private final String carImageName;
    private boolean isDrawn;

    public Car(int startCol, int startRow, MovementDir moveDir, String carImage, int speed){
        this.carSpeed = speed;
        this.moveDir = moveDir;
        this.savedGridPos = new int[] {startCol, startRow};
        this.carImageName = carImage;
        this.carPic = Util.createPicture(startCol, startRow, 0, getImageOffset(), carImage);
        this.isDrawn = false;
    }

    public int getCarSpeed() {
        return carSpeed;
    }

    public boolean isDrawn() {
        return isDrawn;
    }

    public void setDrawn(boolean drawn) {
        isDrawn = drawn;
    }

    public void reset(){
        if(moveDir.equals(MovementDir.RIGHT)) {
            int[] toTransport = Util.toIso(0, savedGridPos[1]);
            this.carPic.transport(toTransport[0], toTransport[1] + getImageOffset());
        }else{
            int[] toTransport = Util.toIso(24, savedGridPos[1]);
            this.carPic.transport(toTransport[0], toTransport[1] + getImageOffset());
        }
    }

    public boolean checkCollision(Player player) {
        double carWidth = carPic.getWidth();
        double carHeight = carPic.getHeight();
        double playerWidth = player.getPlayerPic().getWidth();
        double playerHeight = player.getPlayerPic().getHeight();
        double playerMiddleX = player.getPlayerPic().getX() + playerWidth / 2;
        double playerMiddleY = player.getPlayerPic().getY() + playerHeight / 1.25;

        double finalCarX = (carPic.getX() + carWidth/2) - 15;
        double finalCarY = (carPic.getY() + carHeight / 2) - 15;
        double finalWidthX = finalCarX + carWidth/2;
        double finalHeightY = finalCarY + carHeight /2;

        //Alternative collision
        //return playerMiddleX > finalCarX &&  playerMiddleY > finalCarY && playerMiddleX < finalWidthX && playerMiddleY < finalHeightY;

        int[] gridPos = Util.toGrid(carPic.getX() + carWidth / 2, carPic.getY() + carHeight / 2);
        int[] gridPosPlayer = Util.toGrid(playerMiddleX, playerMiddleY);

        return gridPos[0] == gridPosPlayer[0] && gridPos[1] == gridPosPlayer[1];
    }


    public void showCar(){
        this.carPic.draw();
    }

    private String getCarImage(){
        return this.moveDir.equals(MovementDir.RIGHT) ? "resources/Roadster_64R.png" : "resources/Roadster_64L.png";
    }

    private int getImageOffset(){
        return this.moveDir.equals(MovementDir.RIGHT) ? -25 : -15;
    }

    public void move(){
        double[] movement = this.moveDir.equals(MovementDir.RIGHT) ?  Util.translateMovement(this.carPic, MovementDir.RIGHT, carSpeed) : Util.translateMovement(this.carPic, MovementDir.LEFT, carSpeed);
        this.carPic.translate(movement[0], movement[1]);
    }

    public Picture getCarPic() {
        return carPic;
    }

    public void deleteCar(){
        this.carPic.delete();
    }

    /* Only returns the X/Horizontal distance*/
    public float getXDistance(Car car){
        return Math.abs(this.carPic.getX() - car.carPic.getX());
    }


    public int getX() {
        return carPic.getX();
    }

    public MovementDir getMoveDir() {
        return moveDir;
    }

    public int getY() {
        return carPic.getY();
    }

    public String getCarImageName() {
        return carImageName;
    }
}
