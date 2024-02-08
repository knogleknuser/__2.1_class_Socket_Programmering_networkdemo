package dk.cphbusiness.demo04_httpparsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 * Purpose of this demo is to show how to get the data from the request headers etc.
 * This is a simple http server that can handle GET, POST, PUT and PATCH requests with a single client and only one message from the client before closing the connection
 * The server must therefore be restarted for each request.
 * Author: Thomas Hartmann and Jon Bertelsen
 */
public class RequestDataServer
{
    
    public static void main( String[] args )
    {
        RequestDataServer server = new RequestDataServer();
        server.startConnection( PORT );
    }
    
    
    
    
    private static final int PORT = 9090;
    
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String response = "";
    
    
    
    
    public void startConnection( int port )
    {
        try ( ServerSocket serverSocket = new ServerSocket( port ) ) {
            this.clientSocket = serverSocket.accept(); // blocking call
            this.out = new PrintWriter( this.clientSocket.getOutputStream(), true );
            this.in = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
            
            // Read the request and send it back to the client
            String response = this.generateRequestObject( this.in ).toString();
            this.out.println( response );
            
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
    
    public RequestDTO generateRequestObject( BufferedReader in )
    { // public because we want to use it in extensions of this class
        String requestLine = null; // GET /path/to/endpoint?queryparam1=9&queryparam2=18 HTTP/1.1
        
        Map< String, String > headers = null;
        Map< String, String > queryParams = null;
        Map< String, String > requestBodyData = new HashMap<>();
        
        RequestDTO requestDTO = new RequestDTO();
        
        try {
            StringBuilder requestBuilder = new StringBuilder();
            
            // Read the first line of the request line like: GET / HTTP/1.1 (or POST /path/to/ressource HTTP/1.1)
            requestLine = in.readLine();
            
            if ( requestLine == null || requestLine.isEmpty() ) {
                throw new IllegalArgumentException( "The request is lacking the request line and is therefore not a valid HTTP request" );
            }
            
            // Check if the request has more lines
            if ( !in.ready() ) {
                requestDTO.setRequestLine( requestLine );
                return requestDTO;
            }
            
            // Read the rest of the lines in the request: Headers and body (if any)
            String newLine;
            while ( in.ready() && ( newLine = in.readLine() ) != null && !newLine.isEmpty() ) {
                requestBuilder.append( newLine ).append( "\n" );
            }
            
            // Get the http headers from the request
            headers = this.getHeadersFromRequest( requestBuilder );
            
            // If the request is a POST request, read the body
            try {
                requestBodyData = this.getRequestBody( requestLine, requestBuilder );
                System.out.println( "Request body: " + requestBodyData );
                
            } catch ( IllegalArgumentException e ) {
                System.out.println( "Not a POST, PUT or PATCH request" );
            }
            
            // Parse query parameters
            queryParams = getQueryParameters( requestLine );
            
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        
        return new RequestDTO( requestLine, headers, queryParams, requestBodyData );
    }
    
    private Map< String, String > getHeadersFromRequest( StringBuilder requestBuilder )
    {
        Map< String, String > headers = new HashMap<>();
        
        // loop the requestBuilder until you find an empty line
        for ( String line : requestBuilder.toString().split( "\n" ) ) {
            
            if ( line.isEmpty() ) {
                break;
            }
            
            System.out.println( "Line: " + line );
            
            String[] parts = line.split( ":" );
            headers.put( parts[ 0 ], parts[ 1 ] );
        }
        
        return headers;
    }
    
    private Map< String, String > getRequestBody( String requestLine, StringBuilder requestBuilder ) throws IOException
    {
        if ( !( requestLine.contains( "POST" ) || requestLine.contains( "PUT" ) || requestLine.contains( "PATCH" ) ) ) {
            throw new IllegalArgumentException( "This request contains no body" );
        }
        
        Map< String, String > requestBodyFormParameters = new HashMap<>();
        
        StringBuilder requestBodyBuilder = new StringBuilder();
        int contentLength = getContentLength( requestBuilder.toString() );
        
        // contentLength tells us how many characters the body contains
        if ( contentLength > 0 ) {
            
            char[] buffer = new char[ contentLength ];
            this.in.read( buffer, 0, contentLength );
            requestBodyBuilder.append( buffer );
            
        } else {
            throw new IllegalArgumentException( "This request contains no body" );
        }
        
        String[] paramStrings = requestBodyBuilder.toString().split( "&" );
        
        for ( String paramString : paramStrings ) {
            String[] parts = paramString.split( "=" );
            requestBodyFormParameters.put( parts[ 0 ], parts[ 1 ] );
        }
        
        return requestBodyFormParameters;
    }
    
    private static int getContentLength( String request )
    {
        String[] lines = request.split( "\n" );
        
        for ( String line : lines ) {
            
            if ( line.startsWith( "Content-Length:" ) ) {
                return Integer.parseInt( line.substring( "Content-Length:".length() ).trim() );
            }
            
        }
        
        return 0;
    }
    
    private static Map< String, String > getQueryParameters( String requestLine )
    {
        Map< String, String > queryParams = new HashMap<>();
        
        if ( requestLine.split( " " ).length < 2 ) {    // if there is no path part, we could throw an exception since this is not a valid http request
            return queryParams;
        }
        
        String pathPart = requestLine.split( " " )[ 1 ]; // get the /path/to/endpoint?queryparam1=9&queryparam2=18 part
        
        if ( !pathPart.contains( "?" ) ) {  // if there are no query params return empty map
            return queryParams;
        }
        
        String queriesPart = pathPart.split( "\\?" )[ 1 ]; // get the queryparam1=9&queryparam2=18 part.
        
        // There is either one query param or more:
        String[] queries = queriesPart.contains( "&" ) ? queriesPart.split( "&" ) : new String[]{ queriesPart }; // get the queryparam1=9 and queryparam2=18 parts in a String array
        
        for ( int i = 0; i < queries.length; i++ ) {
            String[] keyValue = queries[ i ].split( "=" );
            queryParams.put( keyValue[ 0 ], keyValue[ 1 ] );
        }
        
        return queryParams;
    }
    
}
