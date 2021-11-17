package com.gamedev;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;

public final class TokenReaderClass {

    public String ReadToken() {
        String tokenAsStr = null;
        try {
            tokenAsStr = "2071564236:AAF_8jccvRxYHuLhxAKysQuwVfezV-nL96c";
        }
        catch (SecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return tokenAsStr;
    }
}
