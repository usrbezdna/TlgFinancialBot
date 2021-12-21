package utils;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import java.io.IOException;


public class SpeechAPI {
    private final VoiceSelectionParams voice;
    private final AudioConfig audioConfig;
    public ByteString textToSpeech(String textToSpeech) throws IOException {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {

            SynthesisInput input = SynthesisInput.newBuilder().setText(textToSpeech).build();
            SynthesizeSpeechResponse response =
                textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            return response.getAudioContent();
        }
    }

    public SpeechAPI(){
         this.voice =
                VoiceSelectionParams.newBuilder()
                        .setLanguageCode("en-US")
                        .setSsmlGender(SsmlVoiceGender.FEMALE)
                        .build();

         this.audioConfig =
                AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
    }

}
