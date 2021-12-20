package architecture;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class ReturningValues {

    public SendMessage _message_  = new SendMessage();
    public SendPhoto _photo_ = new SendPhoto();
    public EditMessageText _edited_message_ = new EditMessageText();
    public SendVoice _send_voice_ = new SendVoice();

    public ReturningValues(SendMessage message){
        this._message_ = message;
    }

    public ReturningValues(SendPhoto photo) { this._photo_ = photo; }

    public ReturningValues(EditMessageText editMessageText){
        this._edited_message_ = editMessageText;
    }

    public ReturningValues(SendVoice voice){
        this._send_voice_ = voice;
    }

    public ReturningValues(){}
}
