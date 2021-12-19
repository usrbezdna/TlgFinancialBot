package commands;

import architecture.BasicCommand;
import architecture.CommandContainer;
import utils.StockAPI;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public class Price extends BasicCommand {
    public static SendMessage getPrice(CommandContainer comCont) {

        SendMessage message = new SendMessage();
        message.setChatId(comCont.getChatID());

        Double stockPrice = StockAPI.getStockPriceUSD(comCont.getArgument());
        message.setText(String.format("Found ticker with price %s", stockPrice));
        return message;
    }

    @Override
    public int getNumberOfArgs() {
        return 1;
    }

    @Override
    public void validateArgs(CommandContainer comCont) {
        String ticker = comCont.getArgument();
        Double stockPrice = StockAPI.getStockPriceUSD(ticker);
        if (stockPrice == null)
            comCont.setError("Invalid ticker. Please make sure that spelling is correct.");
    }
}
