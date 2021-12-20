package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architecture.*;
import utils.*;

import utils.StockNewsAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import static java.lang.Math.toIntExact;

public class News extends BasicCommand {
    private static final Logger logger = LoggerFactory.getLogger(News.class);

    public static ReturningValues news(CommandContainer comCont, 
                                            SendMessage message, 
                                            EditMessageText edited_message) {
        if (comCont.hasCallback()) {
            int message_id = toIntExact(Long.parseLong(comCont.getMsgId()));
            String name = StockAPI.getCompanyName(comCont.getArgument());
            String txt = StockNewsAPI.getNewsMessage(name);

            edited_message.setChatId(comCont.getChatID());
            edited_message.setMessageId(message_id);
            edited_message.setText(txt);
            return new ReturningValues(edited_message);
        } else {
            message.setText("News");
            message.setChatId(comCont.getChatID());
            
            Map<String, Integer> portfolio = JedisHandler.getUserData(comCont.getChatID());
            List<String> tickers = new ArrayList<>(portfolio.keySet());

            InlineKeyboardMarkup keyboard = KeyboardSetUp.setInlineKeyboard(new HashMap<String, String>() {{
                for (String ticker: tickers) 
                    put(ticker, "/news " + ticker);
            }});
            message.setReplyMarkup(keyboard);
            return new ReturningValues(message);
        }
    }

    @Override
    public int getNumberOfArgs() {
        return 0;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {}
}
