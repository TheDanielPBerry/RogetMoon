package com.preAlpha;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class ArticleCollection extends Article {
	
	public ArrayList<Article> collection;
	public Vector3f pos;
	public Vector3f velocity;
	
	public ArticleCollection(ArrayList<Article> articles) {
		collection = articles;
	}
	
	
	public void tick() {
		Vector3f.add(velocity, pos, pos);
		for(Article a : collection) {
			Vector3f.add(a.velocity, velocity, a.velocity);
			a.tick();
			Vector3f.sub(a.velocity, velocity, a.velocity);
		}
	}
	
	
}
