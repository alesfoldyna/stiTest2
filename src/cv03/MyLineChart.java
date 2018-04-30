/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tul.sti.sem;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
/**
 *
 * @author alesf_000
 */
public class MyLineChart {
    private final ArrayList<Integer> data;
    private final String title;
    private final String stageTitle;
    private final String portfolio;

    public MyLineChart(ArrayList<Integer> data, String title, String stageTitle, String portfolio) {
        this.data = data;
        this.title = title;
        this.stageTitle = stageTitle;
        this.portfolio = portfolio;
    }
    
    
    
    
    public void printChart() {
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("icon/github.png")));
        
        int[] list = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            list[i] = data.get(i);
        }
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Commits");
        yAxis.setLabel("Řádky");
        xAxis.setTickUnit(1);
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(data.size()+1);
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) < min) {
                min = data.get(i);
            }
            if (data.get(i) > max) {
                max = data.get(i);
            }
        }
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(min - 10);
        yAxis.setUpperBound(max + 10);
        yAxis.setTickUnit(10);
        
        final LineChart lineChart = new LineChart(
                xAxis,yAxis,
                FXCollections.observableArrayList(
                        new XYChart.Series(
                                portfolio,
                                FXCollections.observableArrayList(
                                        plot(list)
                                )
                        )
                )
        );
        lineChart.setCursor(Cursor.CROSSHAIR);

        lineChart.setTitle(title);

        stage.setScene(new Scene(lineChart, 800, 600));
        stage.show();
    }

    /**
     * @param y
     * @return plotted y values for monotonically increasing integer x values,
     * starting from x=1
     */
    public ObservableList<XYChart.Data<Integer, Integer>> plot(int... y) {
        final ObservableList<XYChart.Data<Integer, Integer>> dataset = FXCollections.observableArrayList();
        int i = 0;
        while (i < y.length) {
            final XYChart.Data<Integer, Integer> dataOB = new XYChart.Data<>(i + 1, y[i]);
            dataOB.setNode(
                    new HoveredThresholdNode(
                            (i == 0) ? 0 : y[i - 1],
                            y[i]
                    )
            );

            dataset.add(dataOB);
            i++;
        }

        return dataset;
    }

    /**
     * a node which displays a value on hover, but is otherwise empty
     */
    class HoveredThresholdNode extends StackPane {

        HoveredThresholdNode(int priorValue, int value) {
            setPrefSize(10, 10);

            final Label label = createDataThresholdLabel(priorValue, value);

            setOnMouseEntered((MouseEvent mouseEvent) -> {
                getChildren().setAll(label);
                setCursor(Cursor.NONE);
                toFront();
            });
            setOnMouseExited((MouseEvent mouseEvent) -> {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
            });
        }

        private Label createDataThresholdLabel(int priorValue, int value) {
            final Label label = new Label(value + "");
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

            /*if (priorValue == 0) {
                label.setTextFill(Color.DARKGRAY);
            } else if (value > priorValue) {
                label.setTextFill(Color.FORESTGREEN);
            } else {
                label.setTextFill(Color.FIREBRICK);
            }*/
            label.setTextFill(Color.FORESTGREEN);
            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }
}
