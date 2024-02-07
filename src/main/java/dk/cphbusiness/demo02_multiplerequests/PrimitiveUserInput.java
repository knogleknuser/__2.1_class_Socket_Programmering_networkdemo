package dk.cphbusiness.demo02_multiplerequests;

import java.util.Scanner;

public class PrimitiveUserInput
{
    private static final Scanner scanner = new Scanner( System.in );
    
    public static String getStringFromKeyboard()
    {
        System.out.print( "Your Input : " );
        
        return scanner.nextLine();
    }
    
}
