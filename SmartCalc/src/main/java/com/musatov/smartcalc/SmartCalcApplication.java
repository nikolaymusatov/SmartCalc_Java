package com.musatov.smartcalc;

import com.musatov.smartcalc.model.ModelLibrary;
import com.musatov.smartcalc.presenter.Presenter;
import com.musatov.smartcalc.view.plot.PlotWindowController;
import com.sun.jna.Pointer;
import com.musatov.smartcalc.view.main.MainWindowController;
import com.musatov.smartcalc.view.main.MainWindowControllerImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SmartCalcApplication extends Application {
    @Override
    public void start(Stage mainStage) throws IOException {
        Pointer model = ModelLibrary.INSTANCE.createModel();
        
        FXMLLoader mainLoader =
                new FXMLLoader(SmartCalcApplication.class.getResource("/smartcalc.fxml"));
        Scene mainScene = new Scene(mainLoader.load());
        MainWindowController mainWindowController = mainLoader.getController();
        
        FXMLLoader plotLoader =
                new FXMLLoader(SmartCalcApplication.class.getResource("/plotwindow.fxml"));
        Scene plotScene = new Scene(plotLoader.load());
        PlotWindowController plotWindowController = plotLoader.getController();
        
        Presenter presenter = new Presenter(mainWindowController, plotWindowController, model);
        mainWindowController.setPresenter(presenter);
        plotWindowController.setPresenter(presenter);
        
        mainStage.setResizable(false);
        mainStage.setTitle("SmartCalc");
        mainStage.setScene(mainScene);
        
        Stage plotStage = new Stage();
        plotStage.setTitle("Plot");
        plotStage.setScene(plotScene);
        plotStage.initModality(Modality.APPLICATION_MODAL);
        
        mainWindowController.setPlotStage(plotStage);
        mainStage.show();
    }
    
    @Override
    public void stop() throws Exception {
        MainWindowControllerImpl.saveHistory();
        MainWindowControllerImpl.deleteModel();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}