import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Players implements Runnable{

    private boolean endLoop;
    private final DatagramSocket serverSocket;
    private String playerOneInfo;
    private String playerTwoInfo;
    private int[] playerStartPos;

    public Players(DatagramSocket serverSocket){
        this.endLoop = false;
        this.serverSocket = serverSocket;
        this.playerStartPos = new int[]{458,594}; //Todo create a better player starting pos
        this.playerOneInfo = "Player1," + "false," + playerStartPos[0] + "," + playerStartPos[1] + "," + "0;";
        this.playerTwoInfo = "Player2," + "false," + playerStartPos[0] + "," + playerStartPos[1] + "," + "0;";
    }

    @Override
    public void run() {

        while(!endLoop){
            try {
                DatagramPacket receivedPacket = receivePacket(serverSocket);

                String packetInfo = getPacketInfo(receivedPacket.getData());

                updatePlayerInfo(packetInfo);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPlayerOneInfo() {
        return playerOneInfo;
    }

    public String getPlayerTwoInfo(){
        return playerTwoInfo;
    }

    public DatagramPacket receivePacket(DatagramSocket serverSocket) throws IOException {
        byte[] receivePacketBuffer = new byte[10000];

        DatagramPacket receivingPacket = new DatagramPacket(receivePacketBuffer, receivePacketBuffer.length);

        serverSocket.receive(receivingPacket); //Waits for the receiving packet

        return receivingPacket;
    }

    public void updatePlayerInfo(String info){

        if(info.isEmpty())
            return;

        String[] playerInfoArray = info.split(";");

        if(playerInfoArray.length < 1)
            return;

        if(playerInfoArray[0].contains(Server.playerOneName)){
            this.playerOneInfo = playerInfoArray[0] + ";";
        }
        else if(playerInfoArray[0].contains(Server.playerTwoName)){
            this.playerTwoInfo = playerInfoArray[0] + ";";
        }

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
