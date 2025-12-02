package software.ulpgc.kata6.application;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import software.ulpgc.kata6.architecture.viewmodel.Histogram;

import javax.swing.*;
import java.awt.*;

public class Desktop extends JFrame {

    private Desktop() throws HeadlessException {
        this.setTitle("Histogram");
        this.setResizable(false);
        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);
    }

    public static Desktop create() {
        return new Desktop();
    }

    public Desktop display(Histogram histogram) {
        this.getContentPane().add(chartPanelWith(histogram));
        return this;
    }

    private ChartPanel chartPanelWith(Histogram histogram) {
        return new ChartPanel(chartWith(histogram));
    }

    private JFreeChart chartWith(Histogram histogram) {
        return ChartFactory.createHistogram(
                histogram.title(),
                histogram.x(),
                histogram.y(),
                datasetWith(histogram)
        );
    }

    private XYSeriesCollection datasetWith(Histogram histogram) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesIn(histogram));
        return dataset;
    }

    private XYSeries seriesIn(Histogram histogram) {
        XYSeries series = new XYSeries(histogram.legend());
        for (int bin : histogram) {
            series.add(bin, histogram.count(bin));
        }
        return series;
    }
}
