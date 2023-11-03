package Game;

public class CarFactory {


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
                    new Car(startCol, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed()),
                    new Car(startCol-2, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed()),
                    new Car(startCol-4, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed()),
                    new Car(startCol-7, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed()),
                    new Car(startCol-10, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed()),
                    new Car(startCol-12, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed()),
                    new Car(startCol-14, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed()),
                    new Car(startCol-16, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed()),
                    new Car(startCol-18, lane.getStartRow(), lane.getMoveDir(), difficulty.getCarSpeed())
            };

        return cars;
    }


}
