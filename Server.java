package jhelp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * This class sets a network connection between end client's objects
 * of {@link jhelp.Client} type and single {@link jhelp.ServerDb} object.
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 * @see jhelp.Client
 * @see jhelp.ClientThread
 * @see jhelp.ServerDb
 */
public class Server implements JHelp
{
    private ServerSocket serverSocket;
    private Socket clientSocket;
   
    private Set<Socket> SocketColl=new HashSet<Socket>();
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    private int SvPort;//--class var  to initiate local serverport
    private int DbPort;//--class var to initiate serverDb port
    private String LOCALHOST = "localhost";//--local adress


    public Server() 
    {
        this(DEFAULT_SERVER_PORT, DEFAULT_DATABASE_PORT);
        System.out.println("Server: Default Server Constructed");
    }
    public Server(int port, int dbPort) //--constructor have to make one socket for data-base connection(as client) and system of sockets for clients(as server)
    {
        SvPort = port;
        DbPort = dbPort;
        System.out.println("Server: Server(int port, int dbPort) Constructed");
    }
    public static void main(String[] args) 
    {
        System.out.println("Server: main");//-- if non-console starting SERVER or starting without paramaters
        Server server = new Server();
        if(args.length==0)
            {
                System.out.println("Server: Start Server with Default parameters");
                if (server.connect() == JHelp.OK) 
                {            
                    server.run();
                    //server.disconnect();
                }
            }
        else
        {
            System.out.println("Server: Start Server with Custom parameters");
                if (server.connect(args) == JHelp.OK) 
                {
                    server.run();
                   // server.disconnect();
                }
        }
    }
    private void run() 
    { 
        try {serverSocket = new ServerSocket (SvPort);}
        catch (IOException ex) {System.err.println("Server : IO Error in run -> "+ex.getMessage());} 
        while(true)
        {
            try 
            {
                Socket clientSocket1 = new Socket();
                clientSocket1= serverSocket.accept();
                if(!clientSocket1.isClosed())
                {
                    ClientThread CT=new ClientThread(this,clientSocket1);
                    Thread t1=new Thread(CT);
                    t1.start();
                }
            } 
            catch (IOException ex) {System.err.println("Server : IO Error in run -> "+ex.getMessage());} 
            System.out.println("Server : query executed");
        }    
    }
    /**
     * The method sets connection to database ({@link jhelp.ServerDb} object) and
     * create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests. This method uses default parameters for connection.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect() //-- connect to ServerDb as client
    {
        Integer ddp = (Integer) DbPort;
        String defDp = ddp.toString();
        String[] args = {LOCALHOST, defDp};
        this.connect(args);
        System.out.println("Server : connect()");
        return OK;
    }
    public int connect(int dbPort) //-- overloaded method if only dbPort need to change in server() method (method added by my own)
    {
        Integer ddp = (Integer) dbPort;
        String defDp = ddp.toString();
        String[] args = {"localhost", defDp};
        this.connect(args);
        System.out.println("SERVER: connect");
        return OK;
    }    
     /**
     * The method sets connection to database ({@link jhelp.ServerDb} object) and
     * create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests.
     * @param args specifies properties of connection.
     * @return error code. The method returns {@link JHelp#OK} if connection are
     * openeds uccessfully, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect(String[] args) 
    {
        String host = args[0];
        int port = parseInt(args[1]);
        try 
        {
            //AS CLIENTT!!1 FOR SERVERDB
            clientSocket = new Socket (host,port);
            output = new ObjectOutputStream(clientSocket.getOutputStream());//-- This is Client ->first output, then inputput
            input = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Server : Server successfully connected to ServerDB!");
        } 
        catch (IOException ex) {System.err.println("Server : Error in creating connections " + ex.getMessage());}
        return OK;
    }
    /**
     * Transports initial {@link Data} object from {@link ClientThread} object to
     * {@link ServerDb} object and returns modified {@link Data} object to
     * {@link ClientThread} object.
     * @param data Initial {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */
    @Override
    public Data getData(Data data) 
    {  //AS SERVER for Client
        try 
        {
           output.writeObject(data); 
           return (Data)input.readObject();
        } 
        catch (IOException ex) {System.err.println("Server : Error in creating connections->" + ex.getMessage());}
        catch (ClassNotFoundException ex) {System.err.println("Server : Error in returning Data class from ServerDB->" + ex.getMessage());}
        System.out.println("Server : getData executed");
        return null;
    }
    /**
     * The method closes connection with database.
     * @return error code. The method returns {@link JHelp#OK} if a connection
     * with database ({@link ServerDb} object) closed successfully,
     * otherwise the method returns {@link JHelp#ERROR} or any error code.
     */
    @Override
    public int disconnect() 
    {
        System.out.println("Server : disconnect executed");
        return OK;
    }
}
