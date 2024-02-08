package dk.cphbusiness.demo05_fileserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestFileClient
{
    public static void main( String[] args )
    {
        RequestFileClient client = new RequestFileClient();
        
        client.startConnection( IP, PORT );
        
        String httpRequest =
                "GET /pages/index.html HTTP/1.1" + System.lineSeparator() +
                        "Host: " + IP + System.lineSeparator() +
                        "User-Agent: SimpleWebClient" + System.lineSeparator() +
                        "Accept: */*" + System.lineSeparator() +
                        "Content-Type: application/x-www-form-urlencoded" + System.lineSeparator() +
                        "Content-Length: " + postData.length() + System.lineSeparator() +
                        "Connection: close" + System.lineSeparator();
        client.sendMessage( httpRequest );
        
        System.out.println( client.response );
        
        client.stopConnection();
    }
    
    
    private static final int PORT = 9090;
    private static final String IP = "127.0.0.1";
    
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String response = "";
    
    
    public void startConnection( String ip, int port )
    {
        try {
            System.out.println( "Starting client socket talking to server on IP: " + IP +
                    " and port number: " + PORT );
            
            this.clientSocket = new Socket( ip, port );
            this.out = new PrintWriter( this.clientSocket.getOutputStream(), true );
            this.in = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public void sendMessage( String msg )
    {
        try {
            this.out.println( msg );
            this.response = this.in.readLine();
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public void stopConnection()
    {
        try {
            System.out.println( "Closing down client socket" );
            
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
