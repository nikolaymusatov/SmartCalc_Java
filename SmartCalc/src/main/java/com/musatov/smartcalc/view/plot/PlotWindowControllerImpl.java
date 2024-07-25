package com.musatov.smartcalc.view.plot;

import com.musatov.smartcalc.presenter.Presenter;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;


public class PlotWindowControllerImpl implements PlotWindowController {
    private Presenter presenter;
    
    private DecimalFormat decimalFormat;
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private TextField xMinField;
    
    @FXML
    private TextField xMaxField;
    
    @FXML
    private TextField yMinField;
    
    @FXML
    private TextField yMaxField;
    
    @FXML
    private TextField xValueField;
    
    @FXML
    private TextField resultField;
    
    @FXML
    private Button replotButton;
    
    @FXML
    private Button calculateButton;
    
    @FXML
    private LineChart<Number, Number> lineChart;
    
    @FXML
    private NumberAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;
    
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
    
    @Override
    public void showResult(double result) {
        resultField.setText(String.valueOf(decimalFormat.format(result)));
    }
    
    @FXML
    @Override
    public void showGraph(double[] xData, double[] yData, int size,
                          double xMin, double xMax) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < size; i++) {
            series.getData().add(new XYChart.Data<>(xData[i], yData[i]));
        }
        lineChart.getData().clear();
        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(xMin);
        xAxis.setUpperBound(xMax);
        lineChart.setVisible(true);
    }
    
    @Override
    public void resetSettings() {
        Set<Node> nodes = gridPane.lookupAll(".text-field");
        nodes.stream()
                .filter(node -> node instanceof TextField)
                .map(node -> (TextField) node)
                .forEach(TextInputControl::clear);
        xAxis.setLowerBound(-100);
        xAxis.setUpperBound(100);
        yAxis.setAutoRanging(true);
        lineChart.setVisible(false);
    }
    
    @FXML
    private void initialize() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("#.######", symbols);
        replotButton.setDisable(true);
        calculateButton.setDisable(true);
    }
    
    @FXML
    private void calculateButtonClick() {
        presenter.calculateValueFromX(Double.parseDouble(xValueField.getText()));
    }
    
    @FXML
    private void replotButtonClick() {
        lineChart.setVisible(false);
        if (!xMinField.getText().isEmpty()) {
            double xMin = Double.parseDouble(xMinField.getText());
            double xMax = Double.parseDouble(xMaxField.getText());
            presenter.recalculateGraph(xMin, xMax);
        }
        if (!yMinField.getText().isEmpty()) {
            double yMin = Double.parseDouble(yMinField.getText());
            double yMax = Double.parseDouble(yMaxField.getText());
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(yMin);
            yAxis.setUpperBound(yMax);
        }
        lineChart.setVisible(true);
    }
    
    @FXML
    private void xValueEntered() {
        calculateButton.setDisable(!xValueField.getText()
                .matches("-?\\d+([.]\\d+)?(e([-+])?\\d+)?"));
        if (xValueField.getText().isEmpty()) {
            resultField.clear();
        }
    }
    
    @FXML
    private void limitsChanged() {
        String xMin = xMinField.getText(), xMax = xMaxField.getText(),
                yMin = yMinField.getText(), yMax = yMaxField.getText();
        boolean[][] checkMatrix = new boolean[][] {
                {true, true, false, false},
                {false, false, true, true},
                {true, true, true, true}};
        boolean[] isEmptyMatrix = new boolean[] {
          !xMin.isEmpty(), !xMax.isEmpty(), !yMin.isEmpty(), !yMax.isEmpty()};
        for (int i = 0; i < 3; i++) {
            if (Arrays.equals(checkMatrix[i], isEmptyMatrix)) {
                if (i == 0 && Double.parseDouble(xMin) < Double.parseDouble(xMax)) {
                    replotButton.setDisable(false);
                } else if (i == 1 && Double.parseDouble(yMin) < Double.parseDouble(yMax)) {
                    replotButton.setDisable(false);
                } else {
                    replotButton.setDisable(i != 2 ||
                            !(Double.parseDouble(xMin) < Double.parseDouble(xMax)) ||
                            !(Double.parseDouble(yMin) < Double.parseDouble(yMax)));
                }
                break;
            } else {
                replotButton.setDisable(true);
            }
        }
    }
}
