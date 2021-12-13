package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.toIntExact;

public class GetBalanceClass {
    public static ReturningValues getBalance(CommandContainer comCont, SendMessage message, EditMessageText edited_message,
                                  String chat_id){
        SendMessage totalPrice;
        if (comCont.hasCallback()) {
            int message_id = toIntExact(Long.parseLong(comCont.getMsgId()));
            if (comCont.getArgument().equals("detailed")) {

                GetPortfolioClass
                        .calcPortfolioBalance(message, chat_id, JedisHandler
                                .getUserData(chat_id), true);

                edited_message.setChatId(chat_id);
                edited_message.setMessageId(message_id);
                edited_message.setText(message.getText());

                return new ReturningValues(edited_message);
            }
        }
        else {
            totalPrice = GetPortfolioClass
                    .calcPortfolioBalance(message, chat_id, JedisHandler
                            .getUserData(chat_id), false);

            InlineKeyboardMarkup keyboard = KeyboardSetUpClass.setInlineKeyboard(new HashMap<String, String>() {{
                put("Show detailed portfolio", "/balance detailed");
            }});

            message.setReplyMarkup(keyboard);
            message.setChatId(chat_id);
            return new ReturningValues(totalPrice);
        }
        return new ReturningValues();
    }
}
