package com.musatov.smartcalc;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String jarDir = new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath()).getParent();
        String libPath = new File(jarDir, "../app").getAbsolutePath();
        System.setProperty("jna.library.path", libPath);
        System.load(new File(libPath, "libmodel.dylib").getAbsolutePath());
        SmartCalcApplication.main(args);
    }
}
