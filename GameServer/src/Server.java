import Game.Car;
import Game.GameLogic;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static InetAddress playerOne = null;
    private static int playerOnePort = 0;
    private static InetAddress playerTwo = null;
    private static int playerTwoPort = 0;

    public static String playerOneName = "";
    public static String playerTwoName = "";

    public static int serverPort = 23600;
    private ExecutorService threadPool;

    public Server(){
        this.threadPool = Executors.newCachedThreadPool();
    }

    public static void main(String[] args) {

        Server myServer = new Server();

        //Creating a log so we can find our server errors
        Logger log = Logger.getLogger(Server.class.getName());

        try{
            if(args.length > 0){
                serverPort = Integer.parseInt(args[0]);
            }

            DatagramSocket serverSocket = startServer(serverPort);

            System.out.println("Hosting server on: " + InetAddress.getLocalHost().getHostAddress() + ":" + serverPort);


            //Game logic generates the cars and moves them serverSide
            GameLogic gameLogic = new GameLogic();
            Thread gameLogicThread = new Thread(gameLogic);
            gameLogicThread.start();

            //Receives player1 and 2 packets and stores the most up to date information to then construct the packets
            Players players = new Players(serverSocket);
            Thread playersThread= new Thread(players);
            playersThread.start();

            while(true){ //Waiting for 2 players to join

                DatagramPacket receivedPacket = myServer.receivePacket(serverSocket);

                String info = getPacketInfo(receivedPacket.getData());

                if(!info.isEmpty() && (!info.contains(playerTwoName) || playerTwoName.isEmpty()) && playerOne == null) {
                    playerOneName = info.split(",")[0];
                    playerOne = receivedPacket.getAddress();
                    playerOnePort = receivedPacket.getPort();

                    System.out.println("PlayerOne Connected: " + playerOne.getHostAddress() + ":"+ playerOnePort);
                }

                if(!info.isEmpty() && (!info.contains(playerOneName) || playerOneName.isEmpty()) && playerTwo == null) {
                    playerTwoName = info.split(",")[0];
                    playerTwo = receivedPacket.getAddress();
                    playerTwoPort = receivedPacket.getPort();

                    System.out.println("PlayerTwo Connected: " + playerTwo.getHostAddress() + ":"+ playerTwoPort);

                }

                if(playerOne != null && playerTwo != null)
                    break;
            }

            myServer.sendPacket(serverSocket, playerOne, playerOnePort, players.getPlayerTwoInfo());
            myServer.sendPacket(serverSocket, playerTwo, playerTwoPort, players.getPlayerOneInfo());

            boolean end = false;
            while(!end) {
                DatagramPacket receivedPacket = myServer.receivePacket(serverSocket);

                String playerOnePacketInfo = constructPacketInfo(players.getPlayerOneInfo(), gameLogic.getCarArray());
                String playerTwoPacketInfo = constructPacketInfo(players.getPlayerTwoInfo(), gameLogic.getCarArray());

                myServer.sendPacket(serverSocket, playerTwo, playerTwoPort, playerOnePacketInfo);
                myServer.sendPacket(serverSocket, playerOne, playerOnePort, playerTwoPacketInfo);

                try {
                    Thread.sleep(25);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            myServer.threadPool.shutdown();
            serverSocket.close();

        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("[Error] Usage: java -jar <executable> <serverPort>");
        }
        catch (Exception e){
            log.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

    }

    public static DatagramSocket startServer(int port) throws SocketException {
        return new DatagramSocket(port);
    }

    public static String getPacketInfo(byte[] data){

        StringBuilder packetData = new StringBuilder();
        for(int i = 0; i < data.length; i++){

            if(data[i] == 0)
                break;

            packetData.append((char)data[i]);

        }

        return packetData.toString();
    }

    public static byte[] setPacketInfo(String data){
        return data.getBytes();
    }

    public DatagramPacket receivePacket(DatagramSocket serverSocket) throws IOException {
        byte[] receivePacketBuffer = new byte[10000];

        DatagramPacket receivingPacket = new DatagramPacket(receivePacketBuffer, receivePacketBuffer.length);

        serverSocket.receive(receivingPacket); //Waits for the receiving packet

        return receivingPacket;
    }


     /* Lambda implementation used here.
     * it can only be used on Single Abstract Method Classes (SAM) interfaces/classes, in this case Runnable interface
     * which has a SAM, the run() method that can be used to create a new Thread to execute it
     * so we can send packets to playerOne and playerTwo with a reduced delta time between these packets
     * */
    public void sendPacket(DatagramSocket serverSocket, InetAddress playerAddress, int playerPort, String data) throws IOException {
        threadPool.submit( () -> {
            byte[] sendData = data.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,playerAddress, playerPort);
            try {
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static String constructPacketInfo(String player, Car[] carArray){

        StringBuilder playerInfo = new StringBuilder();

        playerInfo.append( player );

        StringBuilder carInfo = new StringBuilder();
        for(int i = 0; i < carArray.length; i++){

            carInfo.append(i + ",");
            carInfo.append(carArray[i].getCarIsoPosition()[0] + ",");
            carInfo.append(carArray[i].getCarIsoPosition()[1] + ",");
            carInfo.append(carArray[i].getMoveDir().ordinal() + ",");
            carInfo.append("imagename" + ",");
            carInfo.append(carArray[i].getCarSpeed() + ";");

        }

        return playerInfo.append(carInfo).toString();
    }
}
