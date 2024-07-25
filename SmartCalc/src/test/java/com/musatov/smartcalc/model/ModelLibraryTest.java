package com.musatov.smartcalc.model;

import com.sun.jna.Pointer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

public class ModelLibraryTest {
    static Pointer model;
    
    @BeforeAll
    static void init() {
        String currentDir = System.getProperty("user.dir");
        String libraryPath = currentDir + "/target/libmodel.dylib";
        System.setProperty("jna.library.path", currentDir + "/target");
        System.load(libraryPath);
        
        model = ModelLibrary.INSTANCE.createModel();
    }
    
    @AfterAll
    static void deleteModel() {
        ModelLibrary.INSTANCE.deleteModel(model);
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/equals.csv")
    void calculateValueTest_equals(String expression, double expectedResult) {
        double delta = 1e-6;
        ModelLibrary.INSTANCE.calculateValue(model, expression, 0);
        assertEquals(ModelLibrary.INSTANCE.getResult(model), expectedResult, delta);
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/throws.csv")
    void calculateValueTest_throws(String expression) {
        int result = ModelLibrary.INSTANCE.calculateValue(model, expression, 0);
        assertEquals(result, -1);
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/equals_variable.csv")
    void calculateValueTest_equalsWithVariable(String expression,
                                               double expectedResult,
                                               double x) {
        double delta = 1e-5;
        ModelLibrary.INSTANCE.calculateValue(model, expression, x);
        assertEquals(ModelLibrary.INSTANCE.getResult(model), expectedResult, delta);
    }
}
