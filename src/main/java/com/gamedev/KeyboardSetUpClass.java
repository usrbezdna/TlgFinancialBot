package com.gamedev;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class KeyboardSetUpClass {
    public static ReplyKeyboardMarkup setKeyboard(){

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow rows = new KeyboardRow();

        List<String> commands = Stream.of("/help", "/pie", "/balance").collect(Collectors.toList());
        commands.forEach(rows::add); keyboard.add(rows);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
