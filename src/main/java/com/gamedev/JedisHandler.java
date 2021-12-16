package com.gamedev;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

public class JedisHandler {

    private static final String IP =  EnvVarReader.ReadEnvVar("DB_IP");
    private static final Integer PORT =  Integer.parseInt(EnvVarReader.ReadEnvVar("DB_PORT"));

    private static final String DB_PASS =  EnvVarReader.ReadEnvVar("DB_PASS");
    private static final Jedis db = new Jedis(IP, PORT);
    
    public static void auth(){
        try {
            db.auth(DB_PASS);
        } catch (Exception e)  {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> getUserData (String chat_id) {
        try{
            return new ObjectMapper().readValue(db.get(chat_id), new TypeReference<Map<String, Integer>>(){});
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void setUserData (String chat_id, Map<String, Integer> data) {
        try {
            db.set(chat_id, new ObjectMapper().writeValueAsString(data));
        } catch (JsonProcessingException e){ e.printStackTrace(); }
    }
}
