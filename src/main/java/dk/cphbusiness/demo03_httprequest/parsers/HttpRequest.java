package dk.cphbusiness.demo03_httprequest.parsers;

import dk.cphbusiness.demo01_singlerequest.SimpleClient;
import dk.cphbusiness.demo03_httprequest.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class HttpRequest
{
    
    public static void main( String[] args )
    {
        HttpServer httpServer = new HttpServer();
        new Thread( () -> httpServer.startConnection( 9090 ) ).start();
        
        HttpRequest httpRequest = new HttpRequest( "127.0.0.1", 9090 );
        
        Map< String, String > headerMap = httpRequest.parse();
        
        for ( Map.Entry< String, String > entry : headerMap.entrySet() ) {
            System.out.print( "[ " + entry.getKey() + " ]" + " : " + "[ " + entry.getValue() + "]" );
        }
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
        
        this.parse();
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
    
    public void requestResponse()
    {
        ArrayList< String > lines = new ArrayList<>();
        
        int i = 0;
        String line;
        
        try {
            
            while ( ( line = this.in.readLine() ) != null && i < 2000000000 ) {
                i++;
                lines.add( line );
            }
            
        } catch ( IOException ignored ) {
            System.out.println( "IO Exception - but who cares?" );
        }
        
        this.response = String.join( System.lineSeparator(), lines );
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
        this.startConnection();
        this.requestResponse();
        
        Map< String, String > headerMap = new LinkedHashMap<>();
        StringTokenizer stringTokenizer = new StringTokenizer( this.response );
        
        String key;
        String value;
        
        try {
            
            headerMap.put( "Header", stringTokenizer.nextToken( System.lineSeparator() ) );
            
            while ( true ) {
                key = stringTokenizer.nextToken( ": " );
                try {
                    value = stringTokenizer.nextToken( System.lineSeparator() );
                    headerMap.put( key, value );
                } catch ( NoSuchElementException e ) {
                    value = stringTokenizer.nextToken();
                    headerMap.put( key, value );
                }
            }
            
        } catch ( NoSuchElementException ignored ) {
        
        }
        
        this.stopConnection();
        return headerMap;
    }
    
}
