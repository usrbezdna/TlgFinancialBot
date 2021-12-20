package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.JedisHandler;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Bot bot = new Bot();
    public static Bot getBot() { return bot; }

    public static void main(String[] args) {
        try {
            JedisHandler.auth();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (Exception e) {
            logger.error("Error in setting up bot's api", e);
        }
    }
}
