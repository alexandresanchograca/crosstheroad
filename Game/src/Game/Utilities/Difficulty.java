package Game.Utilities;

/** This class is used to help to generate the right ammount of difficulty of the levels
 * The stats will be maxed to level 5
 * */
    public enum Difficulty{
        EASY(5, 25),
        MEDIUM(8,50),
        HARD(12,100);

        private int carSpeed;
        private int maxCars;

        Difficulty(int carSpeed, int maxCars){
            this.carSpeed = carSpeed;
            this.maxCars = maxCars;
        }

        public int getCarSpeed() {
            return carSpeed;
        }

        public int getMaxCars() {
            return maxCars;
        }
    }
