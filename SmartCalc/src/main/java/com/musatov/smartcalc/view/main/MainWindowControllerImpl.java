package com.musatov.smartcalc.view.main;

import com.musatov.smartcalc.SmartCalcApplication;
import com.musatov.smartcalc.presenter.Presenter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainWindowControllerImpl implements MainWindowController {
    private static Presenter presenter;
    
    private static String historyPath;
    
    private static ListView<String> staticHistoryList;
    
    private Stage plotStage;
    
    private DecimalFormat decimalFormat;
    
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private TextField inputField;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private ListView<String> historyList;
    
    @FXML
    private Button mulButton;
    
    @FXML
    private Button minusButton;
    
    @FXML
    private Button plusButton;
    
    @FXML
    private Button resultButton;
    
    public static void saveHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyPath))) {
            for (int i = 0; !staticHistoryList.getItems().isEmpty()
                    && i < staticHistoryList.getItems().size(); i++) {
                writer.write(staticHistoryList.getItems().get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public static void deleteModel() {
        presenter.deleteModel();
    }
    
    @Override
    public void setPresenter(Presenter presenter) {
        MainWindowControllerImpl.presenter = presenter;
    }
    
    @Override
    public void setPlotStage(Stage plotStage) {
        this.plotStage = plotStage;
    }
    
    @Override
    public void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
    
    @Override
    public void showResult(double result) {
        inputField.setText(String.valueOf(decimalFormat.format(result)));
    }
    
    @FXML
    private void button0Click() {
        inputField.setText(inputField.getText() + "0");
    }
    
    @FXML
    private void button1Click() {
        inputField.setText(inputField.getText() + "1");
    }
    
    @FXML
    private void button2Click() {
        inputField.setText(inputField.getText() + "2");
    }
    
    @FXML
    private void button3Click() {
        inputField.setText(inputField.getText() + "3");
    }
    
    @FXML
    private void button4Click() {
        inputField.setText(inputField.getText() + "4");
    }
    
    @FXML
    private void button5Click() {
        inputField.setText(inputField.getText() + "5");
    }
    
    @FXML
    private void button6Click() {
        inputField.setText(inputField.getText() + "6");
    }
    
    @FXML
    private void button7Click() {
        inputField.setText(inputField.getText() + "7");
    }
    
    @FXML
    private void button8Click() {
        inputField.setText(inputField.getText() + "8");
    }
    
    @FXML
    private void button9Click() {
        inputField.setText(inputField.getText() + "9");
    }
    
    @FXML
    private void dotButtonClick() {
        inputField.setText(inputField.getText() + ".");
    }
    
    @FXML
    private void tanButtonClick() {
        inputField.setText(inputField.getText() + "tan(");
    }
    
    @FXML
    private void cosButtonClick() {
        inputField.setText(inputField.getText() + "cos(");
    }
    
    @FXML
    private void sinButtonClick() {
        inputField.setText(inputField.getText() + "sin(");
    }
    
    @FXML
    private void atanButtonClick() {
        inputField.setText(inputField.getText() + "atan(");
    }
    
    @FXML
    private void asinButtonClick() {
        inputField.setText(inputField.getText() + "asin(");
    }
    
    @FXML
    private void acosButtonClick() {
        inputField.setText(inputField.getText() + "acos(");
    }
    
    @FXML
    private void logButtonClick() {
        inputField.setText(inputField.getText() + "log(");
    }
    
    @FXML
    private void lnButtonClick() {
        inputField.setText(inputField.getText() + "ln(");
    }
    
    @FXML
    private void divButton() {
        inputField.setText(inputField.getText() + "/");
    }
    
    @FXML
    private void mulButtonClick() {
        inputField.setText(inputField.getText() + "*");
    }
    
    @FXML
    private void minusButtonClick() {
        inputField.setText(inputField.getText() + "-");
    }
    
    @FXML
    private void plusButtonClick() {
        inputField.setText(inputField.getText() + "+");
    }
    
    @FXML
    public void powButtonClick() {
        inputField.setText(inputField.getText() + "^");
    }
    
    @FXML
    public void modButtonClick() {
        inputField.setText(inputField.getText() + " mod ");
    }
    
    @FXML
    public void sqrtButtonClick() {
        inputField.setText(inputField.getText() + "sqrt(");
    }
    
    @FXML
    public void xButtonClick() {
        inputField.setText(inputField.getText() + "x");
    }
    
    @FXML
    public void leftBracketButtonClick() {
        inputField.setText(inputField.getText() + "(");
    }
    
    @FXML
    public void rightBracketButtonClick() {
        inputField.setText(inputField.getText() + ")");
    }
    
    @FXML
    protected void acButtonClick() {
        inputField.setText("");
    }
    
    @FXML
    public void bsButtonClick() {
        if (!inputField.getText().isEmpty()) {
            inputField.setText(inputField.getText().substring(0, inputField.getText().length() - 1));
        }
    }
    
    @FXML
    private void initialize() {
        configureMenuBar();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("#.######", symbols);
        historyList.setVisible(false);
        clearButton.setVisible(false);
        staticHistoryList = historyList;
        String jarPath = SmartCalcApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        historyPath = jarPath.substring(0, jarPath.lastIndexOf("/")) + "/history.txt";
        if (!new File(historyPath).exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(historyPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyList.getItems().add(line);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void configureMenuBar() {
        Menu helpMenu = new Menu("Help");
        MenuItem aboutMenuItem = new MenuItem("About SmartCalc");
        aboutMenuItem.setOnAction(event -> showAboutWindow());
        helpMenu.getItems().addAll(aboutMenuItem);
        menuBar.getMenus().clear();
        menuBar.getMenus().addAll(helpMenu);
        menuBar.setUseSystemMenuBar(true);
    }
    
    private void showAboutWindow() {
        try {
            FXMLLoader aboutLoader =
                    new FXMLLoader(SmartCalcApplication.class.getResource(
                            "/aboutwindow.fxml"));
            Scene aboutScene = new Scene(aboutLoader.load());
            Stage aboutStage = new Stage();
            aboutStage.setTitle("About SmartCalc");
            aboutStage.setScene(aboutScene);
            aboutStage.initModality(Modality.APPLICATION_MODAL);
            aboutStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Exception in AboutWindow: " + e.getMessage());
        }
    }
    
    @FXML
    private void resultButtonClick() {
        addExpressionInHistory();
        if (inputField.getText().contains("x")) {
            showErrorDialog("Use \"plot\"-button to work with variables and graphs");
        } else {
            presenter.calculateValue(inputField.getText(), 0);
        }
    }
    
    @FXML
    private void RestoreExpression() {
        Object selectedItem = historyList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            inputField.setText(selectedItem.toString());
            listButtonClick();
        }
    }
    
    @FXML
    private void clearButtonClick() {
        historyList.getItems().clear();
        inputField.setText("");
    }
    
    @FXML
    private void listButtonClick() {
        if (historyList.isVisible()) {
            historyList.setVisible(false);
            clearButton.setVisible(false);
            mulButton.setDisable(false);
            minusButton.setDisable(false);
            plusButton.setDisable(false);
            resultButton.setDisable(false);
        } else {
            historyList.setVisible(true);
            clearButton.setVisible(true);
            mulButton.setDisable(true);
            minusButton.setDisable(true);
            plusButton.setDisable(true);
            resultButton.setDisable(true);
        }
    }
    
    @FXML
    private void plotButtonClick() {
        addExpressionInHistory();
        presenter.calculateGraph(inputField.getText(), -100, 100);
        plotStage.showAndWait();
    }
    
    private void addExpressionInHistory() {
        if (!inputField.getText().isEmpty()) {
            historyList.getItems().add(inputField.getText());
        }
    }
}