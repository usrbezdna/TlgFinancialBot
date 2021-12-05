package com.gamedev;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.HashMap;

public class CommandParserClass {

    public interface MethodRunner {
        void run(String[] s);
    }

    public static final HashMap<String, MethodRunner> CommandList = new HashMap<String, MethodRunner> ();

    public static void parseCommand(Update update) {
        String command = ""; String data = ""; String argument = "";

        CommandExecutorClass.updateChatID(update);

        String[] input = update.getMessage().getText().split(" ");
        try {
            command = input[0]; data = input[1]; argument = input[2];
        } catch (Exception ignored) {}

        String[] args = new String[] { data, argument };

        System.out.println(command);
        System.out.println(Arrays.toString(args));

        startExecution(command, args);
    }

    public static void parseCallback(Update update) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();

            String[] call_data = callbackQuery.getData().split(" ");
            String command = call_data[0]; String argument = call_data[1];

            String message_id = message.getMessageId().toString();
            String chat_id = message.getChatId().toString();

            String[] args = new String[] { "Callback", argument, message_id, chat_id };
            startExecution(command, args);
    }

    private static void startExecution (String command, String[] args){
        if (CommandList.containsKey(command)){
            try {
                CommandList.get(command).run(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void initializeCommands(){
        CommandList.put("/start", args -> CommandExecutorClass.start());
        CommandList.put("/help", args -> CommandExecutorClass.help());
        CommandList.put("/pie", args -> CommandExecutorClass.pie());

        CommandList.put("/balance", CommandExecutorClass::balance);
        CommandList.put("/add", CommandExecutorClass::add);
        CommandList.put("/price", CommandExecutorClass::price);
    }
}
