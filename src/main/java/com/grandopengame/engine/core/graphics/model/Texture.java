package com.grandopengame.engine.core.graphics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Texture object
 */
@RequiredArgsConstructor
@Getter
public class Texture {
    @Setter
    private int rendererId;

    private final int width;
    private final int height;
    private final ByteBuffer buffer;
    private final String path;

    @Setter
    private boolean isLoaded;

    public static Texture loadFrom(String filePath) throws IOException {
        var image = ImageIO.read(new File(filePath));

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip();

        return new Texture(image.getWidth(), image.getHeight(), buffer, filePath);
    }
}
