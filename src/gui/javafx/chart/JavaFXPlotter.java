package gui.javafx.chart;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;

public class JavaFXPlotter {

    public static File toLineChart(String title, String seriesName, List<Integer> times, List<Integer> data) {

        File chartFile = new File("temp/lineCharts.png");

        // results: {completed, successful}
        Boolean[] results = new Boolean[]{false, false};

        SwingUtilities.invokeLater(() -> {

            // Initialize FX Toolkit
            new JFXPanel();

            Platform.runLater(() -> {
                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();

                final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

                lineChart.setTitle(title);

                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(seriesName);

                for (int i = 0; i < times.size(); i++)
                    series.getData().add(new XYChart.Data<Number, Number>(times.get(i), data.get(i)));

                lineChart.getData().add(series);

                Scene scene = new Scene(lineChart, 800, 600);

                WritableImage image = scene.snapshot(null);

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", chartFile);
                    results[1] = true;
                } catch (Exception e) {
                    results[0] = true;
                } finally {
                    results[0] = true;
                }
            });
        });

        while (!results[0]) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return (results[1]) ? chartFile : null;
    }


    public static void main(String[] args) {
        List<Integer> times = Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5});
        List<Integer> data = Arrays.asList(new Integer[]{4, 1, 5, 3, 0, 7});

        File lineChart = JavaFXPlotter.toLineChart("Sample", "Some sample data", times, data);

        if (lineChart != null)
            System.out.println("Image generation is done! Path: " + lineChart.getAbsolutePath());
        else
            System.out.println("File creation failed!");

        System.exit(0);
    }
}
