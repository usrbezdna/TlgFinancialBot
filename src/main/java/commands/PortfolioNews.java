package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import architecture.ReturningValues;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import utils.JedisHandler;
import utils.KeyboardSetUp;
import utils.StockAPI;
import utils.StockNewsAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Math.toIntExact;

public class PortfolioNews extends BasicCommand {

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
            message.setText("PortfolioNews");
            message.setChatId(comCont.getChatID());
            
            Map<String, Integer> portfolio = JedisHandler.getUserData(comCont.getChatID());
            if (portfolio == null) {
                message.setText("Something went wrong with database, please try later");
                return new ReturningValues(message);
            }
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
    public void validateArgs(CommandContainer comCont) {
        Map<String, Integer> userPortfolio = JedisHandler.getUserData(comCont.getChatID());
        if (userPortfolio == null || userPortfolio.isEmpty()){
            comCont.setError("Your portfolio is empty");
        }
    }
}
