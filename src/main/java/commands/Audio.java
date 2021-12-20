package commands;

import java.io.ByteArrayInputStream;

import com.google.protobuf.ByteString;

import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import architecture.BasicCommand;
import architecture.CommandContainer;
import utils.SpeechAPI;

public class Audio extends BasicCommand {

    public static SendVoice sendAudio(CommandContainer comCont) {

        ByteString bytes = null;
        String text = comCont.getArgument();
        String convertedText = text.replaceAll("_", " ");

        System.out.println(convertedText);

        try {
            bytes = SpeechAPI.textToSpeech(convertedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendVoice audio = new SendVoice();
        audio.setChatId(comCont.getChatID());

        audio.setVoice(new InputFile(new ByteArrayInputStream(bytes.toByteArray()), "audio.mp3"));

        return audio;
    }

    @Override
    public int getNumberOfArgs() {
        return 1;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {}
    
}
