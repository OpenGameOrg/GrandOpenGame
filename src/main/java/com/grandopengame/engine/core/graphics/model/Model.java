package com.grandopengame.engine.core.graphics.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

@Getter
@Setter
@Builder
public class Model {
    private ArrayList<Vector3f> vertices;
    private ArrayList<Vector3f> normals;
    private ArrayList<Vector2f> uv;
    private ArrayList<Face> faces;
}
