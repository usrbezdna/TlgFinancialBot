package com.gamedev;

public class CommandContainer
{
    private String command;
    private String argument;
    private String data;

    private String chat_id;
    private String msg_id;

    private boolean callbackFlag;

    public CommandContainer(String[] input, Boolean callbackFlag, String chat_id, String msg_id) {
        try {
            this.msg_id = msg_id;
            this.callbackFlag = callbackFlag;
            this.chat_id = chat_id;
            this.command = input[0];
            this.argument = input[1];
            this.data = input[2];
        } catch (Exception ignored) {}
    }

    public CommandContainer(String[] input, String chat_id) {
        try {
            this.chat_id = chat_id;
            this.command = input[0];
            this.argument = input[1];
            this.data = input[2];
        } catch (Exception ignored) {}
    }

    public String getCommand() {
        return this.command;
    }

    public String getArgument() {
        return this.argument;
    }

    public String getData() {
        return this.data;
    }

    public boolean hasCallback() {
        return callbackFlag;
    }

    public String getChatID() {
        return this.chat_id;
    }

    public String getMsgId() {
        return this.msg_id;
    }
}