package Game;

import Game.Actors.Player.OnlinePlayer;
import Game.Actors.Player.Player;
import Game.Actors.Cars.Car;
import Game.Factories.CarFactory;

import java.io.IOException;
import java.net.*;

/** Singleton implementiation class that handles the connection to our server*/
public class ClientConnection {

    private final InetAddress serverAddress;
    private final int serverPort;
    private final DatagramSocket clientSocket;
    private Car[] carArray;
    private OnlinePlayer onlinePlayer;
    private Player localPlayer;

    public ClientConnection(Player localPlayer, Car[] carArray, String serverIp, int serverPort) throws IOException {
        this.serverAddress = InetAddress.getByName(serverIp);
        this.serverPort = serverPort;
        this.clientSocket = new DatagramSocket();
        this.carArray = carArray;
        this.onlinePlayer = new OnlinePlayer(carArray);
        this.localPlayer = localPlayer;
        byte[] sendData = (Game.playerUser + ",true;").getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

        clientSocket.send(sendPacket);
    }

    public class sendPacket implements Runnable{ //Could also be passed as a lambda but this is more organized

        @Override
        public void run() {
            while(true) {
                byte[] dataPacket = constructPacket(localPlayer).getBytes();
                DatagramPacket sendPacketData = new DatagramPacket(dataPacket, dataPacket.length, serverAddress, serverPort);
                try {
                    clientSocket.send(sendPacketData);

                    //Thread.sleep(0);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public class receivePacket implements Runnable{

        public void run() {
            while(true) {
                byte[] receiveBuffer = new byte[10000];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                try {
                    clientSocket.receive(receivePacket); //Waits for a receiving packet
                    String packetInfo = ClientConnection.getPacketInfo(receiveBuffer);

                    if (packetInfo.isEmpty())
                        continue;

                    String playerInfo = packetInfo.substring( 0, packetInfo.indexOf(";") - 1 ); //Eliminates the Player Data from the string
                    onlinePlayer.updateOnlinePlayer(packetInfo);
                    CarFactory.updateCarsPositions(packetInfo, carArray);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public String constructPacket(Player player){

        StringBuilder playerInfo = new StringBuilder();

        playerInfo.append( Game.playerUser + "," );
        playerInfo.append( "true," );
        playerInfo.append(  player.getPlayerPic().getX() + "," );
        playerInfo.append(  player.getPlayerPic().getY() + "," );
        playerInfo.append(  player.getScore() + ";" );

        return playerInfo.toString();
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
}
