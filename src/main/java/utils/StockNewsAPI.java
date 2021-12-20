package utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jzhangdeveloper.newsapi.net.NewsAPI;
import com.jzhangdeveloper.newsapi.net.NewsAPIClient;
import com.jzhangdeveloper.newsapi.net.NewsAPIResponse;
import com.jzhangdeveloper.newsapi.params.EverythingParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockNewsAPI {
    static NewsAPIClient client = NewsAPI.newClientBuilder()
                                            .setApiKey(EnvVarReader.ReadEnvVar("NEWS_TOKEN"))
                                            .build();

    private static final Logger logger = LoggerFactory.getLogger(StockNewsAPI.class);

    private static JsonObject getTopNews(String query) throws IOException, InterruptedException {
        Map<String, String> everythingParams = EverythingParams.newBuilder()
                                                .setSearchQueryInTitle(query)
                                                .setSortBy("publishedAt")
                                                .setLanguage("en")
                                                .build();

        NewsAPIResponse response = client.getEverything(everythingParams);
        logger.error(String.valueOf(response.getStatusCode()));

        return response.getBodyAsJson();
    }

    private static String parseArticles(JsonObject obj) {
        StringBuilder msg = new StringBuilder();
        int counter = 0;
        final int maxArticles = 5;

        JsonArray articles = obj.getAsJsonArray("articles");
        for (JsonElement art: articles){
            JsonObject jsonArt = (JsonObject) art;
            JsonObject source = jsonArt.getAsJsonObject("source");
            JsonElement name = source.get("name");
            JsonElement title = jsonArt.get("title");
            JsonElement url = jsonArt.get("url");
            for (JsonElement element : Arrays.asList(name, title, url)) {
                String strElem = element.toString();
                strElem = strElem.substring(1, strElem.length()-1);
                msg.append(strElem).append("\n");
            }
            msg.append("===========================\n");
            if (counter == maxArticles)
                break;
            counter++;
        } 
        return msg.toString();
    }

    public static String getNewsMessage(String query) {
        try {
            return parseArticles(getTopNews(query));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return query;
    }
} 