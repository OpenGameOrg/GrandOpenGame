package com.grandopengame.engine;

import com.grandopengame.engine.core.MainLoop;
import com.grandopengame.engine.core.graphics.model.ModelReaderFactory;
import com.grandopengame.engine.core.render.Scene;
import lombok.extern.java.Log;

@Log
public class Main {
    public static void main(String[] args) {
        log.info("Load test model");

        var startTimeMillis = System.currentTimeMillis();
        var scene = new Scene();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var modelStream = classloader.getResourceAsStream("models/cube.obj");
        var model = ModelReaderFactory.getReader("obj").read(modelStream);
        scene.addModel(model);

        log.info("Test model loaded in " + (System.currentTimeMillis() - startTimeMillis) + "ms");

        var mainLoop = new MainLoop();
        mainLoop.setScene(scene);
        mainLoop.run();
    }
}
