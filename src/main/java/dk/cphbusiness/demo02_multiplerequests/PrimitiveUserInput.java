package dk.cphbusiness.demo02_multiplerequests;

import java.util.Scanner;

public class PrimitiveUserInput
{
    private final Scanner scanner = new Scanner( System.in );
    
    public String getStringFromKeyboard()
    {
        System.out.print( "Your Input : " );
        
        return this.scanner.nextLine();
    }
    
}
