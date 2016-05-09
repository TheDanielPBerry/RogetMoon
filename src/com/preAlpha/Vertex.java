package com.preAlpha;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vertex {

	public Vector3f pos;
	public Vector3f normal;
	public Vector2f textureMap;

	public Vertex() {
		
	}
	public Vertex(Vector3f p, Vector3f n, Vector2f t) {
		pos = p;
		normal = n;
		textureMap = t;
	}

	public String toString() {
		String s = "";
		s += "\tv " + pos;
		s += "\n\tvn " + normal;
		s += "\n\tvt " + textureMap;
		return s;
	}
	
	public static Vector3f calculateNormal(Vector3f[] points) {
		
		return (Vector3f) Vector3f.cross(Vector3f.sub(points[0], points[1], null), Vector3f.sub(points[1], points[2], null), null).normalise();
	}
}
