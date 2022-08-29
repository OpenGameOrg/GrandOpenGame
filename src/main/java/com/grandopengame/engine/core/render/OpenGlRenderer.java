package com.grandopengame.engine.core.render;

import com.grandopengame.engine.core.graphics.model.Face;
import com.grandopengame.engine.core.graphics.model.Model;
import com.grandopengame.engine.core.objects.SceneObject;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class OpenGlRenderer implements Renderer {
    private static OpenGlRenderer instance;
    private Map<Long, Integer> modelIdVaoObject;
    private Map<Long, Integer> modelIndexBuffer;

    public static OpenGlRenderer getInstance() {
        if (instance == null) {
            return init();
        }

        return instance;
    }

    private static OpenGlRenderer init() {
        instance = new OpenGlRenderer();
        instance.modelIdVaoObject = new HashMap<>();
        instance.modelIndexBuffer = new HashMap<>();
        return instance;
    }

    @Override
    public void registerModel(Model model) {
        var indexBufferData = BufferUtils.createShortBuffer(model.getFaces().size() * 3);

        for (Face face : model.getFaces()) {
            Vector3f[] normals = {
                    model.getNormals().get(face.getNormals()[0] - 1),
                    model.getNormals().get(face.getNormals()[1] - 1),
                    model.getNormals().get(face.getNormals()[2] - 1)
            };
            Vector2f[] texCoords = {
                    model.getUv().get(face.getTextureCoordinates()[0] - 1),
                    model.getUv().get(face.getTextureCoordinates()[1] - 1),
                    model.getUv().get(face.getTextureCoordinates()[2] - 1)
            };

            indexBufferData.put(face.getVertices()[0]);
            indexBufferData.put(face.getVertices()[1]);
            indexBufferData.put(face.getVertices()[2]);
        }

        var bufferVerts = BufferUtils.createFloatBuffer(model.getVertices().size() * 3);
        model.getVertices().forEach((vert) -> {
            bufferVerts.put(vert.x);
            bufferVerts.put(vert.y);
            bufferVerts.put(vert.z);
        });

        bufferVerts.flip();
        indexBufferData.flip();

        int arrayBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer);
        glBufferData(GL_ARRAY_BUFFER, bufferVerts, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);

        int indexBuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBufferData, GL_STATIC_DRAW);
    }

    private int indexBuffer;
    private int arrayBuffer;

    @Override
    public void render(SceneObject object) {
        var model = object.getModel();

        glDrawElements(GL_TRIANGLES, object.getModel().getFaces().size() * 3, GL_UNSIGNED_SHORT, 0);
    }
}
