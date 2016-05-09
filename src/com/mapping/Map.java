package com.mapping;

import java.util.ArrayList;

import com.preAlpha.Article;

public class Map {
	
	public static ArrayList<Article> articles;
	
	
	public static Article getArticleById(String str) {
		for(Article art : articles) {
			if(art.id.equals(str)) {
				return art;
			}
		}
		return null;
	}
	
	
	
	
}
