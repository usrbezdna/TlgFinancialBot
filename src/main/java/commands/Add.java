package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import redis.clients.jedis.Jedis;
import utils.JedisHandler;
import utils.StockAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.HashMap;
import java.util.Map;

public class Add extends BasicCommand {
    private static final Logger logger = LoggerFactory.getLogger(Add.class);

    public static SendMessage addAsset (CommandContainer comCont){

        String chat_id = comCont.getChatID();
        Jedis dataBase = comCont.getDataBase();

        SendMessage message = new SendMessage();
        message.setChatId(chat_id);

        String ticker = comCont.getArgument();
        String amount = comCont.getData();

        Integer intAmount = Integer.parseInt(amount);

        ticker = ticker.toUpperCase();

        Map<String, Integer> userData = JedisHandler.getUserData(chat_id, dataBase);
        if (userData == null)
            userData = new HashMap<>();

        userData.merge(ticker, intAmount, Integer::sum);
        JedisHandler.setUserData(chat_id, userData, dataBase);

        message.setText(String.format("Added ticker %s with amount: %s", ticker, amount));
        return message;
    }

    private static boolean checkNumber(String amount) {
        int intAmount;
        try {
            intAmount = Integer.parseInt(amount);
            if (intAmount <= 0) throw new NumberFormatException();
            return true;
        } catch (NumberFormatException e){
            logger.warn("Couldn't parse " + amount + " as integer, returned error to user");
        } return false;
    }


    @Override
    public int getNumberOfArgs() {
        return 2;
    }

    @Override
    public void validateArgs (CommandContainer comCont) {
        String ticker = comCont.getArgument();
        String amount = comCont.getData();

        if (!checkNumber(amount))
            comCont.setError("Please specify correct amount");

        Double stockPrice = StockAPI.getStockPriceUSD(ticker);
        if (stockPrice == null) {
            comCont.setError("Invalid ticker. Please make sure that spelling is correct.");
        }
    }

}