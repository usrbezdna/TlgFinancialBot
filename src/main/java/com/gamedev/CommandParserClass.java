package com.gamedev;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

public class CommandParserClass {

    public interface MethodRunner {
        void run(CommandContainer comCont);
    }

    public static final HashMap<String, MethodRunner> CommandList = new HashMap<String, MethodRunner>() {{
        put("/start", args -> CommandExecutorClass.start());
        put("/help", args -> CommandExecutorClass.help());
        put("/pie", args -> CommandExecutorClass.pie(false));
        put("/npie", args -> CommandExecutorClass.pie(true));

        put("/balance", CommandExecutorClass::balance);
        put("/add", CommandExecutorClass::add);
        put("/remove", CommandExecutorClass::remove);
        put("/price", CommandExecutorClass::price);
    }};

    public static void parse(Update update) {

        String[] input;
        String chat_id;
        String msg_id = null;
        boolean flagCB = false;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();

            input = callbackQuery.getData().split("\\s");
            chat_id = message.getChatId().toString();
            msg_id = message.getMessageId().toString();
            flagCB = true;

        } else {
            CommandExecutorClass.updateChatID(update);
            input = update.getMessage().getText().split("\\s");
            chat_id = update.getMessage().getChatId().toString();
        }
        CommandContainer comCont = new CommandContainer(input, flagCB, chat_id, msg_id);
        startExecution(comCont);
    }

    private static void startExecution (CommandContainer comCont) {
        if (CommandList.containsKey(comCont.getCommand()))
            try {
                CommandList.get(comCont.getCommand()).run(comCont);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
