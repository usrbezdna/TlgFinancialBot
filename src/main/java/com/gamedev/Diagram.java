package com.gamedev;

import org.jfree.chart.ChartFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class Diagram {
    public static ByteArrayOutputStream createDiagram(Map<String, Integer> map, Boolean numFlag) throws IOException{
        int width = 800; int height = 600;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (!numFlag) {
            ChartUtilities.writeChartAsJPEG(out, createChart(createPriceDataset(map)), width, height);
        } else ChartUtilities.writeChartAsJPEG(out, createChart(createNumDataset(map)), width, height);
        return out;
    }

    private static PieDataset createPriceDataset(Map<String, Integer> data){
        DefaultPieDataset dataset = new DefaultPieDataset();
        HashMap<String, Double> calculatedData = GetPortfolio.calcPortfolio(data);
        for (String key: calculatedData.keySet()){
            dataset.setValue(key, calculatedData.get(key));
        }
        return dataset;
    }

    private static PieDataset createNumDataset(Map<String, Integer> data){
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (String key: data.keySet()){
            dataset.setValue(key, data.get(key));
        }
        return dataset;
    }

    private static JFreeChart createChart( PieDataset dataset ) {
        return ChartFactory.createPieChart(
                "Your awesome portfolio",   // chart title
                dataset,          // data
                true,      // include legend
                false,
                false);
    }
}
