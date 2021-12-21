package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class JedisHandler {
    private static final Logger logger = LoggerFactory.getLogger(JedisHandler.class);

    private static final String IP =  EnvVarReader.ReadEnvVar("DB_IP");
    private static final Integer PORT =  Integer.parseInt(EnvVarReader.ReadEnvVar("DB_PORT"));
    private static final String DB_PASS =  EnvVarReader.ReadEnvVar("DB_PASS");

    private static final Jedis productionDB = new Jedis(IP, PORT);

    public static Jedis getProductionDB(){
        return  productionDB;
    }

    public static void auth(){
        try {
            productionDB.auth(DB_PASS);
        } catch (Exception e)  {
            logger.error("Error authenticating the database", e);
        }
    }

    public static Map<String, Integer> getUserData (String chat_id, Jedis dataBase) {
        try{
            return new ObjectMapper().readValue(dataBase.get(chat_id), new TypeReference<Map<String, Integer>>(){});
        } catch (Exception e){
            logger.error("Error in getting the user information", e);
            return null;
        }
    }

    public static void setUserData (String chat_id, Map<String, Integer> data, Jedis dataBase) {
        try {
            dataBase.set(chat_id, new ObjectMapper().writeValueAsString(data));
        } catch (JsonProcessingException e){ 
            logger.error("Error in writing users information to database", e); 
        }
    }

    public static void removeAll(String chatID, Jedis dataBase){
        setUserData(chatID, new HashMap<>(), dataBase);
    }
}
