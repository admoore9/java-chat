/**
 * Created by danielbardin on 4/6/15.
 */

import java.net.*;
import java.io.*;

public class Client implements Runnable
{
    private Socket socket = null;
    private Thread thread = null;
    private BufferedReader consoleBuffer = null;
    private DataOutputStream outputStream = null;
    private ClientThread client = null;


    /*
        Create client object
     */
    public Client(String serverName, int serverPort)
    {
        System.out.println("Connecting to server host: " + serverName + " at port: " + serverPort);
        try
        {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        }
        catch (UnknownHostException e)
        {
            System.out.println("Host unknown: " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("Unexpected exception: " + e.getMessage());
        }
    }

    /*
        create a new client thread
     */
    public void run()
    {
        while (thread != null) {
            try {
                outputStream.writeUTF(consoleBuffer.readLine());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Sending error: " + e.getMessage());
                stop();
            }
        }
    }


    /*
        method for a client to send a message
     */
    public void sendMessage(String message)
    {
        if (message.equals("logmeout")) {
            System.out.println("logmeout recognized. Press RETURN to exit.");
            stop();
        }
        else
            System.out.println(message);
    }

    /*
        initialize a client thread
     */
    public void start() throws IOException
    {
        consoleBuffer = new BufferedReader(new InputStreamReader(System.in));
        outputStream = new DataOutputStream(socket.getOutputStream());

        if (thread == null)
        {
            client = new ClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    /*
        Log out a client thread, shut down connections, close streams
     */
    public void stop()
    {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }

        try
        {
            if (consoleBuffer != null)
                consoleBuffer.close();
            if (outputStream != null)
                outputStream.close();
            if (socket != null)
                socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Error closing client process. Error message: " + e);
        }
        client.close();
        client.interrupt();
    }

    public static void main(String args[])
    {
        Client client = null;
        if (args.length != 2)
            System.out.println("Usage: java Client host port");
        else
            client = new Client(args[0], Integer.parseInt(args[1]));
    }
}
