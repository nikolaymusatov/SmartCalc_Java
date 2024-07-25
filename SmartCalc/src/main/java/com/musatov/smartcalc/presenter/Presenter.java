package com.musatov.smartcalc.presenter;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.musatov.smartcalc.model.ModelLibrary;
import com.musatov.smartcalc.view.main.MainWindowController;
import com.musatov.smartcalc.view.plot.PlotWindowController;

public class Presenter {
    private final MainWindowController mainWindowController;
    private final PlotWindowController plotWindowController;
    private final Pointer model;
    private String expression;
    
    public Presenter(MainWindowController mainWindowController,
                     PlotWindowController plotWindowController,
                     Pointer model) {
        this.mainWindowController = mainWindowController;
        this.plotWindowController = plotWindowController;
        this.model = model;
    }
    
    public void deleteModel() {
        ModelLibrary.INSTANCE.deleteModel(this.model);
    }
    
    public void calculateValue(String expression, double x) {
        if (ModelLibrary.INSTANCE.calculateValue(this.model, expression, x) == -1) {
            mainWindowController.showErrorDialog("Invalid expression!");
        } else {
            this.expression = expression;
            mainWindowController.showResult(ModelLibrary.INSTANCE.getResult(model));
        }
    }
    
    public void calculateValueFromX(double x) {
        ModelLibrary.INSTANCE.calculateValue(this.model, this.expression, x);
        plotWindowController.showResult(ModelLibrary.INSTANCE.getResult(this.model));
    }
    
    public void calculateGraph(String expression, double xMin, double xMax) {
        if (ModelLibrary.INSTANCE.calculateGraph(this.model, expression, xMin, xMax) == -1) {
            mainWindowController.showErrorDialog("Invalid expression!");
        } else {
            this.expression = expression;
            plotWindowController.resetSettings();
            prepareGraphData(xMin, xMax);
        }
    }
    
    public void recalculateGraph(double xMin, double xMax) {
        ModelLibrary.INSTANCE.calculateGraph(this.model, this.expression, xMin, xMax);
        prepareGraphData(xMin, xMax);
    }
    
    private void prepareGraphData(double xMin, double xMax) {
        int maxSize = 510;
        IntByReference sizeRef = new IntByReference();
        Memory xDataMemory = new Memory(maxSize * Double.BYTES);
        Memory yDataMemory = new Memory(maxSize * Double.BYTES);
        ModelLibrary.INSTANCE.getGraphData(model, xDataMemory, yDataMemory, sizeRef);
        int size = sizeRef.getValue();
        double[] xData = xDataMemory.getDoubleArray(0, size);
        double[] yData = yDataMemory.getDoubleArray(0, size);
        plotWindowController.showGraph(xData, yData, size, xMin, xMax);
    }
}
