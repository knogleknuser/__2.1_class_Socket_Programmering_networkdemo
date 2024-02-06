package dk.cphbusiness.demo02_multiplerequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient
{
    
    private static final int PORT = 9090;
    private static final String IP = "127.0.0.1";
    
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String response;
    
    public static void main( String[] args )
    {
        EchoClient client = new EchoClient();
        client.startConnection( IP, PORT );
        
        client.sendMessage( "Hello SimpleServer" );
        System.out.println( "Response 1: " + client.response );
        
        client.sendMessage( "Second message" );
        System.out.println( "Response 2: " + client.response );
        
        client.sendMessage( "bye" );
        System.out.println( client.response );
        
        client.stopConnection();
    }
    
    public void startConnection( String ip, int port )
    {
        try {
            this.clientSocket = new Socket( ip, port );
            this.out = new PrintWriter( this.clientSocket.getOutputStream(), true );
            this.in = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    void sendMessage( String msg )
    {
        try {
            this.out.println( msg );
            this.response = this.in.readLine();
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    void stopConnection()
    {
        try {
            System.out.println( "Closing down echo client connection" );
            this.in.close();
            this.out.close();
            this.clientSocket.close();
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public String getResponse()
    {
        return this.response;
    }
    
}
