package commands;
import architecture.BasicCommand;
import architecture.CommandContainer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import utils.StockAPI;
import utils.StockNewsAPI;

public class News extends BasicCommand {

    public static SendMessage getNewsForTicker(CommandContainer comCont, SendMessage message){
        String msg = String.format("Latest news for ticker %s", comCont.getArgument()) +
                "\n\n" + "===========================\n" +
                StockNewsAPI.getNewsMessage(comCont.getArgument());
        message.setText(msg);
        message.setChatId(comCont.getChatID());
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
        if (stockPrice == null) {
            comCont.setError("Invalid ticker. Please make sure that spelling is correct.");
        }
    }
}
