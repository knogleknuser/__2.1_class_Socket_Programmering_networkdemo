package dk.cphbusiness.demo02_multiplerequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/*
 * Purpose of this demo is to show how to read the client request and send it back from the server
 * This is a TCP server example (not HTTP, which means it is not a web server and cannot work with a browser)
 * Author: Thomas Hartmann and Jon Bertelsen
 **/
public class EchoServer
{
    
    private static final int PORT = 9090;
    
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    public static void main( String[] args )
    {
        EchoServer server = new EchoServer();
        server.startConnection( PORT );
    }
    
    /*
     * Purpose of this demo is to show how to accept multiple requests from the client
     * while keeping the connection open
     */
    public void startConnection( int port )
    {
        
        try ( ServerSocket serverSocket = new ServerSocket( port ) ) {
            this.clientSocket = serverSocket.accept(); // blocking call
            this.out = new PrintWriter( this.clientSocket.getOutputStream(), true );
            this.in = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
            
            String inputLine;
            
            do {
                inputLine = this.in.readLine();
                
                if ( "bye".equals( inputLine ) ) {
                    this.out.println( "Good bye ... closing down" );
                    
                } else if ( inputLine != null ) {
                    this.out.println( getRandomResponse() + inputLine );
                }
                
            } while ( inputLine != null && !inputLine.equals( "bye" ) );
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
            
        } finally {
            this.stopConnection();
        }
    }
    
    private static String getRandomResponse()
    {
        String[] randomStrings = {
                "Din mor: ",
                "Fucking nice at du sagde ",
                "Mhmmhm mere? Oh ja du sagde: ",
                "Du elsker echoer ikke? Det håber jeg! Echo: "
        };
        
        Random random = new Random();
        
        return randomStrings[ random.nextInt( randomStrings.length ) ];
    }
    
    public void stopConnection()
    {
        try {
            System.out.println( "Closing Echo server socket down ...." );
            this.in.close();
            this.out.close();
            this.clientSocket.close();
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
}
