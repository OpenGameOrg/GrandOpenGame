package com.grandopengame.engine.core.graphics.model;

/**
 * Return proper {@link ModelReader} object
 */
public class ModelReaderFactory {
    private static final ObjModelReader objModelReader = new ObjModelReader();

    /**
     * Get reader based on model extention
     * @param extention file extention (ex "obj", "fbx")
     * @return model reader
     */
    public static ModelReader getReader(String extention) {
        switch (extention) {
            case "obj":
                return objModelReader;
            case "fbx":
                break;
            default:
                break;
        }

        throw new IllegalArgumentException("No model readers can read " + extention);
    }
}
