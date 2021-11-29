package com.gamedev;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

public class JedisHandler {

    private static final String IP = "185.239.208.162";
    private static final Integer PORT = 6379;

    private static final String DB_PASS = new EnvVarReaderClass().ReadEnvVar("DB_PASS");
    private static final Jedis db = new Jedis(IP, PORT);
    
    public static void auth(){
        try {
            db.auth(DB_PASS);
        } catch (Exception e)  {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getUserData (String chat_id) throws JsonMappingException, JsonProcessingException {
        return new ObjectMapper().readValue(db.get(chat_id), new TypeReference<Map<String, String>>(){});
    }

    public static void setUserData (String chat_id, HashMap<String, String> data) throws JsonProcessingException {
        db.set(chat_id, new ObjectMapper().writeValueAsString(data));
    }
}
