package dk.cphbusiness.networkDemosTests;

import dk.cphbusiness.demo03_httprequest.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class Demo03HttpServer
{
    
    private static final int PORT = 9090;
    private static final String IP = "127.0.0.1";
    private static final String LOCAL_HOST = "http://localhost:";
    private static final String URL = "http://localhost:" + PORT;
    private static URL url;
    
    private static HttpServer httpServer = new HttpServer();
    
    private static String response;
    
    private static HttpURLConnection httpURLConnection;
    
    
    @BeforeAll
    public static void setup()
    {
        System.out.println( "setup" );
    }
    
    @BeforeEach
    public void setupEach() throws IOException
    {
        System.out.println( "setupEach" );
        new Thread( () -> httpServer.startConnection( PORT ) ).start();
        
        try {
            url = new URL( URL );
        } catch ( MalformedURLException e ) {
            throw new RuntimeException( e );
        }
        httpURLConnection = new HttpURLConnection( url )
        {
            /**
             * Opens a communications link to the resource referenced by this
             * URL, if such a connection has not already been established.
             * <p>
             * If the {@code connect} method is called when the connection
             * has already been opened (indicated by the {@code connected}
             * field having the value {@code true}), the call is ignored.
             * <p>
             * URLConnection objects go through two phases: first they are
             * created, then they are connected.  After being created, and
             * before being connected, various options can be specified
             * (e.g., doInput and UseCaches).  After connecting, it is an
             * error to try to set them.  Operations that depend on being
             * connected, like getContentLength, will implicitly perform the
             * connection, if necessary.
             *
             * @throws SocketTimeoutException if the timeout expires before
             *                                the connection can be established
             * @throws IOException            if an I/O error occurs while opening the
             *                                connection.
             * @see URLConnection#connected
             * @see #getConnectTimeout()
             * @see #setConnectTimeout(int)
             */
            @Override
            public void connect() throws IOException
            {

//                response = this.responseMessage;
                
            }
            
            /**
             * Indicates that other requests to the server
             * are unlikely in the near future. Calling disconnect()
             * should not imply that this HttpURLConnection
             * instance can be reused for other requests.
             */
            @Override
            public void disconnect()
            {
            
            }
            
            /**
             * Indicates if the connection is going through a proxy.
             * <p>
             * This method returns {@code true} if the connection is known
             * to be going or has gone through proxies, and returns {@code false}
             * if the connection will never go through a proxy or if
             * the use of a proxy cannot be determined.
             *
             * @return a boolean indicating if the connection is using a proxy.
             */
            @Override
            public boolean usingProxy()
            {
                return false;
            }
        };
        httpURLConnection.connect();
    }
    
    @AfterEach
    public void tearDown()
    {
        System.out.println( "tearDownEach" );
        httpServer.stopConnection();
    }
    
    @Test
    void messageTest() throws IOException
    {
        Socket clientSocket = new Socket( IP, PORT );
        PrintWriter out = new PrintWriter( clientSocket.getOutputStream(), true );
        BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
        
        System.out.println(in.readLine());
    }
    
}
