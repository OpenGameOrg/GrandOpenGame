package com.grandopengame.engine.core.graphics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Face {
    private final int[] vertices;
    private final int[] textureCoordinates;
    private final int[] normals;
}
