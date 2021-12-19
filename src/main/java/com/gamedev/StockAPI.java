package com.gamedev;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StockAPI {
    private static final Logger logger = LoggerFactory.getLogger(StockAPI.class);

    public static Double getStockPriceUSD(String stockName){
        Stock stockObj = getStock(stockName);
        if (stockObj == null || stockObj.getCurrency() == null) { return null; }
        double currencyRatio = 1.0;
        if (!Objects.equals(stockObj.getCurrency(), "USD")) {
            Stock currencyObject = null;
            while (currencyObject == null) {
                currencyObject = getStock(stockObj.getCurrency() + "=X");
            }
            currencyRatio = currencyObject.getQuote().getPrice().doubleValue();
        }
        return Math.round(stockObj.getQuote().getPrice().doubleValue() / currencyRatio * 100.0) / 100.0;
    }

    private static Stock getStock(String stockName){
        try {
            return YahooFinance.get(stockName);
        } catch (IOException e) { logger.error("Got IOException from exchange API", e);}
        return new Stock(stockName);
    }

    public static String getCompanyName(String stockName) {
        Stock stockObj = getStock(stockName);
        return stockObj.getName();
    }
}
