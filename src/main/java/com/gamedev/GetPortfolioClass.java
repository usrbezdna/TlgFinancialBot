/*package com.gamedev;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class GetPortfolioClass {

    public Stock getStock(String stockName) throws IOException {
        return YahooFinance.get(stockName);
    }

    public HashMap<String, Double> calcPortfolio(HashMap<String, Integer> rawPortfolio) {
        HashMap<String, Double> processedPortfolio = new HashMap<String, Double>();
        for (Map.Entry<String, Integer> stock : rawPortfolio.entrySet()) {
            try {
                processedPortfolio.put(stock.getKey(), stock.getValue() *
                        getStock(stock.getKey()).
                                getQuote().
                                getPrice().
                                doubleValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processedPortfolio;
    }

    public Double calcPortfolioBalance(HashMap<String, Double> portfolio) {
        Double balance = 0.0;
        for (Map.Entry<String, Double> stock : portfolio.entrySet()) {
            balance += stock.getValue();
        }
        return balance;
    }
}*/