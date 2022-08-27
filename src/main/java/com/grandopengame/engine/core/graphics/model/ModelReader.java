package com.grandopengame.engine.core.graphics.model;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Object that can parse a model
 * (.obj, .fbx, etc)
 */
public interface ModelReader {
    /**
     * Read model from input stream
     * @param stream input stream containing model data
     * @return Model
     */
    Model read(InputStream stream);

    /**
     * Read model located in specified path
     * @param modelPath path of the model file
     * @return model
     */
    Model read(String modelPath) throws FileNotFoundException;
}
