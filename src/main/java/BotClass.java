import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class BotClass extends TelegramLongPollingBot {

    private static final String TOKEN  = new TokenReaderClass().ReadToken();
    private static final String BOT_NAME = "AwesomeFinancialBot";

    @Override
    public String getBotUsername () {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived (Update update) {
        if (update.getMessage () !=null && update.getMessage ().hasText()) {
            long chat_id = update.getMessage().getChatId();
            try {
                execute(new SendMessage (String.valueOf(chat_id), "Hi" + update.getMessage().getText()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}