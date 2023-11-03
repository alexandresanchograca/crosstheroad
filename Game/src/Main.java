import Game.Game;

public class Main {

    public static void main(String[] args) {
        try {

            String playerName = args[0];
            String serverAddress = args[1];
            int serverPort = Integer.parseInt(args[2]);

            //Execute the game logic
            Game gameObj = new Game(playerName);
            gameObj.run(serverAddress, serverPort);

        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("[Error] Usage: java -jar <executable.jar> <username> <server-address> <serverport>");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}