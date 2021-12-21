package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import architecture.ReturningValues;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import utils.GetPortfolio;
import utils.JedisHandler;
import utils.KeyboardSetUp;

import java.util.HashMap;

import static java.lang.Math.toIntExact;

public class Balance extends BasicCommand {
    public static ReturningValues getBalance(CommandContainer comCont, SendMessage message, EditMessageText edited_message){
        SendMessage totalPrice;
        String chat_id = comCont.getChatID();
        if (comCont.hasCallback()) {
            int message_id = toIntExact(Long.parseLong(comCont.getMsgId()));
            if (comCont.getArgument().equals("detailed")) {

                GetPortfolio
                        .calcPortfolioBalance(message, chat_id, JedisHandler
                                .getUserData(chat_id, comCont.getDataBase()), true);

                edited_message.setChatId(chat_id);
                edited_message.setMessageId(message_id);
                edited_message.setText(message.getText());

                return new ReturningValues(edited_message);
            }
        }
        else {
            totalPrice = GetPortfolio
                    .calcPortfolioBalance(message, chat_id, JedisHandler
                            .getUserData(chat_id, comCont.getDataBase()), false);

            InlineKeyboardMarkup keyboard = KeyboardSetUp.setInlineKeyboard(new HashMap<String, String>() {{
                put("Show detailed portfolio", "/balance detailed");
            }});

            message.setReplyMarkup(keyboard);
            message.setChatId(chat_id);
            return new ReturningValues(totalPrice);
        }
        return new ReturningValues();
    }

    @Override
    public int getNumberOfArgs() {
        return 0;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {}
}
