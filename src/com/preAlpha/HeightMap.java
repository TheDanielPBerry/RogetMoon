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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.lwjglx.BufferUtils;

public class HeightMap extends Article {

	
	/**A reference to this meshes vertex buffer object data.*/
	public int vboId;
	/**A count of the number of vertices.*/
	public int vertexCount = 0;
	/**A physical rotation model that is both visual and effects bounding boxes.*/
	public Vector3f rotation;
	/**Used for modeling to make sure bounding boxes are visible.*/
	public boolean transparent = false;
	/**The rotation of the article and the order in which it should occur.*/
	public String rotateOrder = "";
	
	
	
	
	
	public HeightMap(String srcPath, String texturePath) {
		super();
		vboId = glGenBuffers();
		BufferedImage mapImg = null;
		try {
			mapImg = ImageIO.read(new File(srcPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    	int[] pixels = new int[mapImg.getWidth() * mapImg.getHeight()];
    	mapImg.getRGB(0, 0, mapImg.getWidth(), mapImg.getHeight(), pixels, 0, mapImg.getWidth());
    	Face[] faces = new Face[((mapImg.getWidth()-1) * (mapImg.getHeight()-1))*2];
    	float xScale = 1f/mapImg.getWidth();
    	float yScale = 1f/mapImg.getWidth();
    	for(int y = 0; y < mapImg.getHeight()-1; y++) {
    		for(int x = 0; x < mapImg.getWidth()-1; x++) {
    			float pixelData[] = {(pixels[y * mapImg.getWidth() + x]*0.000001f),
    					(pixels[(y+1) * mapImg.getWidth() + x]*0.000001f),
    					(pixels[(y+1) * mapImg.getWidth() + (x+1)]*0.000001f),
    	    			(pixels[(y) * mapImg.getWidth() + (x+1)]*0.000001f)
    				};
    			
    			faces[y * (mapImg.getWidth()-1) + x] = new Face();
    			faces[y * (mapImg.getWidth()-1) + x].vertices = new Vertex[] { new Vertex(new Vector3f(x,pixelData[0],y), null, new Vector2f((x*xScale),(y*yScale))),
    			new Vertex(new Vector3f(x,pixelData[1],y+1), null, new Vector2f(x*xScale,(y+1)*yScale)),
    			new Vertex(new Vector3f(x+1,pixelData[2],y+1), null, new Vector2f((x+1)*xScale,(y+1)*yScale)) };
    			faces[y * (mapImg.getWidth()-1) + x].setNormals();
    			
    			faces[(y * (mapImg.getWidth()-1) + x)+(faces.length/2)] = new Face();
    			faces[(y * (mapImg.getWidth()-1) + x)+(faces.length/2)].vertices = new Vertex[] { new Vertex(new Vector3f(x,pixelData[0],y), null, new Vector2f(x*xScale,y*xScale)),
    			new Vertex(new Vector3f(x+1,pixelData[2],y+1), null, new Vector2f((x+1)*xScale,(y+1)*yScale)),
    			new Vertex(new Vector3f(x+1,pixelData[3],y), null, new Vector2f((x+1)*xScale,y*yScale)) };
    			faces[(y * (mapImg.getWidth()-1) + x)+(faces.length/2)].setNormals();
    		}
    	}
		
		
		
		vertexCount = faces.length * 3;
		float[] data = getData(faces);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 4);
		buffer.put(data);
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		texture = Texture.loadTexture(texturePath);
		pos = new Vector3f();
		quat = new Vector4f(0,0,0,0);
		billboard = new boolean[] {false, false, false};
		cullFaces = false;
		velocity = new Vector3f();
		boxes = new Box[(faces.length/2)];
		for(int i=0; i<boxes.length; i++) {
			boxes[i] = faces[i].genBox();
			boxes[i].article = this;
		}
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
		glEnable(GL_CULL_FACE);
		
		glPushMatrix();
			if(static_object) {
				glTranslatef(Roget.getCamera().pos.x, Roget.getCamera().pos.y, Roget.getCamera().pos.z);
			}
			glTranslatef(pos.x, pos.y, pos.z);
			glScalef(scale.x, scale.y, scale.z);
			rotateByOrder(rotateOrder, rotation);
			//glRotatef(Roget.getCamera().quat.y, billboard[0]?-1:0, billboard[1]?-1:0, billboard[2]?-1:0);
			
			
		    glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		    
		glPopMatrix();
	}
	
	
	@Override
	public void rotate(String rot) {
		String[] tags = rot.split("=");
		for(byte i=0; i<tags.length; i++) {
			String axis = tags[i].substring(tags[i].indexOf(";")+1);
			rotateOrder.replace(axis, "");
			rotateOrder = axis + rotateOrder;
			if(axis.toUpperCase().equals("X")) {
				rotation.x += Float.parseFloat(tags[i+1].substring(0, tags[i+1].indexOf(";")));
				//rotateBoxes((byte) 0, rotation.x);
			}
			else if(axis.toUpperCase().equals("Y")) {
				rotation.y += Float.parseFloat(tags[i+1].substring(0, tags[i+1].indexOf(";")));
				//rotateBoxes((byte) 1, rotation.y);
			}
			else if(axis.toUpperCase().equals("Z")) {
				rotation.z += Float.parseFloat(tags[i+1].substring(0, tags[i+1].indexOf(";")));
				//rotateBoxes((byte) 2, rotation.z);
			}
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
	