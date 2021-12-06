package com.gamedev;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.toIntExact;

public class GetBalanceClass {
    public static ReturningValues getBalance(String[] args, SendMessage message, EditMessageText edited_message,
                                  String chat_id){
        SendMessage totalPrice;
        if (Objects.equals(args[0], "Callback")) {
            int message_id = toIntExact(Long.parseLong(args[2]));
            if (args[1].equals("detailed")) {

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
                put("Show hello message", "/help");
                put("Some text", "some random text for msg");
            }});

            message.setReplyMarkup(keyboard);
            message.setChatId(chat_id);
            return new ReturningValues(totalPrice);
        }
        return new ReturningValues();
    }
}
