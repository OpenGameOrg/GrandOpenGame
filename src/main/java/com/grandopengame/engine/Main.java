package com.grandopengame.engine;

import com.grandopengame.engine.core.MainLoop;
import com.grandopengame.engine.core.graphics.model.ModelReaderFactory;
import com.grandopengame.engine.core.objects.SceneObject;
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
        var model = ModelReaderFactory.getReader("obj").read(modelStream);
        var object = new SceneObject(model, new Vector3f(), new Vector3f(), new Vector3f(0.5f, 0.5f, 0.5f));
        new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                object.rotation.x -= 0.05f;
                object.rotation.y -= 0.04f;
            }
        }).start();
        scene.addObject(object);

        log.info("Test model loaded in " + (System.currentTimeMillis() - startTimeMillis) + "ms");

        var mainLoop = new MainLoop();
        mainLoop.setScene(scene);
        mainLoop.run();
    }
}
