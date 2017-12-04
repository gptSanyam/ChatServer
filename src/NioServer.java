import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by sanyamgupta on 01/07/17.
 */
public class NioServer {

    public static final int BUFFER_SIZE = 64;


    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(5051));
        serverChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        Map<SocketChannel, ByteBuffer> activeClientMap = new HashMap<>();
        List<SocketChannel> writeReadySockets = new ArrayList<>();
        List<SocketChannel> yetToAuthenticateUsers = new ArrayList<>();
        Map<String, SocketChannel> userToChannelMap = new HashMap<>();

        System.out.println("Listening connections now...");

        while(true){
            //before next iteration change the interest set of the to write socket to write
            Iterator<SocketChannel> writeSocItr = writeReadySockets.iterator();
            if(writeSocItr.hasNext()){
                SocketChannel toWriteSocket = writeSocItr.next();
                toWriteSocket.register(selector, SelectionKey.OP_WRITE);
                writeSocItr.remove();
            }

            int selectedChannels = selector.select();

            if(selectedChannels>0){
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> itr = keys.iterator();

                while(itr.hasNext()){
                    SelectionKey selectedKey = itr.next();
                    if(selectedKey.isAcceptable()){
                        handleNewConnection(selectedKey, activeClientMap, yetToAuthenticateUsers);
                    }
                    if(selectedKey.isReadable()){
                        handleRead(selectedKey, activeClientMap, writeReadySockets, yetToAuthenticateUsers, userToChannelMap);
                    }
                    if(selectedKey.isWritable()){
                        handleWrite(selectedKey, activeClientMap);
                    }
                    itr.remove();
                }

            }

        }
    }

    private static void handleWrite(SelectionKey selectedKey, Map<SocketChannel, ByteBuffer> activeClientMap) throws IOException {
        SocketChannel soc = (SocketChannel) selectedKey.channel();
        if(soc.isConnected()){
            ByteBuffer buff = activeClientMap.get(soc);
            buff.flip();
            while(buff.hasRemaining()){
                soc.write(buff);
            }
            System.out.println("Data written to "+ soc.getRemoteAddress());
            soc.register(selectedKey.selector(), SelectionKey.OP_READ);
        }
    }

    private static void handleRead(SelectionKey selectedKey, Map<SocketChannel, ByteBuffer> activeClientMap, List<SocketChannel> writeReadySockets, List<SocketChannel> yetToAuthenticateUsers, Map<String, SocketChannel> userToChannelMap) throws IOException {
        SocketChannel soc = (SocketChannel) selectedKey.channel();
        ByteBuffer buff = activeClientMap.get(soc);
        buff.clear();
        if(soc.isConnected()){
            int charsRead = soc.read(buff);//todo: this may result in partial read, handle such situations. What about overflows?
            if(charsRead!=-1){

                String readString = new String(buff.array(), StandardCharsets.UTF_8);

                if(readString==null || readString.isEmpty()){
                    return;
                }

                String relevantMsg = null;
                Message receivedMsg = null;
                try{
                    relevantMsg = readString.substring(0, readString.indexOf("\n")); //idea is that message will have \n only at its end
                    System.out.println("Read "+ charsRead + " bytes : "+ relevantMsg);

                    receivedMsg = new SimpleMessage(relevantMsg);//todo this message type should be like a changable module, so that we are free to change the protocol without touching this part of the code;
                    //todo handle exception while reading
                }catch(Exception e){
                    System.out.println("Got unexpected data from client: "+relevantMsg);
                    //e.printStackTrace();
                    return;
                }

                if(receivedMsg==null || relevantMsg == null){
                    return;
                }

                System.out.println(receivedMsg.toString());

                if(yetToAuthenticateUsers.contains(soc)){
                    boolean authenticated = Utils.authenticateUser(receivedMsg);
                    if(authenticated){
                        userToChannelMap.put(receivedMsg.getSender(), soc);
                        yetToAuthenticateUsers.remove(soc);
                    }
                }else{
                    //write to destination socket
                    if(userToChannelMap.containsKey(receivedMsg.getDestination())){
                        SocketChannel destinationSocket = userToChannelMap.get(receivedMsg.getDestination());
                        byte[] msgToSend = relevantMsg.getBytes();
                        ByteBuffer destinationBuff = activeClientMap.get(destinationSocket);
                        destinationBuff.clear();
                        destinationBuff.put(relevantMsg.getBytes(StandardCharsets.UTF_8));
                        destinationBuff.put(relevantMsg.length(), (byte)'\n');
                        activeClientMap.put(destinationSocket, destinationBuff);
                        writeReadySockets.add(destinationSocket);
                        System.out.println("Forwarded message to "+receivedMsg.getDestination());
                    }else{
                        System.out.println("Destination "+ receivedMsg.getDestination() +" unavailable currently");
                        //persist to db, and replay the message to the destination client on connection
                    }
                }


            }
        }

    }

    private static void handleNewConnection(SelectionKey selectedKey, Map<SocketChannel, ByteBuffer> activeClientMap, List<SocketChannel> yetToAuthenticateUsers) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) selectedKey.channel();
        SocketChannel newUserChannel = serverChannel.accept();
        newUserChannel.configureBlocking(false);
        System.out.println("Connected to: "+ newUserChannel.getRemoteAddress());
        ByteBuffer buff = ByteBuffer.allocate(BUFFER_SIZE);
        activeClientMap.put(newUserChannel, buff); // keeping buffer per client
        newUserChannel.register(selectedKey.selector(), SelectionKey.OP_READ);
        yetToAuthenticateUsers.add(newUserChannel);
    }

}
