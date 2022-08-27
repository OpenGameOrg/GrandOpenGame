package com.grandopengame.engine.core.render;

import com.grandopengame.engine.core.graphics.model.Model;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of what should be rendered
 */
public class Scene {
    @Getter
    private List<Model> models = new ArrayList<>();

    public void addModel(Model model) {
        models.add(model);
    }
}
