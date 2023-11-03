package Game.Actors.Player;

import Game.Game;
import org.academiadecodigo.simplegraphics.graphics.Text;

public class Score {
    private int score;
    private Text scoreText;
    private int[] scorePosition;
    private String playerName;
    private Player playerOwner;
    private boolean onlinePlayer;
    public Score(Player player){
        this.playerOwner = player;
        this.onlinePlayer = playerOwner instanceof OnlinePlayer;
        this.scorePosition = onlinePlayer ? new int[]{1375, 825} : new int[]{200, 825};
        this.playerName = onlinePlayer ? "P2" : Game.playerUser;
        try {
            this.score = 0;
            this.scoreText = new Text(scorePosition[0], scorePosition[1], "| " + playerName + ": " + score + " |");
            scoreText.grow(150, 50);
            scoreText.draw();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateScore(){
        this.score++;
        this.scoreText.delete();
        Text temp = new Text(scorePosition[0], scorePosition[1], "| " + playerName + ": " + score + " |");
        temp.grow(150,50);
        temp.draw();
        this.scoreText = temp;
    }

    public void resetScore(){
        this.score = 0;
        this.scoreText.delete();
        Text temp = new Text(scorePosition[0], scorePosition[1], "| " + playerName + ": " + score + " |");
        temp.grow(150,50);
        temp.draw();
        this.scoreText = temp;
    }

    public void setScore(int score) {

        if(this.score == score)
            return;

        this.score = score;
        this.scoreText.delete();
        Text temp = new Text(scorePosition[0], scorePosition[1], "| " + playerName + ": " + score + " |");
        temp.grow(150,50);
        temp.draw();
        this.scoreText = temp;
    }

    public void updateRemotePlayerName(String name){
        if(name.equals(playerName))
            return;

        playerName = name;
        this.scoreText.delete();
        Text temp = new Text(scorePosition[0], scorePosition[1], "| " + playerName + ": " + score + " |");
        temp.grow(150,50);
        temp.draw();
        this.scoreText = temp;
    }

    public int getScore() {
        return score;
    }
}
