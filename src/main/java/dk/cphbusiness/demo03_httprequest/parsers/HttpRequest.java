package dk.cphbusiness.demo03_httprequest.parsers;

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
        
        HttpRequest httpRequest = new HttpRequest( "127.0.0.1", 9090 ); //These two are identical
//        HttpRequest httpRequest = new HttpRequest( "localhost", 9090 );       //These two are identical
        
        httpRequest.parse();
        
        httpRequest.printLastParsedHttpRequest();
        
        
    }
    
    private final String ip;
    private final int port;
    
    private Socket clientSocket;
    private PrintWriter outPw;
    private BufferedReader inBr;
    
    private String responseString = null;
    private String[] responseLines = null;
    private Map< String, String > lastParsedHttpRequest = null;
    
    public HttpRequest( String ip, int port )
    {
        this.ip = ip;
        this.port = port;
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
            this.outPw = new PrintWriter( this.clientSocket.getOutputStream(), true );
            this.inBr = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
            
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
            
            while ( ( line = this.inBr.readLine() ) != null && i < 2000000000 ) {
                i++;
                lines.add( line );
            }
            
        } catch ( IOException ignored ) {
            System.out.println( "IO Exception - but who cares?" );
        }
        
        this.responseLines = lines.toArray( new String[ 0 ] );
        this.responseString = String.join( System.lineSeparator(), lines );
    }
    
    public void stopConnection()
    {
        try {
            System.out.println( "Closing down client socket" );
            
            this.inBr.close();
            this.outPw.close();
            this.clientSocket.close();
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public Map< String, String > parse()
    {
        this.startConnection();
        this.requestResponse();
        
        if ( this.responseString == null ) {
            return null;
        }
        
        Map< String, String > headerMap = new LinkedHashMap<>();
        
        if ( this.responseString.isEmpty() ) {
            return headerMap;
        }
        
        String key;
        String value;
        int bodyCounter = 1;
        
        try {
            
            int lastReadLine = 0;
            
            //Header
            headerMap.put( "Header", this.responseLines[ lastReadLine++ ] );
            
            for ( int i = lastReadLine; i < this.responseLines.length; i++ ) {
                
                if ( !this.responseLines[ i ].contains( ":" ) ) {
                    lastReadLine = i;
                    break;
                }
                
                int colonIndex = this.responseLines[ i ].indexOf( ':' );
                
                key = this.responseLines[ i ].substring( 0, colonIndex ).trim(); //Header Type is everything before colon
                value = this.responseLines[ i ].substring( colonIndex + 1 ).trim(); //The details are everything after
                
                lastReadLine = i;
                
                headerMap.put( key, value );
            }
            
            //We have now reached the empty line before the body, we are not putting that in the map
            lastReadLine++;
            
            //Body
            for ( int i = lastReadLine; i < this.responseLines.length; i++ ) {
                
                if ( !headerMap.containsKey( "Body" ) ) {
                    key = "Body";
                    
                } else {
                    key = "Body" + bodyCounter++;
                }
                value = this.responseLines[ i ].trim();
                
                headerMap.put( key, value );
            }
            
        } catch ( NoSuchElementException ignored ) {
        
        }
        
        this.stopConnection();
        
        this.lastParsedHttpRequest = headerMap;
        
        return this.lastParsedHttpRequest;
    }
    
    public String getResponseString()
    {
        return this.responseString;
    }
    
    public String[] getResponseLines()
    {
        return this.responseLines;
    }
    
    public Map< String, String > getLastParsedHttpRequest()
    {
        return this.lastParsedHttpRequest;
    }
    
    public void printLastParsedHttpRequest()
    {
        if ( this.lastParsedHttpRequest == null ) {
            return;
        }
        
        for ( Map.Entry< String, String > entry : this.lastParsedHttpRequest.entrySet() ) {
            System.out.println( "[ " + entry.getKey() + " ]" + " : " + "[ " + entry.getValue() + " ]" );
        }
        
    }
    
}
