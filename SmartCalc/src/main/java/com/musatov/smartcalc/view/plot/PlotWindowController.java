package com.musatov.smartcalc.view.plot;

import com.musatov.smartcalc.presenter.Presenter;

public interface PlotWindowController {
    void setPresenter(Presenter presenter);
    void showResult(double result);
    void showGraph(double[] xData, double[] yData, int size,
                   double xMin, double xMax);
    void resetSettings();
}
