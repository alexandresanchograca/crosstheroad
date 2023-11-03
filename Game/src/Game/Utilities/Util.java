package Game.Utilities;

import Game.Actors.Grid.Grid;
import org.academiadecodigo.simplegraphics.graphics.Shape;
import org.academiadecodigo.simplegraphics.pictures.Picture;

public class Util {
    private static int TILE_WIDTH_HALF = 64/2; //ImageWidth estamos a usar 64x64 tiles
    private static int TILE_HEIGHT_HALF = 64/2; //ImageHeight

    private static int TILE_WIDTH_QUARTER = TILE_WIDTH_HALF/2;
    private static int TILE_HEIGHT_QUARTER = TILE_HEIGHT_HALF/2;

    public static int[] toIso(int x, int y){

        int i = (x - y) * TILE_WIDTH_HALF;
        int j = (x + y) * TILE_HEIGHT_QUARTER;

        i += 810-TILE_WIDTH_HALF;
        j+=50;

        return new int[]{i,j};
    }

    public static int[] toIsoAlt(int x, int y, int cellWidth, int cellHeight){

        int i = (x - y) * (cellWidth / 2);
        int j = (x + y) * (cellHeight/4);



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

    public static double[] translateMovement(Picture pic, MovementDir moveDir, double speed){

        int[] cenasOld = Util.toIso(pic.getX(), pic.getY());
        int[] cenas = Util.toIso(pic.getX(), pic.getY());
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

    /* Returns a new picture given a grid position (col and row) */
    public static Picture createPicture(int col, int row, String filename){
        int[] gridPos = Util.toIso(col, row);
        return new Picture(gridPos[0], gridPos[1], filename);
    }

    /* Returns a new picture given a grid position (col and row) */
    public static Picture createPicture(int col, int row, int offsetX, int offsetY, String filename){
        int[] gridPos = Util.toIso(col, row);
        gridPos[0] += offsetX;
        gridPos[1] += offsetY;
        return new Picture(gridPos[0], gridPos[1], filename);
    }

    public static boolean gridLimitsRight(Picture pic){
        int[] gridPos = Util.toGrid(pic.getX() + pic.getWidth(), pic.getY() + pic.getHeight());
        return gridPos[0] < Grid.COLS - 1 && gridPos[1] < Grid.COLS - 1;
    }

    public static boolean gridLimitsLeft(Picture pic){
        int[] gridPos = Util.toGrid(pic.getX() + pic.getWidth(), pic.getY() + 15);
        return gridPos[0] > 0 && gridPos[1] > 0;
    }

    public static boolean gridLimitsDown(Picture pic){
        int[] gridPos = Util.toGrid(pic.getX(), pic.getY() + pic.getHeight());
        return gridPos[0] > 1 && gridPos[1] < Grid.ROWS - 1;
    }

    public static boolean gridLimitsUp(Picture pic){
        int[] gridPos = Util.toGrid(pic.getX() + pic.getWidth(), pic.getY() + pic.getHeight());
        return gridPos[0] > 1 && gridPos[1] > 1;
    }

    public static boolean gridLimitsRight(Picture pic, int speed){

        double[] newGridPos = Util.translateMovement(pic, MovementDir.RIGHT, speed);

        int[] gridPos = Util.toGrid(pic.getX() + newGridPos[0] + pic.getWidth(), pic.getY() + newGridPos[1] + pic.getHeight());
        return gridPos[0] < Grid.COLS - 1 && gridPos[1] < Grid.COLS - 1;
    }

    public static boolean gridLimitsLeft(Picture pic, int speed){

        double[] newGridPos = Util.translateMovement(pic, MovementDir.LEFT, speed);


        int[] gridPos = Util.toGrid(pic.getX() + newGridPos[0]+ pic.getWidth(), pic.getY() + newGridPos[1]);
        return gridPos[0] > -1 && gridPos[1] >-1;
    }

    public static boolean gridLimitsDown(Picture pic, int speed){

        double[] newGridPos = Util.translateMovement(pic, MovementDir.DOWN, speed);


        int[] gridPos = Util.toGrid(pic.getX() + newGridPos[0], pic.getY() + newGridPos[1] + pic.getHeight());
        return gridPos[0] > -1 && gridPos[1] < Grid.ROWS - 1;
    }

    public static boolean gridLimitsUp(Picture pic, int speed){

        double[] newGridPos = Util.translateMovement(pic, MovementDir.UP, speed);


        int[] gridPos = Util.toGrid(pic.getX() + newGridPos[0], pic.getY() + newGridPos[1] - 10);
        return gridPos[0] > -1 && gridPos[1] > -1;
    }


    public static int getImageWidth(String imagePath){
        return new Picture(0,0, imagePath).getWidth();
    }

    public static int getImageHeight(String imagePath){
        return new Picture(0,0, imagePath).getHeight();
    }

    public static int getColX(int colPos){
        return Grid.PADDING + (colPos * Grid.CELLSIZE);
    }

    public static int getRowY(int rowPos){
        return Grid.PADDING + (rowPos * Grid.CELLSIZE);
    }

    public static int getPixelPos(int gridPos){
        return Grid.PADDING + (gridPos * Grid.CELLSIZE);
    }

    public static int getGridPos(int pixel){
        return (pixel - Grid.PADDING) / Grid.CELLSIZE;
    }

    public static int getYRow(int yPixel){
        return (yPixel - Grid.PADDING) / Grid.CELLSIZE;
    }

    public static float getDistance(int posOne, int posTwo){
        return Math.abs((float)posOne - (float)posTwo);
    }

    public static float getDistance2D(Shape objOne, Shape objTwo){
        return (float)Math.sqrt(Math.pow(objTwo.getX() - objOne.getX(), 2) - Math.pow(objTwo.getY() - objOne.getY(), 2));
    }

}
