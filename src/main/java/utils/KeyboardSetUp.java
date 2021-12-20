package utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.stream.*;

public class KeyboardSetUp {
    public static ReplyKeyboardMarkup setReplyKeyboard(){

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow rows = new KeyboardRow();

        List<String> commands = Stream.of("/help", "/pie", "/balance", "/portfolioNews", "/npie")
                .collect(Collectors.toList());
        commands.forEach(rows::add); keyboard.add(rows);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public static InlineKeyboardMarkup setInlineKeyboard (HashMap<String, String> callBackCommands) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        int counter = 1;
        for (HashMap.Entry<String, String> element : callBackCommands.entrySet())
        {

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(element.getKey());
            button.setCallbackData(element.getValue());

            rowInline.add(button);counter++;

            if (counter % 2 == 0) {
                rowsInline.add(rowInline);
                rowInline =  new ArrayList<>();
            }

        }
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
