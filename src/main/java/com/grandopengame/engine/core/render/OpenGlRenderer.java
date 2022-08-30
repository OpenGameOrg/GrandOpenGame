package com.grandopengame.engine.core.render;

import com.grandopengame.engine.core.graphics.model.Face;
import com.grandopengame.engine.core.graphics.model.Model;
import com.grandopengame.engine.core.graphics.model.Texture;
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
import static org.lwjgl.opengl.GL30.*;

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
    public void loadModel(Model model) {
        var indexBufferData = BufferUtils.createShortBuffer(model.getIndices().size());

        model.getIndices().forEach(indexBufferData::put);

        var bufferVerts = BufferUtils.createFloatBuffer(model.getVertices().size() * 5);
        for (int i = 0; i < model.getVertices().size(); i++) {
            var vert = model.getVertices().get(i);
            var texCoord = model.getUv()[i];
            bufferVerts.put(vert.x);
            bufferVerts.put(vert.y);
            bufferVerts.put(vert.z);
            bufferVerts.put(texCoord.x);
            bufferVerts.put(texCoord.y);
        }

        bufferVerts.flip();
        indexBufferData.flip();

        var vao = glGenVertexArrays();
        glBindVertexArray(vao);

        arrayBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glBufferData(GL_ARRAY_BUFFER, bufferVerts, GL_STATIC_DRAW);

        indexBuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBufferData, GL_STATIC_DRAW);

        modelIdVaoObject.put(model.getId(), vao);
    }

    @Override
    public Texture loadTexture(Texture texture) {
        var textureId = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texture.getWidth(),
                texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, texture.getBuffer());

        texture.setLoaded(true);
        texture.setRendererId(textureId);

        return texture;
    }

    private int indexBuffer;
    private int arrayBuffer;

    @Override
    public void render(SceneObject object) {
        var model = object.getModel();

        glBindVertexArray(modelIdVaoObject.get(model.getId()));
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);

        glDrawElements(GL_TRIANGLES, object.getModel().getIndices().size() * 3, GL_UNSIGNED_SHORT, 0);
    }
}
