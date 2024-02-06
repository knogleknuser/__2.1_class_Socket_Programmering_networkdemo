package dk.cphbusiness.demo01_singlerequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Purpose of this demo is to show the most basic use of sockets with inspiration
 * from: https://www.baeldung.com/a-guide-to-java-sockets
 * The server only accepts one client and only one message from the client before
 * closing the connection
 * Author: Thomas Hartmann and Jon Bertelsen
 */
public class SimpleServer
{
    
    private static final int PORT = 9090;
    
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    public static void main( String[] args )
    {
        SimpleServer server = new SimpleServer();
        server.startConnection( PORT );
    }
    
    public void startConnection( int port )
    {
        try ( ServerSocket serverSocket = new ServerSocket( port ) ) {
            this.clientSocket = serverSocket.accept(); // blocking call
            
            this.out = new PrintWriter( this.clientSocket.getOutputStream(), true );
            
            this.in = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
            
            String greeting = this.in.readLine();
            
            System.out.println( greeting );
            this.out.println( "Hello SimpleClient, Greetings from SimpleServer" );
            
        } catch ( IOException e ) {
            e.printStackTrace();
            
        } finally {
            this.stopConnection();
        }
    }
    
    public void stopConnection()
    {
        try {
            System.out.println( "Closing down socket ..." );
            
            this.in.close();
            this.out.close();
            this.clientSocket.close();
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
}
