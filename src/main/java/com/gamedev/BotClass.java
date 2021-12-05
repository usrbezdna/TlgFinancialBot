package com.gamedev;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

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

        if (update.hasCallbackQuery())
            CommandParserClass.parseCallback(update);
        else
            CommandParserClass.parseCommand(update);
    }

    public <T> void sendEverything (T toBeSend) {
        try {
            if (toBeSend instanceof SendPhoto) {
                 execute((SendPhoto) toBeSend);
            } else if (toBeSend instanceof SendMessage) {
                execute((SendMessage) toBeSend);
            } else if (toBeSend instanceof EditMessageText){
                execute((EditMessageText) toBeSend);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}