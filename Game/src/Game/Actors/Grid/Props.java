package Game.Actors.Grid;

import Game.Utilities.Difficulty;
import Game.Utilities.Util;
import Game.Utilities.Lanes;
import Game.Utilities.MovementDir;
import org.academiadecodigo.simplegraphics.pictures.Picture;

/** Used to draw Props */
public class Props {
    private MovementDir moveDir;
    private Picture[] propsPic;

    public Props(Difficulty diff){
        for(int i = 0; i < Grid.ROWS; i++){

            if(Lanes.isLane(i + 1, diff))
                continue;

            if(i > 20)
                break;

            Prop currentProp = Prop.getRandomProp();

            Picture propPic = Util.createPicture(Grid.COLS - 1, i, 0, currentProp.offset * -1, currentProp.filename);

            propPic.draw();
        }
    }

    public enum Prop{
        TreeOne("resources/png/coniferAltShort.png", 0);

        private String filename;
        private int offset;
        Prop(String filename, int offset){
            this.filename = filename;
            this.offset = offset;
        }

        public static Prop getRandomProp(){
            return TreeOne;
        }

    }

}
