package com.gamedev;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;

public class TokenReaderClass {

    public String ReadToken() {
        String tokenAsStr = null;
        try {
            tokenAsStr = System.getenv("TOKEN");
            System.out.println("I WAS UPDATED")
        }
        catch (SecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return tokenAsStr;
    }
}
