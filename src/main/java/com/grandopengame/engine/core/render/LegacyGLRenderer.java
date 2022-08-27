package com.grandopengame.engine.core.render;

import com.grandopengame.engine.core.graphics.model.Face;
import com.grandopengame.engine.core.graphics.model.Model;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class LegacyGLRenderer implements Renderer {
    @Override
    public void render(Model model) {
        glMaterialf(GL_FRONT, GL_SHININESS, 120);
        glBegin(GL_TRIANGLES);
        {
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
                Vector3f[] vertices = {
                        model.getVertices().get(face.getVertices()[0] - 1),
                        model.getVertices().get(face.getVertices()[1] - 1),
                        model.getVertices().get(face.getVertices()[2] - 1)
                };
                {
                    glNormal3f(normals[0].x(), normals[0].y(), normals[0].z());
                    glTexCoord2f(texCoords[0].x(), texCoords[0].y());
                    glVertex3f(vertices[0].x(), vertices[0].y(), vertices[0].z());
                    glNormal3f(normals[1].x(), normals[1].y(), normals[1].z());
                    glTexCoord2f(texCoords[1].x(), texCoords[1].y());
                    glVertex3f(vertices[1].x(), vertices[1].y(), vertices[1].z());
                    glNormal3f(normals[2].x(), normals[2].y(), normals[2].z());
                    glTexCoord2f(texCoords[2].x(), texCoords[2].y());
                    glVertex3f(vertices[2].x(), vertices[2].y(), vertices[2].z());
                }
            }
        }
        glEnd();
    }
}
