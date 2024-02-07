package dk.cphbusiness.networkDemosTests;

import dk.cphbusiness.demo03_httprequest.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Demo03HttpServer
{
    
    private static final int PORT = 9090;
    private static final String IP = "127.0.0.1";
    private static final String URL = "http://localhost:";
    
    private static HttpServer httpServer = new HttpServer();
    
    @BeforeAll
    public static void setup()
    {
        System.out.println( "setup" );
    }
    
    @BeforeEach
    public void setupEach()
    {
        System.out.println( "setupEach" );
        new Thread( () -> httpServer.startConnection( PORT ) ).start();
    }
    
    @AfterEach
    public void tearDown()
    {
        System.out.println( "tearDownEach" );
        httpServer.stopConnection();
    }
    
    @Test
    void main()
    {
    }
    
    @Test
    void startConnection()
    {
    }
    
    @Test
    void stopConnection()
    {
    }
    
}
