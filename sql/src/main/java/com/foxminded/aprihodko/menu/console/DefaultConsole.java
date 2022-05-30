package com.foxminded.aprihodko.menu.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class DefaultConsole implements Console{
    
    private final InputStream in;
    private final PrintStream out;
    private final Scanner scanner;
    
    public DefaultConsole(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
        scanner = new Scanner(in);
    }
    
    @Override
    public void print(String text) {
        out.print(text);
        
    }

    @Override
    public void println(String text) {
        print(text);
        print(EOL);
        
    }

    @Override
    public String askForString(String prompt) {
        print(prompt + ":");
        return scanner.nextLine();
    }
    
    @Override
    public void close() throws IOException {
        scanner.close();
    }
}
