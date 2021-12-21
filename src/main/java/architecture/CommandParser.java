package architecture;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import redis.clients.jedis.Jedis;
import utils.JedisHandler;

import java.util.HashMap;

public class CommandParser {
    public interface MethodRunner {
        void run(CommandContainer comCont);
    }

    public static final HashMap<String, MethodRunner> CommandList = new HashMap<String, MethodRunner>() {{
        put("/start", args -> CommandExecutor.start());
        put("/help", args -> CommandExecutor.help());
        put("/pie", conCont -> CommandExecutor.pie(conCont, false));
        put("/npie", conCont -> CommandExecutor.pie(conCont,true));


        put("/removeAll", CommandExecutor::removeAll);
        put("/portfolioNews", CommandExecutor::getPortfolioNews);
        put("/balance", CommandExecutor::balance);
        put("/add", CommandExecutor::add);
        put("/remove", CommandExecutor::remove);
        put("/price", CommandExecutor::price);
        put("/news", CommandExecutor::getNews);
    }};

    public static void parse(Update update) {

        String[] input;
        String chat_id;
        String msg_id = null;
        boolean flagCB = false;
        Jedis prodDB = JedisHandler.getProductionDB();

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
        CommandContainer comCont = new CommandContainer(input, flagCB, chat_id, msg_id, prodDB);
        if (comCont.hasError()) CommandExecutor.printError(comCont);
        else startExecution(comCont);
    }

    private static void startExecution (CommandContainer comCont) {
        CommandList.get(comCont.getCommand()).run(comCont);
    }
}
