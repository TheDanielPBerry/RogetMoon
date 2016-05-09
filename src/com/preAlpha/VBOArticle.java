package com.preAlpha;


import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;



import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.lwjglx.BufferUtils;

public class VBOArticle extends Article {
	
	
	
	
	
	
	
	
	/**A reference to this meshes vertex buffer object data.*/
	public int vboId;
	/**A count of the number of vertices.*/
	public int vertexCount = 0;
	/**Instances are different versions of the same model*/
	public ArrayList<LightWeightArticle> instances = new ArrayList<LightWeightArticle>();
	
	
	public VBOArticle() {
		super();
		vboId = glGenBuffers();
		Face[] faces = OBJArticle.loadOBJArticle("res\\assets\\sword.obj");
		vertexCount = faces.length * 3;
		float[] data = getData(faces);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 4);
		buffer.put(data);
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		texturePath = "res\\images\\sword.png";
		texture = Texture.loadTexture("res\\images\\sword.png");
		pos = new Vector3f();
		quat = new Vector4f(0,0,0,0);
		billboard = new boolean[] {false, false, false};
		cullFaces = false;
		velocity = new Vector3f();
		boxes = new Box[0];
		scale = new Vector3f(1,1,1);
	}

	public VBOArticle(String filePath) {
		super();
		vboId = glGenBuffers();
		Face[] faces = OBJArticle.loadOBJArticle(filePath);
		vertexCount = faces.length * 3;
		float[] data = getData(faces);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 4);
		buffer.put(data);
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		texturePath = OBJArticle.loadOBJTexture(filePath);
		texture = Texture.loadTexture(texturePath);
		pos = new Vector3f();
		quat = new Vector4f(0,0,0,0);
		billboard = new boolean[] {false, false, false};
		cullFaces = false;
		velocity = new Vector3f();
		boxes = new Box[0];
		scale = new Vector3f(1,1,1);
	}
	
	public void tick() {
	}
	
	
	public static float[] getData(Face[] faces) {
		final byte VERTEX_SIZE = 8;
		final byte FACE_SIZE = 3;
		float[] buffer = new float[faces.length * VERTEX_SIZE * FACE_SIZE];
		for(int i=0; i<faces.length; i++) {
			for(int j=0; j<faces[i].vertices.length; j++) {
				float[] linearVertex = getVertexLinear(faces[i].vertices[j]);
				for(int k=0; k<linearVertex.length; k++) {
					buffer[((i * (FACE_SIZE*VERTEX_SIZE)) + (j * VERTEX_SIZE) + k)] = linearVertex[k];
				}
			}
		}
		return buffer;
	}
	
	public static float[] getVertexLinear(Vertex v) {
		return new float[] {v.pos.x, v.pos.y, v.pos.z, v.normal.x, v.normal.y, v.normal.z, v.textureMap.x, v.textureMap.y};
	}
	
	
	
	@Override
	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
	    glEnableClientState(GL_NORMAL_ARRAY);
	    glEnableClientState(GL_TEXTURE_COORD_ARRAY);
	    glBindBuffer(GL_ARRAY_BUFFER, vboId);
	    glVertexPointer(3, GL_FLOAT, 32, 0);
	    glNormalPointer(GL_FLOAT, 32, 12);
	    glTexCoordPointer(2, GL_FLOAT, 32, 24);
		if(texture != null) texture.bind();
		if(cullFaces) {
			glEnable(GL_CULL_FACE);
		} else {
			glDisable(GL_CULL_FACE);
		}
		
		
	    for(LightWeightArticle instance : instances) {
			glPushMatrix();
				if(instance.static_object) {
					glTranslatef(Roget.getCamera().pos.x, Roget.getCamera().pos.y, Roget.getCamera().pos.z);
				}
				glTranslatef(instance.pos.x, instance.pos.y, instance.pos.z);
				glScalef(instance.scale.x, instance.scale.y, instance.scale.z);
				rotateByOrder(instance.rotateOrder, instance.rotation);
				//glRotatef(Roget.getCamera().quat.y, billboard[0]?-1:0, billboard[1]?-1:0, billboard[2]?-1:0);
				if(instance.transparent) {
					glColor4f(1,1,1,0.2f);
				}
				
			    glDrawArrays(GL_TRIANGLES, 0, vertexCount);
			    
				if(instance.transparent) {
					glColor4f(1,1,1,1);
				}
			glPopMatrix();
	    }
	}
	
	
	public void rotateByOrder(String s, Vector3f angle) {
		if(s.length()==0) {
			return;
		}
		switch(s.charAt(0)) {
		case 'X':
			glRotatef(angle.x, 1, 0, 0);
			break;
		case 'Y':
			glRotatef(angle.y, 0, 1, 0);
			break;
		case 'Z':
			glRotatef(angle.z, 0, 0, 1);
			break;
		}
		rotateByOrder(s.substring(1), angle);
	}
	
}


