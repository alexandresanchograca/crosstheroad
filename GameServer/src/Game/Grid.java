package Game;

/** Main grid, where we will place our objects */
public class Grid {
    public static final int PADDING = 10;
    public static final int CELLSIZE = 128;
    public static final int COLS = 24;
    public static final int ROWS = 24;

    public static int getCols() {
        return COLS;
    }

    public static int getRows() {
        return ROWS;
    }

    public static int getWidth(){
        return (CELLSIZE * COLS) + PADDING;
    }

    public static int getHeight(){
        return (CELLSIZE * ROWS) + PADDING;
    }

    private static int getCellSize(){
        return CELLSIZE;
    }
}
