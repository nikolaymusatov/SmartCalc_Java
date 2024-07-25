package com.musatov.smartcalc.model;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface ModelLibrary extends Library {
    ModelLibrary INSTANCE = Native.load("model", ModelLibrary.class);
    
    Pointer createModel();
    void deleteModel(Pointer model);
    int calculateValue(Pointer model, String expression, double x);
    int calculateGraph(Pointer model, String expression, double xmin,
                     double xmax);
    double getResult(Pointer model);
    void getGraphData(Pointer model, Pointer xData, Pointer yData,
                   IntByReference size);
}
