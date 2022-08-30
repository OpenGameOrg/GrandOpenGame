package com.grandopengame.engine.core.render;

import com.grandopengame.engine.core.graphics.model.Model;
import com.grandopengame.engine.core.graphics.model.Texture;
import com.grandopengame.engine.core.objects.SceneObject;

/**
 * Render stuff
 */
public interface Renderer {
    void render(SceneObject object);

    void loadModel(Model model);

    Texture loadTexture(Texture texture);
}
