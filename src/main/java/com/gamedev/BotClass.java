package com.gamedev;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotClass extends TelegramLongPollingBot {

    private static final String TOKEN = new EnvVarReaderClass().ReadEnvVar("TOKEN");
    private static final String BOT_NAME = "AwesomeFinancialBot";

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        CommandParserClass.parseCommand(update);
    }

    void sendMessage(SendMessage message){
        try{
            execute(message);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    void updateMessage(EditMessageText message){
        try{
            execute(message);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    void sendPhoto(SendPhoto photo){
        try{
            execute(photo);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}