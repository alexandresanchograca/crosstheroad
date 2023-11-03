package Game.Factories;

import Game.Actors.Cars.Car;
import Game.Utilities.Difficulty;
import Game.Utilities.Lanes;
import Game.Utilities.MovementDir;

import java.util.LinkedList;

public class CarFactory {

    public static LinkedList<Integer> leftRows;
    public static LinkedList<Integer> rightRows;

    public static String getCarImage(MovementDir moveDir){
        CarPictures carPic = CarPictures.values()[(int)(Math.random() * CarPictures.values().length)];
        return carPic.getCarPic(moveDir);
    }

    public static Car[] createAllCars(Difficulty difficulty){ //Transform it just to get it into string form easily

        Car[][] temp2D = generateAllCars(difficulty);

        Car[] temp = new Car[temp2D.length * temp2D[0].length];

        int index = 0;

        for(int i = 0; i < temp2D.length; i++){
            for(int j = 0; j < temp2D[0].length; j++){
                temp[index] = temp2D[i][j];
                index++;
            }
        }

        return temp;
    }

    public static Car[][] generateAllCars(Difficulty difficulty){
        Car[][] tempo = new Car[Lanes.values().length][9];
        for(int i = 0; i < Lanes.values().length; i++){
            tempo[i] = generateCarRow(difficulty, Lanes.values()[i]);
        }
        return tempo;
    }


    public static Car[] generateCarRow(Difficulty difficulty, Lanes lane){

        int startCol = lane.getMoveDir().equals(MovementDir.RIGHT) ? 0 : 24 ;

        Car[] cars = new Car[]{
            new Car(startCol, lane.getStartRow(), lane.getMoveDir(), CarPictures.CISTERN.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed()),
            new Car(startCol-2, lane.getStartRow(), lane.getMoveDir(), CarPictures.PICKUP.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed()),
            new Car(startCol-4, lane.getStartRow(), lane.getMoveDir(), CarPictures.PICKUP.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed()),
            new Car(startCol-7, lane.getStartRow(), lane.getMoveDir(), CarPictures.RACING.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed()),
            new Car(startCol-10, lane.getStartRow(), lane.getMoveDir(), CarPictures.REGULAR.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed()),
            new Car(startCol-12, lane.getStartRow(), lane.getMoveDir(), CarPictures.CISTERN.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed()),
            new Car(startCol-14, lane.getStartRow(), lane.getMoveDir(), CarPictures.REGULAR.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed()),
            new Car(startCol-16, lane.getStartRow(), lane.getMoveDir(), CarPictures.PICKUP.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed()),
            new Car(startCol-18, lane.getStartRow(), lane.getMoveDir(), CarPictures.RACING.getCarPic(lane.getMoveDir()), difficulty.getCarSpeed())
        };

        return cars;
    }

    public static void updateCarsPositions(String info, Car[] cars){

        info = info.substring( info.indexOf(";") +1 ); //Eliminates the Player Data from the string

        while(!info.isEmpty()) {

            int endLine = info.indexOf(";") + 1;
            String carInfo = info.substring(0, endLine );

            info = info.substring(endLine);

            String[] carInfoArray = carInfo.split(",");

            int carIndex =  Integer.parseInt(carInfoArray[0]);
            int carX =  Integer.parseInt(carInfoArray[1]);
            int carY =  Integer.parseInt(carInfoArray[2]);
            MovementDir dir = MovementDir.values()[ Integer.parseInt(carInfoArray[3]) ];
            String carImage = carInfoArray[4];
            //int carSpeed = Integer.parseInt( carInfoArray[5] );

            cars[carIndex].getCarPic().transport(carX, carY);
        }

    }

    public enum CarPictures{
        CISTERN("resources/Cistern_64L.png", "resources/Cistern_64R.png"),
        FIRETRUCK("resources/Firetruck_64L.png", "resources/Firetruck_64R.png"),
        LUXURY("resources/Luxury_Car_64L.png", "resources/Luxury_Car_64R.png"),
        PICKUP("resources/Pick_up_64L.png", "resources/Pick_up_64R.png"),
        RACING("resources/Racing_Car_64L.png", "resources/Racing_Car_64R.png"),
        REGULAR("resources/Regular_Car_64L.png", "resources/Regular_Car_64R.png"),
        ROADSTER("resources/Roadster_64L.png", "resources/Roadster_64R.png");

        private String carPicLeft, carPicRight;

        CarPictures(String carPicLeft, String carPicRight){
            this.carPicLeft = carPicLeft;
            this.carPicRight = carPicRight;
        }

        public String getCarPic(MovementDir moveDir){
            return MovementDir.LEFT.equals(moveDir) ? this.carPicLeft : this.carPicRight;
        }
    }
}
