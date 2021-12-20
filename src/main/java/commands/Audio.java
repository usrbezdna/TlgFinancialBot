package commands;

import java.io.ByteArrayInputStream;

import com.google.protobuf.ByteString;

import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import architecture.BasicCommand;
import architecture.CommandContainer;
import utils.SpeechAPI;

public class Audio extends BasicCommand {

    public static SendAudio sendAudio(CommandContainer comCont) {
        ByteString bytes = new ByteString(); 
        
        String txt = "This text must be read";
        try {
            bytes = SpeechAPI.textToSpeech(txt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendAudio audio = new SendAudio();
        audio.setChatId(comCont.getChatID());
        audio.setAudio(new InputFile(new ByteArrayInputStream(bytes.toByteArray()), "audio.mp3"));
        return audio;
    }

    @Override
    public int getNumberOfArgs() {
        return 0;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {        
    }
    
}
