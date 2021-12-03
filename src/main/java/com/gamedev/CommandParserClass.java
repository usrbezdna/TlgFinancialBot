package com.gamedev;

import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CommandParserClass {

    public interface MethodRunner {
        void run(String[] s);
    }

    public static final HashMap<String, MethodRunner> CommandList = new HashMap<String, MethodRunner> ();

    public static void parseCommand(Update update){
        String command = "";
        String data = "";
        String argument = "";
        boolean hasCallback = update.hasCallbackQuery();
        System.out.println("Has Callback " + update.hasCallbackQuery());
        if (hasCallback){
            System.out.println("I'm in");
            System.out.println("T " + update.getCallbackQuery().getData().split(Pattern.quote("__"))[0]);
            command = String.valueOf(update.getCallbackQuery().getData().split(Pattern.quote("__"))[0]);
            System.out.println("Command is " + command);
        } else {
            CommandExecutorClass.updateChatID(update);
            String[] input = update.getMessage().getText().split(" ");
            data = "";  command = input[0]; argument = "";
            try {
                data = input[1];
                argument = input[2];
           } catch (Exception ignored) {}
        }
        
        System.out.println("Command " + command);
        System.out.println("Has Callback " + hasCallback);

        if (CommandList.containsKey(command)){
            try {
                if (!hasCallback) {
                    System.out.println("No callback use");
                    String[] args = new String[] { data, argument };
                    CommandList.get(command).run(args);
                } else {
                    System.out.println("Going to callback");
                    String call_data = update.getCallbackQuery().getData();
                    String message_id = update.getCallbackQuery().getMessage().getMessageId().toString();
                    String chat_id = update.getCallbackQuery().getMessage().getChatId().toString();
                    String[] args = new String[] { "Callback", call_data, message_id, chat_id };
                    CommandList.get(command).run(args);
                }
                
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
