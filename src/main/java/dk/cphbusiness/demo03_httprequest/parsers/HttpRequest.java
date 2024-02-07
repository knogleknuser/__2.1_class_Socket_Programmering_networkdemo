package dk.cphbusiness.demo03_httprequest.parsers;

import dk.cphbusiness.demo01_singlerequest.SimpleClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class HttpRequest
{
    
    public static void main( String[] args )
    {
        HttpRequest httpRequest = new HttpRequest( "localhost", 9090 );
    }
    
    private final String ip;
    private final int port;
    
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String response = "";
    
    public HttpRequest( String ip, int port )
    {
        this.ip = ip;
        this.port = port;
        
        this.startConnection();
        
    }
    
    public void startConnection()
    {
        this.startConnection( this.ip, this.port );
    }
    
    public void startConnection( String ip, int port )
    {
        try {
            System.out.println( "Starting client socket talking to server on IP: " +
                    ip + " and port number: " + port );
            this.clientSocket = new Socket( ip, port );
            this.out = new PrintWriter( this.clientSocket.getOutputStream(), true );
            this.in = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public void request()
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
    
    public Map< String, String > parse()
    {
    
    }
    
}
