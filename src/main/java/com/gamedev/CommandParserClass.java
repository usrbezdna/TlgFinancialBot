package com.gamedev;

import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.HashMap;

public class CommandParserClass {

    private static final HashMap<String, Commands> CommandList = new HashMap<String, Commands>();

    public static void parseCommand(Update update){
        String msg = update.getMessage().getText();
        if (CommandList.containsKey(msg)){
            CommandExecutorClass.setUpdate(update);
            try {
                CommandList.get(msg).doCommand();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initialize(){
        CommandList.put("/start", CommandExecutorClass::start);
        CommandList.put("/help", CommandExecutorClass::help);
        CommandList.put("/pie", CommandExecutorClass::pie);
    }

    public interface Commands {
        void doCommand();
    }
}
