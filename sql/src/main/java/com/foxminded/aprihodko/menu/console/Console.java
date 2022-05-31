package com.foxminded.aprihodko.menu.console;

import java.io.Closeable;

public interface Console extends Closeable{
    
    String EOL = "\n";
    
    void print(String text);
    
    void println(String text);
    
    String askForString(String prompt);
    
    default Integer askForInteger(String prompt) {
        do {
            String input = askForString(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                println("Wrong input '" + input + "'");
            }
        } while (true);
    }
    
    default Integer askForInteger(String prompt, int min, int max) {
        do {
            int input = askForInteger(String.format("%s(%d...%d)", prompt, min, max));
            if (input >= min && input <= max) {
                return input;
            }
            println(String.format("Wrong input %d, expected(%d...%d)", input, min, max));
        } while (true);
    }
}
