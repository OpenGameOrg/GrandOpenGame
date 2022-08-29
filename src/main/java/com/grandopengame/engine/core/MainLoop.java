package com.grandopengame.engine.core;

import com.grandopengame.engine.core.render.OpenGlRenderer;
import com.grandopengame.engine.core.render.Renderer;
import com.grandopengame.engine.core.render.Scene;
import lombok.Setter;
import lombok.extern.java.Log;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Main lwjgl loop is here
 */
@Log
public class MainLoop {
    private long windowHandle;
    @Setter
    private Scene scene;
    private Renderer renderer;

    private long startTime;
    private int currentFrame;

    public void run() throws IOException {
        log.info("Initialising main game loop");

        startTime = System.currentTimeMillis();

        renderer = OpenGlRenderer.getInstance();

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // Create the window
        windowHandle = glfwCreateWindow(600, 600, "GrandOpenGame", NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        // Get the thread stack and push a new frame
        try (var stack = stackPush()) {
            var pWidth = stack.mallocInt(1);
            var pHeight = stack.mallocInt(1);

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary display
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert vidmode != null;
            glfwSetWindowPos(windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2);
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowHandle);
    }

    private void loop() throws IOException {
        GL.createCapabilities();

        int error = glGetError();
        glfwSetErrorCallback((ret, args) -> {
            log.severe("GLFW ERROR: " + ret);
        });

        renderer.registerModel(scene.getObjects().get(0).getModel());

        int program = createShaderProgram();

        int location = glGetUniformLocation(program, "u_Color");
        assert (location != -1);
        glUniform4f(location, 0.9f, 0.3f, 0.8f, 1.0f);

        int someError = glGetError();

        log.info("Main loop initialised in " + (System.currentTimeMillis() - startTime) + "ms");
        startTime = System.currentTimeMillis();
        float r = 0.0f;
        float increment = 0.05f;
        while (!glfwWindowShouldClose(windowHandle)) {
            if (System.currentTimeMillis() - startTime > 1000) {
                startTime = System.currentTimeMillis();
                log.info("Current fps: " + currentFrame);
                currentFrame = 0;
            }

            glClear(GL_COLOR_BUFFER_BIT);
            // render scene
            if (scene != null) {
                glUniform4f(location, r, 0.3f, 0.8f, 1.0f);

                if (r > 1.0f)
                    increment = -0.05f;
                else if (r < 0.0f)
                    increment = 0.05f;

                r+= increment;

                scene.getObjects().stream().forEach((sceneObject) -> renderer.render(sceneObject));
            }
            //
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();

            currentFrame++;
        }
    }

    private int createShaderProgram() throws IOException {
        int program = GL20.glCreateProgram();

        var vertexShader = compileShader(GL_VERTEX_SHADER, "shaders/simple.vert");
        var fragmentShader = compileShader(GL_FRAGMENT_SHADER, "shaders/simple.frag");

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        glUseProgram(program);

        return program;
    }

    private int compileShader(int shaderType, String sourcePath) throws IOException {
        int shader = GL20.glCreateShader(shaderType);
        var shaderSource = Files.readString(Path.of(sourcePath));
        GL20.glShaderSource(shader, shaderSource);
        GL20.glCompileShader(shader);

        var buffer = BufferUtils.createIntBuffer(1);
        glGetShaderiv(shader, GL_COMPILE_STATUS, buffer);

        if (buffer.get(0) == GL_FALSE) {
            var error = glGetShaderInfoLog(shader);
            log.severe(String.format("Can't compile %s shader, reason: %s", sourcePath, error));
            glDeleteShader(shader);
            return 0;
        }

        return shader;
    }
}
