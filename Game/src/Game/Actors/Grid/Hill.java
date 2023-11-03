package Game.Actors.Grid;

import Game.Utilities.Difficulty;
import Game.Utilities.Util;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.LinkedList;

public class Hill {
    private LinkedList<Picture> hills;
    public Hill(Difficulty difficulty) {
        this.hills = new LinkedList<>();

        for (int row = 0; row < Grid.ROWS; row++) {
            Grid.GridImage tileObj = Grid.GridImage.getTile(0, row, difficulty);

            if (tileObj.equals(Grid.GridImage.HILL) || tileObj.equals(Grid.GridImage.HILLEND)) {
                Picture newHill = Util.createPicture(0, row, 0, tileObj.getOffset(), tileObj.getFilename());
                newHill.draw();
                hills.add(newHill);
            }
        }
    }

    public LinkedList<Picture> getHills() {
        return hills;
    }
}
