package com.gamedev;

import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.HashMap;

public class CommandParserClass {

    private static final HashMap<String, Commands> CommandList = new HashMap<>();

    public static void parseCommand(Update update){
        String text = update.getMessage().getText();

        if (CommandList.containsKey(text)){
            CommandExecutorClass.setChat_id(update);
            try {
                CommandList.get(text).doCommand();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initializeCommands(){
        CommandList.put("/start", CommandExecutorClass::start);
        CommandList.put("/help", CommandExecutorClass::help);
        CommandList.put("/pie", CommandExecutorClass::pie);
    }

    public interface Commands {
        void doCommand();
    }
}
