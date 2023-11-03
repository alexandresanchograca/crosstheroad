package Game;

public class Helper {
    private static int TILE_WIDTH_HALF = 64/2; //ImageWidth estamos a usar 64x64 tiles
    private static int TILE_HEIGHT_HALF = 64/2; //ImageHeight
    private static int pictureWidth = 64;
    private static int pictureHeight = 64;

    private static int TILE_WIDTH_QUARTER = TILE_WIDTH_HALF/2;
    private static int TILE_HEIGHT_QUARTER = TILE_HEIGHT_HALF/2;

    public static int[] toIso(int x, int y){

        int i = (x - y) * TILE_WIDTH_HALF;
        int j = (x + y) * TILE_HEIGHT_QUARTER;

        i += 810-TILE_WIDTH_HALF;
        j+=50;

        return new int[]{i,j};
    }

    public static int[] toGrid(double i, double j){

        i-=810;
        j-=50;

        double tx = Math.ceil(((i / TILE_WIDTH_HALF) + (j / TILE_HEIGHT_QUARTER))/2);
        double ty = Math.ceil(((j / TILE_HEIGHT_QUARTER) - (i / TILE_WIDTH_HALF))/2);

        int x = (int) Math.ceil(tx)-1;
        int y = (int) Math.ceil(ty)-1;

        return new int[]{x, y};
    }

    public static double[] translateMovement(int posX, int posY, MovementDir moveDir, double speed){

        int[] cenasOld = Helper.toIso(posX, posY);
        int[] cenas = Helper.toIso(posX, posY);
        int[] newCenas = new int[2];
        newCenas[0]= cenas[0] - cenasOld[0];
        newCenas[1]= cenas[1] - cenasOld[1];

        double speedX = speed / 2;
        double speedY = speed / 4;

        double[] diff = new double[2];
        switch (moveDir){
            case UP:
                diff[0] = (double)(newCenas[0]) + speedX;
                diff[1] = (double)(newCenas[1]) - speedY;
                break;
            case DOWN:
                diff[0] = (double)(newCenas[0]) - speedX;
                diff[1] = (double)(newCenas[1]) + speedY;
                break;
            case LEFT:
                diff[0] = (double)(newCenas[0]) - speedX;
                diff[1] = (double)(newCenas[1]) - speedY;
                break;
            case RIGHT:
                diff[0] = (double)(newCenas[0]) + speedX;
                diff[1] = (double)(newCenas[1]) + speedY;
                break;
            default:
                diff[0] = 0;
                diff[1] = 0;
                break;
        }

        return diff;
    }

    public static boolean gridLimitsRight(int[] carPos){
        int[] gridPos = Helper.toGrid(carPos[0] + pictureWidth, carPos[1] + pictureHeight);
        return gridPos[0] < Grid.COLS - 1 && gridPos[1] < Grid.COLS - 1;
    }

    public static boolean gridLimitsLeft(int[] carPos){
        int[] gridPos = Helper.toGrid(carPos[0] + pictureWidth, carPos[1] + 15);
        return gridPos[0] > 0 && gridPos[1] > 0;
    }

    public static boolean gridLimitsRight(int[] carPos, int speed){

        double[] newGridPos = Helper.translateMovement(carPos[0], carPos[1], MovementDir.RIGHT, speed);

        int[] gridPos = Helper.toGrid(carPos[0] + newGridPos[0] + pictureWidth, carPos[1] + newGridPos[1] + pictureHeight);
        return gridPos[0] < Grid.COLS - 1 && gridPos[1] < Grid.COLS - 1;
    }

    public static boolean gridLimitsLeft(int[] carPos, int speed){

        double[] newGridPos = Helper.translateMovement(carPos[0], carPos[1], MovementDir.LEFT, speed);


        int[] gridPos = Helper.toGrid(carPos[0] + newGridPos[0]+ pictureWidth, carPos[1] + newGridPos[1]);
        return gridPos[0] > -1 && gridPos[1] >-1;
    }

    public static boolean gridLimitsDown(int[] carPos, int speed){

        double[] newGridPos = Helper.translateMovement(carPos[0], carPos[1], MovementDir.DOWN, speed);


        int[] gridPos = Helper.toGrid(carPos[0] + newGridPos[0], carPos[1] + newGridPos[1] + pictureHeight);
        return gridPos[0] > -1 && gridPos[1] < Grid.ROWS - 1;
    }

    public static boolean gridLimitsUp(int[] carPos, int speed){

        double[] newGridPos = Helper.translateMovement(carPos[0], carPos[1], MovementDir.UP, speed);


        int[] gridPos = Helper.toGrid(carPos[0] + newGridPos[0], carPos[1] + newGridPos[1] - 10);
        return gridPos[0] > -1 && gridPos[1] > -1;
    }


}
