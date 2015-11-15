/*
 * Client.java
 *
 */
package jhelp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements JHelp, ActionListener, WindowListener 
{
    public static final long serialVersionUID = 1234;
    private Properties prop;
    private Data data;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    JTextField termText = new JTextField("Insert item name");
    JTextArea termDefText = new JTextArea("Item description",5,1);
    
    public Client(String[] args) 
    {
        this();
        System.out.println("Client: constructor (args) executed");
    }
    public Client() 
    {
        JPanel glPan = new JPanel();
        JPanel mainPan = new JPanel();
        JPanel settingPan = new JPanel();
        JPanel helpPan = new JPanel(); 
        mainPan.setLayout(new BorderLayout());
        settingPan.setLayout(new BorderLayout());
        helpPan.setLayout(new BorderLayout());
        JTabbedPane tabbed = new JTabbedPane();
        tabbed.setSize(720, 620);
        tabbed.setLocation(0, 0);

        tabbed.addTab("Main", mainPan);
        tabbed.addTab("Configuration", settingPan);
        tabbed.addTab("About", helpPan);
        
        JLabel term = new JLabel("Item");
        //term.setSize(10, 10);
        //term.setLocation(0, 0);
        JLabel def = new JLabel("Description");
        
        //JTextArea termDefText = new JTextArea("Описание термина",1,1);
        
        JButton findBut = new JButton("Find");
        JButton addBut = new JButton("Add");
        JButton editBut = new JButton("Change");
        JButton delBut = new JButton("Delete");
        JButton nextBut = new JButton("Forward");
        JButton prevousBut = new JButton("Backward");
        JButton exitBut = new JButton("Exit");
       
        JMenuBar mb = new JMenuBar();
        JMenu fileMB = new JMenu();
        JMenuItem openFileMB = new JMenuItem();
        fileMB.add(openFileMB);
        
        mb.add(fileMB);
        
        mb.add(new JMenu("Input"));
        mb.add(new JMenu("Options"));
        mb.add(new JMenu("Help"));
        
        setJMenuBar(mb);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  
        addWindowListener(this);
        setSize(720,620);
        setLocationRelativeTo(null);

        glPan.setLayout(null);
        mainPan.setLayout(null);
        //topPan.setLayout(topPanLay);
 
       // glPan.setLayout(new BorderLayout());

        add(glPan);
        glPan.add(tabbed);
        
        //mainPan.setSize(750, 600);
       // mainPan.setLocation(0, 0);
        term.setSize(50, 20);
        term.setLocation(10,10);
        termText.setSize(450, 20);
        termText.setLocation(term.getX() + term.getWidth() + 5, 10);
        def.setSize(70, 20);
        def.setLocation(10, term.getY() + term.getHeight()+ 5);
        termDefText.setSize(550, 470);
        termDefText.setLocation(10, def.getY() + def.getHeight()+ 5);
        findBut.setSize(120, 20);
        findBut.setLocation(termDefText.getX()+ termDefText.getWidth() + 10, termText.getY());
        addBut.setSize(120, 20);
        addBut.setLocation(findBut.getX(),termDefText.getY());
        editBut.setSize(120, 20);
        editBut.setLocation(findBut.getX(),addBut.getY() + addBut.getHeight() + 10);
        
        delBut.setSize(120, 20);
        delBut.setLocation(findBut.getX(),editBut.getY() + editBut.getHeight() + 10);
        
        nextBut.setSize(120, 20);
        nextBut.setLocation(findBut.getX(),delBut.getY() + delBut.getHeight() + 30);
        
        prevousBut.setSize(120, 20);
        prevousBut.setLocation(findBut.getX(),nextBut.getY() + nextBut.getHeight() + 10);
        
        exitBut.setSize(120, 20);
        exitBut.setLocation(findBut.getX(),termDefText.getY() + termDefText.getHeight() - exitBut.getHeight());
        
        findBut.addActionListener(this);
        exitBut.addActionListener(this);
        
        mainPan.add(term); 
        mainPan.add(termText);
        mainPan.add(def);
        mainPan.add(termDefText);
        mainPan.add(findBut);
        mainPan.add(addBut);
        mainPan.add(editBut);
        mainPan.add(delBut);
        mainPan.add(nextBut);
        mainPan.add(prevousBut);
        mainPan.add(exitBut);
        setResizable(false);
        setTitle("Конь 17-42");
        setVisible(true);
    }
    static public void main(String[] args) 
    {
      Client client = new Client(args);
    }
    public void run(String terms) 
    {
        System.out.println("Client: run started");
        Item t = new Item(terms);
        Data d = new Data(t);
        getData(d);
        System.out.println("Client: getData executed");
    }
    @Override
    public int connect() 
    {
        String[] args = {};
        return this.connect(args);
    }
    @Override
    public int connect(String[] args) 
    {
        String host = "localhost";
        int port=JHelp.DEFAULT_SERVER_PORT;
        System.out.println("Client: connect");
        try 
        {
            clientSocket = new Socket (host, port);
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("SUCCESSFULLY connected with serverDb!");
            return JHelp.OK;
        } 
        catch (IOException ex) {System.out.println("Client:IO error in connect(args)");}
        return JHelp.ERROR;
     }
    @Override
    public Data getData(Data data) {
        Data test;  
        try 
        {
            output.writeObject(data);
            test = (Data)input.readObject();
            termDefText.setText("");
            for(int i=0; i<test.getValues(). length;i++)
            {
                termDefText.setText(termDefText. getText() + test.getValue(i).getItem() + "\n");
            }
        } 
        catch (ClassNotFoundException ex) {System.err.println("Client : getData Class definition error -> "+ex.getMessage());} 
        catch (IOException ex) {System.err.println("Client : getData IO error -> "+ ex.getMessage());}
        return null;
    }
    @Override
    public int disconnect() 
    {
        System.out.println("Client: disconnect executed");
        return JHelp.DISCONNECT;    
    }
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        String terms="";
        switch (ae.getActionCommand())
        {
            case "Find":
                terms = termText.getText();
                System.out.println(termText.getText());
                if (connect() == JHelp.OK) 
                {
                    run(terms);
                    disconnect();
                }
                break;
                
            case "Insert":
                terms = termText.getText();
                System.out.println(termText.getText());
                if (connect() == JHelp.OK) 
                {
                    run(terms);
                    disconnect();
                }
                break;
            case "Delete":
                terms = termText.getText();
                System.out.println(termText.getText());
                if (connect() == JHelp.OK) 
                {
                    run(terms);
                    disconnect();
                }
                break;
            case "Update":
                terms = termText.getText();
                System.out.println(termText.getText());
                if (connect() == JHelp.OK) 
                {
                    run(terms);
                    disconnect();
                }
                break;
            case "Exit":
                disconnect();
                System.exit (0);     
                break;
        }
    }
    @Override
    public void windowOpened(WindowEvent we) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void windowClosing(WindowEvent we) 
    {
        disconnect();
        System.exit (0);
        System.out.println("Client : Closing");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void windowClosed(WindowEvent we) 
    {
        System.out.println("Client : Closed");
//        disconnect();
//        System.exit (0);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void windowIconified(WindowEvent we) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void windowDeiconified(WindowEvent we) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void windowActivated(WindowEvent we) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void windowDeactivated(WindowEvent we) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
