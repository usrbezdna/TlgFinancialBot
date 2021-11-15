import org.jfree.chart.ChartFactory;
import java.io.*;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;


//allocations - pie diagram
//dataset#1 hashMap[key:String(ticker), value:BigDecimal(price)]
//dataset hashMap[key:String(ticker), value:BigDecimal(total)]

public class DiagramClass {
    private static PieDataset createDataset(){
        DefaultPieDataset test = new DefaultPieDataset();
        test.setValue( "Sector 1" , 20.056 );
        test.setValue( "Sector 2" , 36.6798 );
        test.setValue( "Sector 3" , 40.097854 );
        test.setValue( "Sector 4" , 10.4534555 );
        return test;
    }

    private static JFreeChart createChart( PieDataset dataset ) {
        return ChartFactory.createPieChart(
                "Test Diagram",   // chart title
                dataset,          // data
                true,             // include legend
                false,
                false);
    }


    static void CreateDiagram() {
        int width = 800;
        int height = 600;
        File buffer = new File("TD.jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(buffer, createChart(createDataset()), width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
