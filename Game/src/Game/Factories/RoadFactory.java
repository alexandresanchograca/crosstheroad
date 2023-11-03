package Game.Factories;

import Game.Actors.Grid.Road;
import Game.Utilities.Difficulty;

import java.util.LinkedList;

public class RoadFactory {
    public static LinkedList<Road> createRoads(Difficulty difficulty ){
        LinkedList<Road> temp = new LinkedList<>();

        switch (difficulty){
            case EASY:
                temp.add(new Road(10));
                temp.add(new Road(14));
                break;
            case MEDIUM:
                temp.add(new Road(6));
                temp.add(new Road(10));
                temp.add(new Road(14));
                break;
            case HARD:
                temp.add(new Road(2));
                temp.add(new Road(6));
                temp.add(new Road(10));
                temp.add(new Road(14));
                temp.add(new Road(18));
                break;
        }

        return  temp;
    }

}
