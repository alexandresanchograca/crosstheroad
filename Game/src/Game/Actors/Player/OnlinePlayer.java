package Game.Actors.Player;

import Game.Actors.Cars.Car;
import Game.Utilities.Util;

public class OnlinePlayer extends Player{
    public OnlinePlayer(Car[] carArray) {
        super(carArray);
    }

    public void updateOnlinePlayer(String info){

        if(info.isEmpty())
            return;

        String[] playerInfo = info.split(",|;");

        if(playerInfo.length < 5)
            return;

        String playerName = playerInfo[0];
        String playerConnected = playerInfo[1];
        int playerPosX = Integer.parseInt(playerInfo[2]);
        int playerPosY = Integer.parseInt(playerInfo[3]);
        int score = Integer.parseInt(playerInfo[4]);

        this.scoreboard.updateRemotePlayerName(playerName);
        this.scoreboard.setScore(score);
        setOnlineMoveAnimation(playerPosX, playerPosY);
        this.getPlayerPic().transport(playerPosX, playerPosY);
    }

    public void setOnlineMoveAnimation(int newPosx, int newPosY){

        if(newPosx == playerPic.getX() && newPosY == playerPic.getY())
            return;

        int[] newGridCoords = Util.toGrid(newPosx, newPosY);
        int[] oldGridCoords = Util.toGrid(playerPic.getX(), playerPic.getY());


        if(newGridCoords[0] > oldGridCoords[0]){ //Moved right
            moveRight();
        }
        else if(newGridCoords[0] < oldGridCoords[0]){ //Moved left
            moveLeft();
        }
        else if(newGridCoords[1] > oldGridCoords[1]){ //Moved Down
            moveDown();
        }else{ //Moved Up
            moveUp();
        }

    }
}
