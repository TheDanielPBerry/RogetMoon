package com.preAlpha;

/*
 * The MIT License (MIT)
 *
 * Copyright © 2014, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.stb.STBImage.*;

/**
 * This class represents a texture.
 *
 * @author Heiko Brumme
 */
public class Texture {

    /**
     * Stores the handle of the texture.
     */
    private final int id;

    /**
     * Width of the texture.
     */
    private final int width;
    /**
     * Height of the texture.
     */
    private final int height;

    /**
     * Creates a texture with specified width, height and data.
     *
     * @param width Width of the texture
     * @param height Height of the texture
     * @param data Picture Data in RGBA format
     */
    public Texture(int width, int height, ByteBuffer data) {
        id = glGenTextures();
        this.width = width;
        this.height = height;
        
        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
    }

    /**
     * Binds the texture.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Delete the texture.
     */
    public void delete() {
        glDeleteTextures(id);
    }

    /**
     * Gets the texture width.
     *
     * @return Texture width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the texture height.
     *
     * @return Texture height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Load texture from file.
     *
     * @param path File path of the texture
     * @return Texture from specified file
     */
    public static Texture loadTexture(String path) {
    	
        /* Prepare image buffers */
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        
        /* Load image */
        stbi_set_flip_vertically_on_load(1);
        ByteBuffer image = stbi_load(path, w, h, comp, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load a texture file!"
                    + System.lineSeparator() + stbi_failure_reason());
        }
        
        /* Get width and height of image */
        int width = w.get();
        int height = h.get();

        return new Texture(width, height, image);
    }
    
    
    private static final int BYTES_PER_PIXEL = 4;
    public static int loadTexture(BufferedImage image) {
    	int[] pixels = new int[image.getWidth() * image.getHeight()];
    	image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
    	ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);
    	for(int y = 0; y < image.getHeight(); y++) {
    		for(int x = 0; x < image.getWidth(); x++) {
    			int pixel = pixels[y * image.getWidth() + x];
    			buffer.put((byte) ((pixel >> 16) & 0xFF));
    			buffer.put((byte) ((pixel >> 8) & 0xFF));
    			buffer.put((byte) (pixel & 0xFF));
    			buffer.put((byte) ((pixel >> 24) & 0xFF));
    		}
    	}
    	buffer.flip();
    	int textureID = glGenTextures();
    	glBindTexture(GL_TEXTURE_2D, textureID);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    	return textureID;
    }
    
    
}