package jhelp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class provides a network connection between end client of
 * {@link jhelp.Client} type and {@link jhelp.Server} object. Every object of
 * this class may work in separate thread.
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 * @see jhelp.Client
 * @see jhelp.Server
 */
public class ClientThread implements JHelp, Runnable 
{
    private Server server;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    /**
     * Creates a new instance of Client
     * @param server reference to {@link Server} object.
     * @param socket reference to {@link java.net.Socket} object for connection
     * with client application.
     */
    public ClientThread(Server server, Socket socket) 
    {
        this.server=server;
        this.clientSocket=socket;
        System.out.println("CT: constructor (server,socket) executed");
        //run();
    }
    @Override
    public void run() 
    { 
        try
        {
            connect();
            output.writeObject(getData((Data) input.readObject()));
            disconnect();
        }
        catch (ClassNotFoundException ex) {System.err.println("CT : Class Error in run:GetData call ->"+ex.getMessage());} 
        catch (IOException ex) {System.err.println("CT : IO Error in run:GetData call ->" + ex.getMessage());}
        System.out.println("CT: run executed");
    }
    /**
     * Opens input and output streams for data interchanging with
     * client application.  The method uses default parameters.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect() 
    {
        String args[]={};
        this.connect(args);
        
        System.out.println("CT : connect() executed");
        return JHelp.OK;
    }
    /**
     * Opens input and output streams for data interchanging with
     * client application. This method uses parameters specified by parameter
     * <code>args</code>.
     * @param args defines properties for input and output streams.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect(String[] args) 
    {
        try 
        {
            input = new ObjectInputStream(clientSocket.getInputStream());//-- This is Server -> first input, then output
            output = new ObjectOutputStream(clientSocket.getOutputStream()); 
            System.out.println("CT : Client successfully conneted to this server, thread created!");
        } 
        catch (IOException ex) {System.err.println("CT : Error in creating connections " + ex.getMessage());return JHelp.ERROR;}
        System.out.println("CT: connect(String[] args)");
        return JHelp.OK;
        
    }
    /**
     * Transports {@link Data} object from client application to {@link Server}
     * and returns modified {@link Data} object to client application.
     * @param data {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */
    @Override
    public Data getData(Data data) 
    {
        System.out.println("CT : getData executed");
        return   server.getData(data); 
    }

    /**
     * The method closes connection with client application.
     * @return error code. The method returns {@link JHelp#OK} if input/output 
     * streams and connection with client application was closed successfully,
     * otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int disconnect() 
    {
        try 
        {
            input.close();
            output.close();
            clientSocket.close();
        } 
        catch (IOException ex) {System.err.println("CT error on closing connetction->"+ ex.getMessage());        }
        System.out.println("CT:client disconnected");
        return JHelp.OK;
    }
}
