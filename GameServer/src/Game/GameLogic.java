package Game;

public class GameLogic implements Runnable{
    private Difficulty difficulty;
    private boolean endGame;
    private Car[] carArray;

    public GameLogic(){
        this.difficulty = Difficulty.HARD;
        this.endGame = false;
        this.carArray = CarFactory.createAllCars(difficulty);
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public Car[] getCarArray(){
        return this.carArray;
    }

    @Override
    public void run() {

        while(!endGame){

            for (int i = 0; i < carArray.length; i++) {

                if (carArray[i].getMoveDir().equals(MovementDir.RIGHT) && !Helper.gridLimitsRight(carArray[i].getCarIsoPosition()) && carArray[i].isDrawn()) {
                    carArray[i].reset();
                }
                else if (carArray[i].getMoveDir().equals(MovementDir.LEFT) && !Helper.gridLimitsLeft(carArray[i].getCarIsoPosition()) && carArray[i].isDrawn()) {
                    carArray[i].reset();
                }
                else if(Helper.gridLimitsLeft(carArray[i].getCarIsoPosition()) && Helper.gridLimitsRight(carArray[i].getCarIsoPosition()) && !carArray[i].isDrawn()){
                    carArray[i].setDrawn(true);
                }
            }

            for (int i = 0; i < carArray.length; i++) {
                carArray[i].move();
            }

            try {
                Thread.sleep(64);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
