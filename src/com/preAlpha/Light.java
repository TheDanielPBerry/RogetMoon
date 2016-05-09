package com.preAlpha;


import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLightfv;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	
	public int lightId;
	public Vector3f pos;
	public FloatBuffer position;
	public FloatBuffer diffuse;
	public boolean on = true;
	
	public static short lightCount = 0;
	
	public Light() {
		lightId = (GL_LIGHT0 + Light.lightCount++);
		glEnable(lightId);
		
        float lightDiffuse[] = { 0.1f, 0.1f, 0.1f, 0.1f };
        float lightPosition[] = { 0f, 0f, 0f, 1f };
        
        
        diffuse = ByteBuffer.allocateDirect(4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        diffuse.put(lightDiffuse).flip();
        
        position = ByteBuffer.allocateDirect(4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        position.put(lightPosition).flip();
        
        pos = new Vector3f();
	}
	
	public Light(Light l) {
		lightId = (GL_LIGHT0 + Light.lightCount++);
		glEnable(lightId);

        float lightDiffuse[] = { 0.1f, 0.1f, 0.1f, 0.1f };
        float lightPosition[] = { 0f, 0f, 0f, 1f };
        
        
        diffuse = ByteBuffer.allocateDirect(4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        diffuse.put(lightDiffuse).flip();
        
        position = ByteBuffer.allocateDirect(4*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        position.put(lightPosition).flip();
        
        pos = new Vector3f();
	}
	
	
	public void render() {
		glPushMatrix();
		glTranslatef(pos.x, pos.y, pos.z);
		//glLightfv(lightId, GL_DIFFUSE, diffuse);
		//glLightfv(lightId, GL_POSITION, position);
		glPopMatrix();
	}
	
	public void toggle() {
		on = !on;
		if(on) {
			glEnable(lightId);
		} else {
			glDisable(lightId);
		}
	}
	
	
}

class WhiteLight extends Light {
	public WhiteLight() {
		super();
	}
}
class ColorLight extends Light {
	
	public ColorLight() {
		super();
	}
}