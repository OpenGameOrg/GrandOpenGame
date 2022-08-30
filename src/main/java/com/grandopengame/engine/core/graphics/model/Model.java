package com.grandopengame.engine.core.graphics.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;

@Getter
@Setter
@Builder
public class Model {
    private long id;
    private ArrayList<Vector3f> vertices;
    private ArrayList<Vector3f> normals;
    private Vector2f[] uv;
    private ArrayList<Short> indices;
}
