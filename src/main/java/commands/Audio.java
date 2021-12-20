package commands;

import java.io.ByteArrayInputStream;
import com.google.protobuf.ByteString;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import utils.SpeechAPI;


public class Audio {
    public static SendVoice returnAudio(String text) {

        ByteString bytes = null;
        try {
            bytes = SpeechAPI.textToSpeech(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendVoice audio = new SendVoice();
        audio.setVoice(new InputFile(new ByteArrayInputStream(bytes.toByteArray()), "audio.mp3"));
        return audio;
    }    
}
