package com.preAlpha;


import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.NamedNodeMap;

import com.mapping.Parsing;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Article {
	
	/**A String id to refer to what type of model this is. ex: Crate, WhiteTile*/
	public String name;
	/**The bound texture. Can only loaded using Texture.loadArticle(String filePath).*/
	public Texture texture;
	/**Lazy Loading of textures outside of the opengl context.*/
	public String texturePath = null;
	/**The position of an mesh in global space.*/
	public Vector3f pos;
	/**A visual rotation of the mesh using quaternions.*/
	public Vector4f quat;
	/**A factor of scaling for the object and it's bounding boxes.*/
	public Vector3f scale;
	/**Determine whether the mesh is part of the dynamic motion state.*/
	public boolean dynamic_object = false;
	/**Determine whether the mesh translated relative to the camera.*/
	public boolean static_object = false;
	/**A collection of all of the boxes which make up the physical collision area of the mesh.*/
	public Box[] boxes;
	/**The geometry of the article*/
	public Vector3f[][] triangles;
	/**The texture points*/
	public Vector2f[][] textureMaps;
	/**Any light sources which may exist as part of the mesh.*/
	public Light[] lightSources = {};
	/**A tracking of the objects current speed and direction through space.*/
	public Vector3f velocity;
	/**Determine if this is a mesh where faces do not need to be drawn on both sides.*/
	public boolean cullFaces;
	/**Determine if the mesh should face the camera on a particular axis constantly.*/
	public boolean[] billboard;
	/**Mapped attributes tied to the article from xml.*/
	public NamedNodeMap attributes;
	/**Event mapping to this mesh.*/
	public HashMap<String, String> events;
	/**An XML identifier for this object in the map.*/
	public String id = "";
	
	
	public Article(String cube) {
		pos = new Vector3f(0,0,-1f);
		scale = new Vector3f(1,1,1);
		quat = new Vector4f(0,0,0,0);
		billboard = new boolean[] {false, false, false};
		cullFaces = false;
		triangles = new Vector3f[][] {
			{
				new Vector3f(-0.5f, -0.5f, -0.5f),
				new Vector3f(-0.5f, 0.5f, -0.5f),
				new Vector3f(0.5f, 0.5f, -0.5f)
			},
			{
				new Vector3f(0.5f, -0.5f, -0.5f),
				new Vector3f(-0.5f, -0.5f, -0.5f),
				new Vector3f(0.5f, 0.5f, -0.5f)
			},
			{
				new Vector3f(-0.5f, 0.5f, 0.5f),
				new Vector3f(-0.5f, -0.5f, 0.5f),
				new Vector3f(0.5f, 0.5f, 0.5f)
			},
			{
				new Vector3f(-0.5f, -0.5f, 0.5f),
				new Vector3f(0.5f, -0.5f, 0.5f),
				new Vector3f(0.5f, 0.5f, 0.5f)
			},
			{
				new Vector3f(0.5f, -0.5f, 0.5f),
				new Vector3f(0.5f, -0.5f, -0.5f),
				new Vector3f(0.5f, 0.5f, 0.5f)
			},
			{
				new Vector3f(0.5f, -0.5f, -0.5f),
				new Vector3f(0.5f, 0.5f, -0.5f),
				new Vector3f(0.5f, 0.5f, 0.5f)
			},
			{
				new Vector3f(-0.5f, -0.5f, -0.5f),
				new Vector3f(-0.5f, -0.5f, 0.5f),
				new Vector3f(-0.5f, 0.5f, 0.5f)
			},
			{
				new Vector3f(-0.5f, -0.5f, -0.5f),
				new Vector3f(-0.5f, 0.5f, 0.5f),
				new Vector3f(-0.5f, 0.5f, -0.5f)
			},
			{
				new Vector3f(-0.5f, 0.5f, -0.5f),
				new Vector3f(-0.5f, 0.5f, 0.5f),
				new Vector3f(0.5f, 0.5f, 0.5f)
			},
			{
				new Vector3f(0.5f, 0.5f, -0.5f),
				new Vector3f(-0.5f, 0.5f, -0.5f),
				new Vector3f(0.5f, 0.5f, 0.5f)
			},
			{
				new Vector3f(-0.5f, -0.5f, 0.5f),
				new Vector3f(-0.5f, -0.5f, -0.5f),
				new Vector3f(0.5f, -0.5f, 0.5f)
			},
			{
				new Vector3f(-0.5f, -0.5f, -0.5f),
				new Vector3f(0.5f, -0.5f, -0.5f),
				new Vector3f(0.5f, -0.5f, 0.5f)
			},
		};
		texture = Texture.loadTexture("res\\images\\crate.png");
		textureMaps = new Vector2f[][] {
				{
					new Vector2f(0f, 0f),
					new Vector2f(1f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 1f),
					new Vector2f(0f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 0f),
					new Vector2f(1f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 1f),
					new Vector2f(0f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 0f),
					new Vector2f(1f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 1f),
					new Vector2f(0f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 0f),
					new Vector2f(1f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 1f),
					new Vector2f(0f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 0f),
					new Vector2f(1f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 1f),
					new Vector2f(0f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 0f),
					new Vector2f(1f, 0f),
					new Vector2f(1f, 1f)
				},
				{
					new Vector2f(0f, 1f),
					new Vector2f(0f, 0f),
					new Vector2f(1f, 1f)
				}
		};
	}
	public Article(boolean f) {
		texture = null;
		pos = new Vector3f(0,0,0f);
		quat = new Vector4f(0,0,0,0);
		billboard = new boolean[] {false, false, false};
		cullFaces = false;
		triangles = new Vector3f[0][0];
		velocity = new Vector3f();
		textureMaps = new Vector2f[0][0];
		boxes = new Box[0];
		scale = new Vector3f(1,1,1);
	}
	
	public Article() {
		texture = null;
		pos = new Vector3f(0,0,0);
		quat = new Vector4f(0,0,0,0);
		billboard = new boolean[] {false, false, false};
		cullFaces = false;
		triangles = new Vector3f[0][0];
		velocity = new Vector3f();
		textureMaps = new Vector2f[0][0];
		boxes = new Box[0];
		scale = new Vector3f(1,1,1);
	}
	
	
	public Article(Article a) {
		attributes = a.attributes;
		name = a.name;
		scale = a.scale;
		texture = a.texture;
		texturePath = a.texturePath;
		pos = new Vector3f(a.pos.x,a.pos.y,a.pos.z);
		quat = new Vector4f(0,0,0,0);
		billboard = a.billboard;
		cullFaces = a.cullFaces;
		triangles = a.triangles;
		velocity = new Vector3f();
		textureMaps = a.textureMaps;
		boxes = new Box[a.boxes.length];
		dynamic_object = a.dynamic_object;
		for(byte i=0; i<boxes.length; i++) {
			boxes[i] = new Box(a.boxes[i].pos, a.boxes[i].dim);
			boxes[i].article = this;
		}
		if(attributes != null) {
			buildAttributes();
		}
	}
	
	public void setAttributes(NamedNodeMap a) {
		attributes = a;
		buildAttributes();
	}
	
	
	public void buildAttributes() {
		for(byte i=0; i<attributes.getLength(); i++) {
			switch(attributes.item(i).getNodeName()) {
			case "x":
				pos.x = (float) Parsing.eval(attributes.item(i).getNodeValue());
				break;
			case "y":
				pos.y = (float) Parsing.eval(attributes.item(i).getNodeValue());
				break;
			case "z":
				pos.z = (float) Parsing.eval(attributes.item(i).getNodeValue());
				break;
			case "vx":
				velocity.x = (float) Parsing.eval(attributes.item(i).getNodeValue());
				break;
			case "vy":
				velocity.y = (float) Parsing.eval(attributes.item(i).getNodeValue());
				break;
			case "vz":
				velocity.z = (float) Parsing.eval(attributes.item(i).getNodeValue());
				break;
			case "static":
				static_object = Boolean.parseBoolean(attributes.item(i).getNodeValue());
				break;
			case "dynamic":
				dynamic_object = Boolean.parseBoolean(attributes.item(i).getNodeValue());
				break;
			case "id":
				id = attributes.item(i).getNodeValue();
				break;
			case "scale-x":
				scale(new Vector3f(Float.parseFloat(attributes.item(i).getNodeValue()),1,1));
				break;
			case "scale-y":
				scale(new Vector3f(1,Float.parseFloat(attributes.item(i).getNodeValue()),1));
				break;
			case "scale-z":
				scale(new Vector3f(1,1,Float.parseFloat(attributes.item(i).getNodeValue())));
				break;
			}
		}
	}
	
	
	
	
	public void rotate(String rot) {
		rot = rot.toUpperCase();
		if(rot.indexOf("X=")>=0) {
			
		}
		if(rot.indexOf("Y=")>=0) {
			short value = Short.parseShort(rot.substring(rot.indexOf("Y=")+2, rot.indexOf(";", rot.indexOf("Y=")+2)));
			if(value == 90) {
				for(byte i=0; i<triangles.length; i++) {
					float temp[] = {triangles[i][0].y,triangles[i][1].y,triangles[i][2].y};
					Vector3f.cross(triangles[i][0],new Vector3f(0,1,0), triangles[i][0]);
					Vector3f.cross(triangles[i][1],new Vector3f(0,1,0), triangles[i][1]);
					Vector3f.cross(triangles[i][2],new Vector3f(0,1,0), triangles[i][2]);
					triangles[i][0].y = temp[0];
					triangles[i][1].y = temp[1];
					triangles[i][2].y = temp[2];
				}
			}
			if(value == -90) {
				for(byte i=0; i<triangles.length; i++) {
					float temp[] = {triangles[i][0].y,triangles[i][1].y,triangles[i][2].y};
					Vector3f.cross(triangles[i][0],new Vector3f(0,-1,0), triangles[i][0]);
					Vector3f.cross(triangles[i][1],new Vector3f(0,-1,0), triangles[i][1]);
					Vector3f.cross(triangles[i][2],new Vector3f(0,-1,0), triangles[i][2]);
					triangles[i][0].y = temp[0];
					triangles[i][1].y = temp[1];
					triangles[i][2].y = temp[2];
				}
			}
		}
		if(rot.indexOf("Z=")>=0) {
			short value = Short.parseShort(rot.substring(rot.indexOf("Z=")+2, rot.indexOf(";", rot.indexOf("Z=")+2)));
			if(value == 90) {
				for(byte i=0; i<triangles.length; i++) {
					float temp[] = {triangles[i][0].z,triangles[i][1].z,triangles[i][2].z};
					Vector3f.cross(triangles[i][0],new Vector3f(0,0,1), triangles[i][0]);
					Vector3f.cross(triangles[i][1],new Vector3f(0,0,1), triangles[i][1]);
					Vector3f.cross(triangles[i][2],new Vector3f(0,0,1), triangles[i][2]);
					triangles[i][0].z = temp[0];
					triangles[i][1].z = temp[1];
					triangles[i][2].z = temp[2];
				}
			}
		}
	}
	
	
	public void render() {
		if(cullFaces) {
			glEnable(GL_CULL_FACE);
		} else {
			glDisable(GL_CULL_FACE);
		}
		if(texture != null) texture.bind();
		glPushMatrix();
		if(static_object) {
			glTranslatef(Roget.getCamera().pos.x, Roget.getCamera().pos.y, Roget.getCamera().pos.z);
		}
		glTranslatef(pos.x, pos.y,pos.z);
		glScalef(scale.x, scale.y, scale.z);
		glRotatef(-quat.y, 0, 1, 0);
		glRotatef(-quat.z, 0, 0, 1);
		glRotatef(-quat.x, 1, 0, 0);
		//glRotatef(Roget.getCamera().quat.y, billboard[0]?-1:0, billboard[1]?-1:0, billboard[2]?-1:0);
		
		glBegin(GL_TRIANGLES); {
			for(short i=0; i<triangles.length; i++) {
				for(short j=0; j<triangles[i].length; j++) {
					glTexCoord2f(textureMaps[i][j].x,textureMaps[i][j].y);
					glVertex3f(triangles[i][j].x,triangles[i][j].y, triangles[i][j].z);
				}
			}
		}glEnd();
		glPopMatrix();
	}
	
	
	public void tick() {
    	if(texture == null && texturePath != null) {
    		texture = Texture.loadTexture(texturePath);
    	}
	}
	
	

	public void scale(Vector3f s) {
		scale.x *= s.x;
		scale.y *= s.y;
		scale.z *= s.z;
		for(Box b : boxes) {
			b.pos.x *= s.x;
			b.pos.y *= s.y;
			b.pos.z *= s.z;
			b.dim.x *= s.x;
			b.dim.y *= s.y;
			b.dim.z *= s.z;
		}
	}
	public void setScale(Vector3f s) {
		scale.x = s.x;
		scale.y = s.y;
		scale.z = s.z;
		for(Box b : boxes) {
			b.pos.x *= s.x;
			b.pos.y *= s.y;
			b.pos.z *= s.z;
			b.dim.x *= s.x;
			b.dim.y *= s.y;
			b.dim.z *= s.z;
		}
	}
	
	
	
	public static Article loadArticle(String filePath) {
		return loadArticlePiecewise(filePath, false);
	}
	
	public static Article loadArticlePiecewise(String filePath, boolean piecewise) {
		Article article = new Article(false);
		try {
			Scanner fileIn = new Scanner(new File(filePath));
			String line;
			String mode = null;
			Point index = new Point();
			while(fileIn.hasNext()) {
				line = fileIn.nextLine();
				if(line.indexOf('=')>=0) {
					String tags[] = line.split("=");
					mode = tags[0].toUpperCase();
					index = new Point();
					switch(mode.toUpperCase()) {
					case "NAME":
						article.name = tags[1];
						break;
					case "TEXTURE":
						if(piecewise) {
							article.texturePath = tags[1];
						} else {
							article.texture = Texture.loadTexture(tags[1]);
						}
						break;
					case "FACES":
						article.triangles = new Vector3f[Integer.parseInt(tags[1])][3];
						break;
					case "TEXTURE_MAPS":
						article.textureMaps = new Vector2f[Integer.parseInt(tags[1])][3];
						break;
					case "BOXES":
						article.boxes = new Box[Integer.parseInt(tags[1])];
						for(byte i=0; i<article.boxes.length; i++) {
							article.boxes[i] = new Box(null, null);
						}
						break;
					case "CULL":
						article.cullFaces = Boolean.parseBoolean(tags[1]);
						break;
					case "STATIC":
						article.static_object = Boolean.parseBoolean(tags[1]);
						break;
					case "BILLBOARD":
						article.billboard = new boolean[3];
						article.billboard[0] = tags[1].toUpperCase().indexOf('X')>=0;
						article.billboard[1] = tags[1].toUpperCase().indexOf('Y')>=0;
						article.billboard[2] = tags[1].toUpperCase().indexOf('Z')>=0;
						break;
					case "LIGHTS":
						
						break;
					case "OBJ":
						article = new VBOArticle(tags[1]);
						break;
					}
				}
				else if(mode != null) {
					String values[];
					switch(mode.toUpperCase()) {
					case "FACES":
						values = line.split("\\|");
						article.triangles[index.y/3][index.x%3] = new Vector3f(
								Float.parseFloat(values[0]),
								Float.parseFloat(values[1]),
								Float.parseFloat(values[2]));
						index.x++;
						index.y++;
						break;
					case "TEXTURE_MAPS":
						values = line.split("/");
						article.textureMaps[index.y/3][index.x%3] = new Vector2f(
								Float.parseFloat(values[0]),
								Float.parseFloat(values[1])
								);
						index.x++;
						index.y++;
						break;
					case "BOXES":
						values = line.split("\\|");
						if(index.x%2==0) {
							article.boxes[index.y].pos = new Vector3f(
									Float.parseFloat(values[0]),
									Float.parseFloat(values[1]),
									Float.parseFloat(values[2]));
						}else {
							article.boxes[index.y].dim = new Vector3f(
									Float.parseFloat(values[0]),
									Float.parseFloat(values[1]),
									Float.parseFloat(values[2]));
							index.y++;
						}
						index.x++;
						break;
					}
				}
			}
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		article.velocity = new Vector3f();
		article.pos = new Vector3f();
		for(byte i=0; i<article.boxes.length; i++) {
			article.boxes[i].article = article;
		}
		return article;
	}
	
	
	public String toString() {
		return name;
	}
	
	
	
}



