module edu.school21.smartcalc {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.jna;
    requires java.rmi;
    
    opens com.musatov.smartcalc.model to javafx.fxml;
    exports com.musatov.smartcalc.model;
    exports com.musatov.smartcalc;
    opens com.musatov.smartcalc to javafx.fxml;
    exports com.musatov.smartcalc.view.main;
    opens com.musatov.smartcalc.view.main to javafx.fxml;
    exports com.musatov.smartcalc.view.plot;
    opens com.musatov.smartcalc.view.plot to javafx.fxml;
    exports com.musatov.smartcalc.presenter;
}