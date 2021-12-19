package architecture;

import architecture.CommandContainer;
import architecture.CommandExecutor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

public class CommandParser {
    public interface MethodRunner {
        void run(CommandContainer comCont);
    }

    public static final HashMap<String, MethodRunner> CommandList = new HashMap<String, MethodRunner>() {{
        put("/start", args -> CommandExecutor.start());
        put("/help", args -> CommandExecutor.help());
        put("/pie", args -> CommandExecutor.pie(false));
        put("/npie", args -> CommandExecutor.pie(true));
        put("/removeAll", args -> CommandExecutor.removeAll());

        put("/balance", CommandExecutor::balance);
        put("/add", CommandExecutor::add);
        put("/remove", CommandExecutor::remove);
        put("/price", CommandExecutor::price);
    }};

    public static void parse(Update update) {

        String[] input;
        String chat_id;
        String msg_id = null;
        boolean flagCB = false;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();

            input = callbackQuery.getData().split("\\s");
            chat_id = message.getChatId().toString();
            msg_id = message.getMessageId().toString();
            flagCB = true;

        } else {
            CommandExecutor.updateChatID(update);
            input = update.getMessage().getText().split("\\s");
            chat_id = update.getMessage().getChatId().toString();
        }
        CommandContainer comCont = new CommandContainer(input, flagCB, chat_id, msg_id);
        if (comCont.hasError()) CommandExecutor.printError(comCont);
        else startExecution(comCont);
    }

    private static void startExecution (CommandContainer comCont) {
        CommandList.get(comCont.getCommand()).run(comCont);
    }
}
