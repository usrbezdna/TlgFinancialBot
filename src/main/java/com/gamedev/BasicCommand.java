package com.gamedev;

public abstract class BasicCommand {
    abstract public int getNumberOfArgs();
    abstract public void validateArgs(CommandContainer comCont);
}
