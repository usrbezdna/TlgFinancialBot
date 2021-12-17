package com.gamedev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class EnvVarReader {

    private static final Logger logger = LoggerFactory.getLogger(EnvVarReader.class);

    public static String ReadEnvVar(String name) {
        String tokenAsStr = null;
        try {
            tokenAsStr = System.getenv(name);
            System.out.println(tokenAsStr);
        }
        catch (SecurityException e) {
            logger.error(name + " environment variables wasn't found");
            System.exit(1);
        }
        return tokenAsStr;
    }
}
