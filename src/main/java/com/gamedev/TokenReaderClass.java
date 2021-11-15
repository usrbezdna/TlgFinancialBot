package com.gamedev;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;

public class TokenReaderClass {

    public String ReadToken() {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("token.txt");
        String tokenAsStr;

        try {
             tokenAsStr = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
             tokenAsStr = "Error";
            e.printStackTrace();
        }
        return tokenAsStr;
    }
}
