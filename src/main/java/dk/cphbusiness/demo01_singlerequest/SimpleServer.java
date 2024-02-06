package dk.cphbusiness.demo01_singlerequest;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/*
 * Purpose of this demo is to show the most basic use of sockets with inspiration
 * from: https://www.baeldung.com/a-guide-to-java-sockets
 * The server only accepts one client and only one message from the client before
 * closing the connection
 * Author: Thomas Hartmann and Jon Bertelsen
 */
public class SimpleServer
{
    
    private static final int PORT = 9090;
    
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    public static void main( String[] args )
    {
        System.out.println();
        SimpleServer server = new SimpleServer();
        server.startConnection( PORT );
    }
    
    public void startConnection( int port )
    {
        try ( ServerSocket serverSocket = new ServerSocket( port ) ) {
            this.clientSocket = serverSocket.accept(); // blocking call
            
            this.out = new PrintWriter( this.clientSocket.getOutputStream(), true );
            
            this.in = new BufferedReader( new InputStreamReader( this.clientSocket.getInputStream() ) );
            
            String greeting = this.in.readLine();
            
            System.out.println( greeting );
            this.out.println( "Hello SimpleClient, Greetings from SimpleServer" );
            this.out.println( "The time is " + LocalTime.now() );
            
            this.out.println( this.getLocalTimePretty() );
//            this.out.println( this.getLocalTimePretty( 1, 1, 1, 1, 1 ) );
            
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
    
    private String getLocalTimePretty()
    {
        return this.getLocalTimePretty( new GregorianCalendar() );
    }
    
    private String getLocalTimePretty( int year, int month, int day, int hour, int minute )
    {
        return this.getLocalTimePretty( new GregorianCalendar( year, ( int ) ( month - 1 ), day, hour, minute ) );
    }
    
    private String getLocalTimePretty( @NotNull GregorianCalendar gregorianCalendar )
    {
        StringBuilder stringBuilder = new StringBuilder();
        
        //Night
        if ( gregorianCalendar.get( Calendar.HOUR_OF_DAY ) >= 22 || gregorianCalendar.get( Calendar.HOUR_OF_DAY ) < 5 ) {
            stringBuilder.append( "God nat til dig, det er kl: " ).append( String.format( "%02d", gregorianCalendar.get( Calendar.HOUR_OF_DAY ) ) ).append( ":" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.MINUTE ) ) ).append( ", burde du ikke være i seng? Dagens dato er " ).append( String.format( "%04d", gregorianCalendar.get( Calendar.YEAR ) ) ).append( "/" ).append( String.format( "%02d", ( gregorianCalendar.get( Calendar.MONTH ) + 1 ) ) ).append( "/" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.DAY_OF_MONTH ) ) );
            return stringBuilder.toString();
        }
        
        //Evening
        if ( gregorianCalendar.get( Calendar.HOUR_OF_DAY ) >= 17 ) {
            stringBuilder.append( "God aften til dig, det er kl: " ).append( String.format( "%02d", gregorianCalendar.get( Calendar.HOUR_OF_DAY ) ) ).append( ":" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.MINUTE ) ) ).append( ", og det er ved at være tid til at gå i seng. Dagens dato er " ).append( String.format( "%04d", gregorianCalendar.get( Calendar.YEAR ) ) ).append( "/" ).append( String.format( "%02d", ( gregorianCalendar.get( Calendar.MONTH ) + 1 ) ) ).append( "/" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.DAY_OF_MONTH ) ) );
            return stringBuilder.toString();
        }
        
        //Afternoon
        if ( gregorianCalendar.get( Calendar.HOUR_OF_DAY ) >= 12 ) {
            stringBuilder.append( "God eftermiddag til dig, det er kl: " ).append( String.format( "%02d", gregorianCalendar.get( Calendar.HOUR_OF_DAY ) ) ).append( ":" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.MINUTE ) ) ).append( ", og det er midt på dagen. Dagens dato er " ).append( String.format( "%04d", gregorianCalendar.get( Calendar.YEAR ) ) ).append( "/" ).append( String.format( "%02d", ( gregorianCalendar.get( Calendar.MONTH ) + 1 ) ) ).append( "/" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.DAY_OF_MONTH ) ) );
            return stringBuilder.toString();
        }
        
        //Before noon
        if ( gregorianCalendar.get( Calendar.HOUR_OF_DAY ) >= 10 ) {
            stringBuilder.append( "God formiddag til dig, det er kl: " ).append( String.format( "%02d", gregorianCalendar.get( Calendar.HOUR_OF_DAY ) ) ).append( ":" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.MINUTE ) ) ).append( ", og det er tideligt. Dagens dato er " ).append( String.format( "%04d", gregorianCalendar.get( Calendar.YEAR ) ) ).append( "/" ).append( String.format( "%02d", ( gregorianCalendar.get( Calendar.MONTH ) + 1 ) ) ).append( "/" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.DAY_OF_MONTH ) ) );
            return stringBuilder.toString();
        }
        
        //Morning
        if ( gregorianCalendar.get( Calendar.HOUR_OF_DAY ) >= 5 ) {
            stringBuilder.append( "God morgen til dig, det er kl: " ).append( String.format( "%02d", gregorianCalendar.get( Calendar.HOUR_OF_DAY ) ) ).append( ":" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.MINUTE ) ) ).append( ", og det er alt for tideligt! Dagens dato er " ).append( String.format( "%04d", gregorianCalendar.get( Calendar.YEAR ) ) ).append( "/" ).append( String.format( "%02d", ( gregorianCalendar.get( Calendar.MONTH ) + 1 ) ) ).append( "/" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.DAY_OF_MONTH ) ) );
            return stringBuilder.toString();
        }
        
        //Default
        stringBuilder.append( "God dag til dig, det er kl: " ).append( String.format( "%02d", gregorianCalendar.get( Calendar.HOUR_OF_DAY ) ) ).append( ":" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.MINUTE ) ) ).append( ", og du burde ikke se denne besked. Dagens dato er " ).append( String.format( "%04d", gregorianCalendar.get( Calendar.YEAR ) ) ).append( "/" ).append( String.format( "%02d", ( gregorianCalendar.get( Calendar.MONTH ) + 1 ) ) ).append( "/" ).append( String.format( "%02d", gregorianCalendar.get( Calendar.DAY_OF_MONTH ) ) );
        return stringBuilder.toString();
        
    }
    
}
