package com.musatov.smartcalc.view.main;

import com.musatov.smartcalc.presenter.Presenter;
import javafx.stage.Stage;

public interface MainWindowController {
    void setPresenter(Presenter presenter);
    void setPlotStage(Stage plotStage);
    void showErrorDialog(String message);
    void showResult(double result);
}
