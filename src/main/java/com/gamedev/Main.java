package com.gamedev;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    private static final BotClass bot = new BotClass();
    public static BotClass getBot() { return bot; }

    public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
        try {
            JedisHandler.init();
            CommandParserClass.initializeCommands();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("UPDATED");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
