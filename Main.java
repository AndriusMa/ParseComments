package com.library;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        ParseFile parser = new ParseFile();
        try {
            parser.getFileName();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
