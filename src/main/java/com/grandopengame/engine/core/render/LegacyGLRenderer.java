package com.grandopengame.engine.core.render;

import com.grandopengame.engine.core.graphics.model.Face;
import com.grandopengame.engine.core.objects.SceneObject;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;

public class LegacyGLRenderer implements Renderer {
    @Override
    public void render(SceneObject object) {
        var model = object.getModel();
        glPushMatrix();
        glTranslatef(object.position.x, object.position.y, object.position.z);
        glRotatef(object.rotation.x, 1.0f, 0, 0);
        glRotatef(object.rotation.y, 0, 1.0f, 0);
        glRotatef(object.rotation.z, 0, 0, 1.0f);
        glScalef(object.scale.x, object.scale.y, object.scale.z);

        var indexBuffer = BufferUtils.createShortBuffer(model.getFaces().size() * 3);

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

            indexBuffer.put(face.getVertices()[0]);
            indexBuffer.put(face.getVertices()[1]);
            indexBuffer.put(face.getVertices()[2]);
        }

        var bufferVerts = BufferUtils.createFloatBuffer(model.getVertices().size() * 3);
        model.getVertices().forEach((vert) -> {
            bufferVerts.put(vert.x);
            bufferVerts.put(vert.y);
            bufferVerts.put(vert.z);
        });

        bufferVerts.flip();
        indexBuffer.flip();

        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferVerts, GL15.GL_STATIC_DRAW);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
        glDrawElements(GL_TRIANGLES, model.getFaces().size() * 3, GL_UNSIGNED_SHORT, 0);

        glPopMatrix();
    }
}
