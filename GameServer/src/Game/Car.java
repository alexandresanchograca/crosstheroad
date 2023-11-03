package Game;

public class Car {

    private MovementDir moveDir;
    private final int[] savedGridPos;
    private int carSpeed;
    private boolean isDrawn;
    private int[] carIsoPosition;

    public Car(int startCol, int startRow, MovementDir moveDir, int speed){
        this.carSpeed = speed;
        this.moveDir = moveDir;
        this.savedGridPos = new int[] {startCol, startRow};
        this.carIsoPosition = Helper.toIso(startCol, startRow);
        this.carIsoPosition[1] += getImageOffset();
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
            int[] toTransport = Helper.toIso(0, savedGridPos[1]);
            this.carIsoPosition[0] = toTransport[0];
            this.carIsoPosition[1] = toTransport[1] + getImageOffset();
        }else{
            int[] toTransport = Helper.toIso(Grid.getCols() - 1, savedGridPos[1]);
            this.carIsoPosition[0] = toTransport[0];
            this.carIsoPosition[1] = toTransport[1] + getImageOffset();
        }
    }

    public void move(){
        double[] movement = this.moveDir.equals(MovementDir.RIGHT) ?  Helper.translateMovement(carIsoPosition[0], carIsoPosition[1], MovementDir.RIGHT, carSpeed) : Helper.translateMovement(carIsoPosition[0], carIsoPosition[1], MovementDir.LEFT, carSpeed);
        this.carIsoPosition[0] += (int)movement[0];
        this.carIsoPosition[1] += (int)movement[1];
    }

    private int getImageOffset(){
        return this.moveDir.equals(MovementDir.RIGHT) ? -25 : -15;
    }


    public MovementDir getMoveDir() {
        return moveDir;
    }

    public int[] getCarIsoPosition(){
        return carIsoPosition;
    }
}
