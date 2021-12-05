package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class ReturningValues {

    public SendMessage _message_;
    public SendPhoto _photo_;
    public EditMessageText _edited_message_;

    public ReturningValues(SendMessage message, SendPhoto photo, EditMessageText edited_message){
        this._message_ = message;
        this._photo_ = photo;
        this._edited_message_ = edited_message;
    }

    public ReturningValues(SendMessage message, SendPhoto photo){
        this._message_ = message;
        this._photo_ = photo;
    }

    public ReturningValues(SendMessage message){
        this._message_ = message;
    }

    public ReturningValues(SendPhoto photo){
        this._photo_ = photo;
    }

    public ReturningValues(EditMessageText editMessageText){
        this._edited_message_ = editMessageText;
    }
}
