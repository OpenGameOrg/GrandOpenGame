package com.grandopengame.engine;

import com.grandopengame.engine.core.MainLoop;
import com.grandopengame.engine.core.graphics.model.ModelReaderFactory;
import com.grandopengame.engine.core.graphics.model.Texture;
import com.grandopengame.engine.core.objects.SceneObject;
import com.grandopengame.engine.core.render.OpenGlRenderer;
import com.grandopengame.engine.core.render.Scene;
import lombok.extern.java.Log;
import org.joml.Vector3f;

import java.io.IOException;

@Log
public class Main {
    public static void main(String[] args) throws IOException {
        log.info("Load test model");

        var startTimeMillis = System.currentTimeMillis();
        var scene = new Scene();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var modelStream = classloader.getResourceAsStream("models/cube.obj");
        OpenGlRenderer renderer = OpenGlRenderer.getInstance();
        var model = ModelReaderFactory.getReader("obj").read(modelStream);
        for (int i = 0; i < 1; i++) {
            var object = new SceneObject(model, new Vector3f(), new Vector3f(), new Vector3f(0.5f, 0.5f, 0.5f));
            scene.addObject(object);
        }

        log.info("Scene stats: " + scene.getStats());

        log.info("Test model loaded in " + (System.currentTimeMillis() - startTimeMillis) + "ms");

        var mainLoop = new MainLoop();
        mainLoop.setScene(scene);
        mainLoop.run();
    }
}
