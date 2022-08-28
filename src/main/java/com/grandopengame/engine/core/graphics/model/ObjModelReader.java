package com.grandopengame.engine.core.graphics.model;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Read .obj wavefront model
 */
public class ObjModelReader implements ModelReader {
    @Override
    public Model read(InputStream stream) {
        var scanner = new Scanner(stream);
        var vertices = new ArrayList<Vector3f>();
        var faces = new ArrayList<Face>();
        var uv = new ArrayList<Vector2f>();
        var normals = new ArrayList<Vector3f>();

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            if (line == null || line.isBlank() || line.startsWith("#")) {
                continue;
            }
            var components = line.split(" ");
            switch (components[0]) {
                case "v":
                    vertices.add(new Vector3f(
                            Float.parseFloat(components[1]),
                            Float.parseFloat(components[2]),
                            Float.parseFloat(components[3]))
                    );
                    break;
                case "vn":
                    normals.add(new Vector3f(
                            Float.parseFloat(components[1]),
                            Float.parseFloat(components[2]),
                            Float.parseFloat(components[3]))
                    );
                    break;
                case "f":
                    var faceVerts = new short[] {
                            Short.parseShort(components[1].split("/")[0]),
                            Short.parseShort(components[2].split("/")[0]),
                            Short.parseShort(components[3].split("/")[0]),
                    };
                    var faceTexture = new int[] {
                            Integer.parseInt(components[1].split("/")[1]),
                            Integer.parseInt(components[2].split("/")[1]),
                            Integer.parseInt(components[3].split("/")[1]),
                    };
                    var faceNormals = new int[] {
                            Integer.parseInt(components[1].split("/")[2]),
                            Integer.parseInt(components[2].split("/")[2]),
                            Integer.parseInt(components[3].split("/")[2]),
                    };
                    faces.add(new Face(faceVerts, faceTexture, faceNormals));
                    break;
                case "vt":
                    uv.add(new Vector2f(
                            Float.parseFloat(components[1]),
                            Float.parseFloat((components[2])))
                    );
                    break;
                default:
                    break;
            }
        }
        return Model.builder()
                .vertices(vertices)
                .normals(normals)
                .faces(faces)
                .uv(uv)
                .build();
    }

    @Override
    public Model read(String modelPath) throws FileNotFoundException {
        return read(new FileInputStream(modelPath));
    }
}
