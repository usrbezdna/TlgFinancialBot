package com.gamedev;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    private static final BotClass bot = new BotClass();
    public static BotClass getBot() { return bot; }

    public static void main(String[] args) {
        try {
            JedisHandler.auth();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
