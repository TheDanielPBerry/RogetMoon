package com.preAlpha;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;

public class OBJArticle extends Article {
	
	
	
	public OBJArticle() {
		
	}
	
	
	
	
	public static Face[] loadOBJArticle(String filePath) {
		return loadOBJArticle(new File(filePath));
	}
	
	public static Face[] loadOBJArticle(File f) {
		Article a = new Article();
		a.cullFaces = true;

		ArrayList<Vector3f> geometry = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
		ArrayList<Face> faces = new ArrayList<Face>();
		
		try {
			BufferedReader fileIn = new BufferedReader(new FileReader(f));
			String line = "";
			while((line = fileIn.readLine()) != null) {
				String tags[] = line.split(" ");
				switch(tags[0]) {
				case "#":
					break;
				case "v":
					geometry.add(parseVector3f(tags[1],tags[2],tags[3]));
					break;
				case "vn":
					normals.add(parseVector3f(tags[1],tags[2],tags[3]));
					break;
				case "vt":
					textures.add(parseVector2f(tags[1],tags[2]));
					break;
				case "f":
					Face plane = new Face();
					plane.vertices = new Vertex[tags.length-1];
					for(byte i=1; i<tags.length; i++) {
						plane.vertices[i-1] = parseVertex(tags[i], geometry, normals, textures);
					}
					faces.add(plane);
					break;
				case "s":
					break;
				case "o":
					break;
				case "usemtl":
					break;
				case "mtllib":
					break;
				}
			}
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Face[] result = new Face[faces.size()];
		result = faces.toArray(result);
		
		return result;
	}
	
	
	public static String loadOBJTexture(String filePath) {	
		return loadOBJTexture(new File(filePath));
	}
public static String loadOBJTexture(File f) {
		String result = "";
		
		HashMap<String, String> materialLibrary = null;
		try {
			BufferedReader fileIn = new BufferedReader(new FileReader(f));
			String line = "";
			while((line = fileIn.readLine()) != null) {
				String tags[] = line.split(" ");
				switch(tags[0]) {
				case "#":
					break;
				case "usemtl":
					result = materialLibrary.get(tags[1]);
					break;
				case "mtllib":
					materialLibrary = loadMaterialLibrary(f.getAbsolutePath().replace(f.getName(), tags[1]));
					break;
				}
			}
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static HashMap<String,String> loadMaterialLibrary(String filePath) {
		HashMap<String, String> map = new HashMap<String,String>();
		try {
			BufferedReader fileIn = new BufferedReader(new FileReader(new File(filePath)));
			String line = "";
			String[] material = new String[2];
			while((line = fileIn.readLine()) != null) {
				String tags[] = line.split(" ");
				switch(tags[0]) {
				case "#":
					break;
				case "newmtl":
					material[0] = tags[1];
					break;
				case "map_Kd":
					material[1] = tags[1];
					map.put(material[0], material[1]);
					material = new String[2];
					break;
				}
			}
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Vertex parseVertex(String l, ArrayList<Vector3f> geometry, ArrayList<Vector3f> normals, ArrayList<Vector2f> textures) {
		String[] tags = l.split("/");
		Vertex v = new Vertex();
		v.pos = geometry.get(Integer.parseInt(tags[0])-1);
		v.textureMap = textures.get(Integer.parseInt(tags[1])-1);
		v.normal = normals.get(Integer.parseInt(tags[2])-1);
		return v;
	}
	

	public static Vector3f parseVector3f(String x, String y, String z) {
		Vector3f v = new Vector3f();
		try {
			v.x = Float.parseFloat(x);
		}catch(NumberFormatException e) {}
		try {
			v.y = Float.parseFloat(y);
		}catch(NumberFormatException e) {}
		try {
			v.z = Float.parseFloat(z);
		}catch(NumberFormatException e) {}
		return v;
	}
	
	public static Vector2f parseVector2f(String x, String y) {
		Vector2f v = new Vector2f();
		try {
			v.x = Float.parseFloat(x);
		}catch(NumberFormatException e) {}
		try {
			v.y = Float.parseFloat(y);
		}catch(NumberFormatException e) {}
		return v;
	}
	
	
}

class Face {
	
	public Vertex[] vertices;
	
	public String toString() {
		String s = "==============\n";
		for(Vertex v : vertices) {
			s += v.toString() + "\n";
		}
		return s;
	}
	
	public void setNormals() {
		Vector3f n = Vertex.calculateNormal(new Vector3f[] {vertices[0].pos,vertices[1].pos,vertices[2].pos});
		for(byte i=0; i<vertices.length; i++) {
			vertices[i].normal = n;
		}
	}
	
	public Box genBox() {
		Vector3f top = new Vector3f(vertices[0].pos);
		Vector3f bottom = new Vector3f(vertices[0].pos);
		for(Vertex v : vertices) {
			if(v.pos.x>top.x) {
				top.x = v.pos.x;
			}
			if(v.pos.x<bottom.x) {
				bottom.x = v.pos.x;
			}
			if(v.pos.y>top.y) {
				top.y = v.pos.y;
			}
			if(v.pos.y<bottom.y) {
				bottom.y = v.pos.y;
			}
			if(v.pos.z>top.z) {
				top.z = v.pos.z;
			}
			if(v.pos.z<bottom.z) {
				bottom.z = v.pos.z;
			}
		}
		top = Vector3f.sub(top, bottom, top);
		return new Box(bottom, top);
	}
	
}
