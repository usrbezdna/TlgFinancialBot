package com.gamedev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Bot bot = new Bot();
    public static Bot getBot() { return bot; }

    public static void main(String[] args) {
        try {
            System.out.println("PORT is ");
            System.out.println(EnvVarReader.ReadEnvVar("DB_PORT"));
            System.out.println( Integer.parseInt(EnvVarReader.ReadEnvVar("DB_PORT")));
            JedisHandler.auth();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (Exception e) {
            logger.error("Error in setting up bot's api", e);
        }
    }
}
