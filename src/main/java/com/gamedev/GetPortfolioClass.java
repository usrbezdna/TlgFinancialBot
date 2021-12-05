package com.gamedev;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class GetPortfolioClass {

    public static Double getStockPriceUSD(String stockName) throws IOException {
        Stock stockObj = YahooFinance.get(stockName);

        double currencyRatio;
        if (Objects.equals(stockObj.getCurrency(), "USD")) {
            currencyRatio = 1.0;
        } else {
            Stock currencyObject = YahooFinance.get(stockObj.getCurrency() + "=X");
            while (currencyObject == null){
                currencyObject = YahooFinance.get(stockObj.getCurrency() + "=X");
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            }


            currencyRatio = currencyObject.
                                getQuote().
                                getPrice().
                                doubleValue();
        }
        return stockObj.getQuote().getPrice().doubleValue() / currencyRatio;
    }

    public static HashMap<String, Double> calcPortfolio(Map<String, String> rawPortfolio) {
        HashMap<String, Double> processedPortfolio = new HashMap<String, Double>();

        for (Map.Entry<String, String> stock : rawPortfolio.entrySet()) {
            try {
                processedPortfolio.put(stock.getKey(), 
                    Integer.parseInt(stock.getValue()) * getStockPriceUSD(stock.getKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processedPortfolio;
    }

    public static SendMessage calcPortfolioBalance(SendMessage message, String chat_id, Map<String,
                                                    String> portfolio, Boolean hasCallback)
    {
        Double balance = 0.0;  StringBuilder details = new StringBuilder(); message.setChatId(chat_id);

        if (portfolio == null) {
            message.setText(balance.toString());
            return message;
        }

        HashMap<String, Double> calculated = calcPortfolio(portfolio);

        for (Map.Entry<String, Double> stock : calculated.entrySet()) {
            balance += stock.getValue();
            if (hasCallback)
                details.append(stock.getKey()).append(" - ").append(stock.getValue().toString()).append("\n");
        }

        double result = Math.floor(balance);

        if (hasCallback) {
            details.insert(0, "$" + result + "\n");
            message.setText(details.toString());
        } else {
            message.setText("$" + result);
        }
        return message;
    }
}