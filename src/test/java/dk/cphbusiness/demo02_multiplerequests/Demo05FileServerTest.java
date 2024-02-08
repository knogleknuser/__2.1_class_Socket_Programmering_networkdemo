package dk.cphbusiness.demo02_multiplerequests;

import dk.cphbusiness.demo05_fileserver.HttpRequest;
import dk.cphbusiness.demo05_fileserver.RequestFileClient;
import dk.cphbusiness.demo05_fileserver.RequestFileServer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Purpose of this demo is to show how to get a html file from the server.
 * Author: Thomas Hartmann and Jon Bertelsen
 */
class Demo05FileServerTest
{
    
    private static final int PORT = 9090;
    private static final String IP = "127.0.0.1";
    
    private static RequestFileServer rfs = new RequestFileServer();
    
    @BeforeAll
    public static void setup()
    {
        System.out.println( "setup" );
        
    }
    
    @BeforeEach
    public void setupEach()
    {
        System.out.println( "setupEach" );
        new Thread( () -> rfs.startConnection( PORT ) ).start();
    }
    
    @AfterEach
    public void tearDown()
    {
        System.out.println( "tearDownEach" );
        rfs.stopConnection();
    }
    
    @Test
    @DisplayName( "Test getting a file from the server" )
    public void testGettingFileFromServer()
    {
        String shttpRequest =
                "GET /pages/index.html HTTP/1.1" + System.lineSeparator() +
                        "Host: " + IP + System.lineSeparator() +
                        "Content-Type: text/html; charset=UTF-8" + System.lineSeparator() +
                        "Content-Length: 87" + System.lineSeparator() +
                        "Connection: close" + System.lineSeparator();
        
        HttpRequest httpRequest = new HttpRequest( IP, PORT );
        
        httpRequest.parse( shttpRequest );
        
        String expected = "<html><head><title>hello world</title></head><body><h1>Hello World</h1></body></html>";
        assertEquals( expected, httpRequest.getLastParsedHttpRequest().get( "Body" ) );
    }
    
    @Test
    @DisplayName( "Test getting a file from the server without index.html" )
    public void testGettingFileFromServerNoHtml()
    {
        String shttpRequest =
                "GET /pages HTTP/1.1" + System.lineSeparator() +
                        "Host: " + IP + System.lineSeparator() +
                        "Content-Type: text/html; charset=UTF-8" + System.lineSeparator() +
                        "Content-Length: 87" + System.lineSeparator() +
                        "Connection: close" + System.lineSeparator();
        
        HttpRequest httpRequest = new HttpRequest( IP, PORT );
        
        httpRequest.parse( shttpRequest );
        
        String expected = "<html><head><title>hello world</title></head><body><h1>Hello World</h1></body></html>";
        assertNotNull( httpRequest.getLastParsedHttpRequest() );
        assertNotEquals( 0, httpRequest.getLastParsedHttpRequest().size() );
        
        assertTrue( httpRequest.getLastParsedHttpRequest().containsKey( "Body" ) );
        assertEquals( expected, httpRequest.getLastParsedHttpRequest().get( "Body" ) );
    }
    
}