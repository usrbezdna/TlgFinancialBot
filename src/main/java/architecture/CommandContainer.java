package architecture;

import commands.*;
import redis.clients.jedis.Jedis;

import java.util.HashMap;


public class CommandContainer
{

    private String argument;
    private String data;
    private String errorMessage;

    private final String command;
    private final String chat_id;
    private final String msg_id;
    private final Jedis dataBase;


    private final boolean callbackFlag;
    private boolean errorFlag = false;


    public CommandContainer(String[] input, Boolean callbackFlag, String chat_id, String msg_id, Jedis db) {
        this.msg_id = msg_id;
        this.callbackFlag = callbackFlag;
        this.chat_id = chat_id;
        this.dataBase = db;

        this.command = input[0];
        String invalidArgument = "Incorrect number of arguments, try \"/help\"";

        if (CommandParser.CommandList.containsKey(this.command)) {
            switch (testMap.get(this.command).getNumberOfArgs()) {
                case 0: if (this.hasCallback()) { this.argument = input[1]; }
                        else if (input.length > 1){this.setError("This command does not take any arguments");}
                        break;
                case 1: if (input.length == 2) { this.argument = input[1]; }
                        else this.setError(invalidArgument);
                        break;
                case 2: if (input.length == 3) { this.argument = input[1]; this.data = input[2]; }
                        else this.setError(invalidArgument);
                        break;
                default: this.setError(invalidArgument);
            }
        } else
            this.setError("Incorrect command, try \"/help\"");

        if (!this.hasError())
            testMap.get(this.command).validateArgs(this);
    }

    private static final HashMap<String, BasicCommand> testMap = new HashMap<String, BasicCommand>() {{
        put("/start", new Start());
        put("/help", new Help());
        put("/pie", new Pie());
        put("/npie", new Pie());
        put("/removeAll", new Help());

        put("/balance", new Balance());
        put("/add", new Add());
        put("/remove", new Remove());
        put("/price", new Price());
        put("/portfolioNews", new PortfolioNews());
        put("/news", new News());
    }};


    public String getCommand() {
        return this.command;
    }

    public String getArgument() {
        return this.argument;
    }

    public String getData() {
        return this.data;
    }

    public String getChatID() {
        return this.chat_id;
    }

    public String getMsgId() {
        return this.msg_id;
    }

    public boolean hasCallback() {
        return this.callbackFlag;
    }

    public boolean hasError() {
        return this.errorFlag;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

    public Jedis getDataBase() { return this.dataBase; }

    public void setError(String error){
        this.errorFlag = true;
        this.errorMessage = error;
    }

}