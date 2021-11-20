package com.gamedev;

import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.HashMap;

public class CommandParserClass {

    public static final HashMap<String, MethodRunner> CommandList = new HashMap<String, MethodRunner> ();

    public static void parseCommand(Update update){
        CommandExecutorClass.updateChatID(update);
        String[] input = update.getMessage().getText().split(" ");
        String data = "";  String command = input[0];
        try {
             data = input[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (CommandList.containsKey(command)){
            try {
                CommandList.get(command).run(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initializeCommands(){
        CommandList.put("/start", arg -> CommandExecutorClass.start());
        CommandList.put("/help", arg -> CommandExecutorClass.help());
        CommandList.put("/pie", arg -> CommandExecutorClass.pie());
        CommandList.put("/price", arg -> CommandExecutorClass.price((String)arg));
    }

    public interface MethodRunner {
        void run(Object arg);
    }
}
