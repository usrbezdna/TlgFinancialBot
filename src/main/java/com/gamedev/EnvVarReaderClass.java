package com.gamedev;

public final class  EnvVarReaderClass {

    public String ReadEnvVar(String name) {
        String tokenAsStr = null;
        try {
            tokenAsStr = System.getenv(name);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return tokenAsStr;
    }
}
