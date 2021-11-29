package com.gamedev;

public final class EnvVarReaderClass {

    public String ReadEnvVar(String type) {
        String tokenAsStr = null;
        try {
            tokenAsStr = System.getenv(type);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return tokenAsStr;
    }
}
