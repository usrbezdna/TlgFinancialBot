import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class BotClass extends TelegramLongPollingBot {

    private static final String TOKEN = new TokenReaderClass().ReadToken();
    private static final String BOT_NAME = "AwesomeFinancialBot";

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() { return TOKEN; }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            String chat_id = String.valueOf(update.getMessage().getChatId());
            switch (message_text) {
                case "/start" -> {
                    ReplyKeyboardMarkup keyboard = SetUpKeyboard();
                    SendMessage message = new SendMessage();
                    message.setChatId(chat_id);
                    message.setText("Started! Try \"/help\" for commands.");
                    message.setReplyMarkup(keyboard);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                case "/help" -> {
                    SendMessage message = new SendMessage();
                    message.setChatId(chat_id);
                    message.setText("This is useless help. We have \"/pie\" - makes a test " +
                            "pie diagram, \"/help\" - helps and \"/start\" - starts and opens " +
                            "inline keyboard. Try them all :)");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                case "/pie" -> {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chat_id);
                    sendPhoto.setCaption("This diagram was made by a very impolite and hateful " +
                            "but utterly handsome programmer. Enjoy!");
                    try {
                        sendPhoto.setPhoto(new InputFile(new File("FJ.jpeg")));
                    } catch (Exception e) {
                        DiagramClass.CreateDiagram();
                        sendPhoto.setPhoto(new InputFile(new File("FJ.jpeg")));
                    }
                    try {
                        execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ReplyKeyboardMarkup SetUpKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/help");
        row.add("/pie");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}