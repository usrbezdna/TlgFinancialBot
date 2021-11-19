package com.gamedev;

public final class TokenReaderClass {

    public String ReadToken() {
        String tokenAsStr = null;
        try {
            tokenAsStr = System.getenv("TOKEN");
        }
        catch (SecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return tokenAsStr;
    }
}
