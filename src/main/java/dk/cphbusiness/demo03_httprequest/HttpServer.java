package dk.cphbusiness.demo03_httprequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The purpose of this demo is to show a simple http request and response
 * The request should be sent from a browser
 * Author: Jon Bertelsen
 */

public class HttpServer
{
    
    private static final int PORT = 9090;
    
    private Socket clientSocket;
    private PrintWriter out;
    
    public static final String responseHeader = "HTTP/1.1 200 OK" + System.lineSeparator() +
            "Date: Mon, 23 May 2022 22:38:34 GMT" + System.lineSeparator() +
            "Server: Apache/2.4.1 (Unix)" + System.lineSeparator()+
            "Content-Type: text/html; charset=UTF-8" + System.lineSeparator() +
            "Content-Length: 200" + System.lineSeparator() +
            "Connection: close" + System.lineSeparator();
    
    public static final String responseBody =
            "<html>" +
                    "<head>" +
                    "<title>" +
                    "hello world why do you suck so much" +
                    "</title>" +
                    "</head>" +
                    "<body>" +
                    "<h1>" +
                    "hello world why do you suck so much" +
                    "</h1>" +
                    "<p>" +
                    "hello world why do you suck so much" +
                    "</p>" +
                    "</body>" +
                    "</html>";
    
    public static void main( String[] args )
    {
        HttpServer server = new HttpServer();
        
        System.out.println( "Now get this webpage from a browser, tiger!" );
        System.out.println( "On http://localhost:" + PORT );
        
        server.startConnection( PORT );
    }
    
    public void startConnection( int port )
    {
        try ( ServerSocket serverSocket = new ServerSocket( port ); ) {
            
            this.clientSocket = serverSocket.accept(); // wait for client request
            this.out = new PrintWriter( this.clientSocket.getOutputStream(), true );
            
            
            String responseHeader = HttpServer.responseHeader;
            
            
            String responseBody =HttpServer.responseBody;
            
            this.out.print( responseHeader );
            this.out.print( System.lineSeparator() );
//            this.out.println( System.lineSeparator() + System.lineSeparator() ); // separate header and payload section
            this.out.print( responseBody );
            
            
            
            
            
        } catch ( IOException e ) {
            System.out.println( "An error has occured during network I/O" );
            throw new RuntimeException( e );
            
        }
        finally {
            this.stopConnection();
        }
        
    }
    
    public void stopConnection()
    {
        try {
            System.out.println( "Closing down client socket" );
            this.out.close();
            this.clientSocket.close();
            
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
}
