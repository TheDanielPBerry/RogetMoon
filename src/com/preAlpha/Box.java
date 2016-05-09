package com.preAlpha;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Box {
	public Article article;
	public Vector3f pos;
	public Vector3f dim;
	
	public Box(final Vector3f top, final Vector3f bottom) {
		pos = top;
		dim = bottom;
	}
	public Box(Box b) {
		pos = new Vector3f(b.pos.x, b.pos.y, b.pos.z);
		dim = new Vector3f(b.dim.x, b.dim.y, b.dim.z);
	}
	

	public Article buildArticle() {
		return buildArticle(article.pos);
	}
	public Article buildArticle(Vector3f position) {
		Article art = new Article(false);
		art.pos = position;
		art.cullFaces = false;
		art.boxes = new Box[0];
		art.name = "Bounding Box";
		//art.velocity = article.velocity;
		art.texture = Texture.loadTexture("res\\images\\boundingBox.png");
		Vector3f[] corners = {pos, new Vector3f(pos.x, pos.y, pos.z+dim.z), 
				new Vector3f(pos.x+dim.x, pos.y, pos.z), new Vector3f(pos.x+dim.x, pos.y, pos.z+dim.z), 
				new Vector3f(pos.x, pos.y+dim.y, pos.z), new Vector3f(pos.x, pos.y+dim.y, pos.z+dim.z), 
				new Vector3f(pos.x+dim.x, pos.y+dim.y, pos.z), new Vector3f(pos.x+dim.x, pos.y+dim.y, pos.z+dim.z)};
		art.triangles = new Vector3f[][] {
				{
					pos, corners[1], corners[3]
				},{
					pos, corners[2], corners[3]
				},{
					pos, corners[4], corners[2]
				},{
					pos, corners[6], corners[2]
				},{
					corners[7], corners[6], corners[4]
				},{
					corners[7], corners[5], corners[4]
				},{
					corners[7], corners[2], corners[3]
				},{
					corners[7], corners[2], corners[6]
				},{
					corners[7], corners[1], corners[3]
				},{
					corners[7], corners[1], corners[5]
				}
		};
		art.textureMaps = new Vector2f[][] {
				{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				},{
					new Vector2f(0,0),new Vector2f(1,0),new Vector2f(0,1)
				}
		};
		return art;
	}
	static Article cox = Article.loadArticle("res\\assets\\crate.dmf");
	
	
	public boolean intersects(Box b) {
		return ((article.pos.x+pos.x+article.velocity.x)<(b.article.pos.x+b.pos.x+b.dim.x+b.article.velocity.x)) && ((article.pos.x+pos.x+dim.x+article.velocity.x)>(b.article.pos.x+b.pos.x+b.article.velocity.x)) &&
				((article.pos.y+pos.y+article.velocity.y)<(b.article.pos.y+b.pos.y+b.dim.y+b.article.velocity.y)) && ((article.pos.y+pos.y+dim.y+article.velocity.y)>(b.article.pos.y+b.pos.y+b.article.velocity.y)) &&
				((article.pos.z+pos.z+article.velocity.z)<(b.article.pos.z+b.pos.z+b.dim.z+b.article.velocity.z)) && ((article.pos.z+pos.z+dim.z+article.velocity.z)>(b.article.pos.z+b.pos.z+b.article.velocity.z));
		
	}public boolean intersectsX(Box b) {
		return ((article.pos.x+pos.x+article.velocity.x)<(b.article.pos.x+b.pos.x+b.dim.x)) && ((article.pos.x+pos.x+dim.x+article.velocity.x)>(b.article.pos.x+b.pos.x)) &&
				((article.pos.y+pos.y)<(b.article.pos.y+b.pos.y+b.dim.y)) && ((article.pos.y+pos.y+dim.y)>(b.article.pos.y+b.pos.y)) &&
				((article.pos.z+pos.z)<(b.article.pos.z+b.pos.z+b.dim.z)) && ((article.pos.z+pos.z+dim.z)>(b.article.pos.z+b.pos.z));
	}public boolean intersectsY(Box b) {
		return ((article.pos.x+pos.x)<(b.article.pos.x+b.pos.x+b.dim.x)) && ((article.pos.x+pos.x+dim.x)>(b.article.pos.x+b.pos.x)) &&
				((article.pos.y+pos.y+article.velocity.y)<(b.article.pos.y+b.pos.y+b.dim.y)) && ((article.pos.y+pos.y+dim.y+article.velocity.y)>(b.article.pos.y+b.pos.y)) &&
				((article.pos.z+pos.z)<(b.article.pos.z+b.pos.z+b.dim.z)) && ((article.pos.z+pos.z+dim.z)>(b.article.pos.z+b.pos.z));
	}public boolean intersectsZ(Box b) {
		return ((article.pos.x+pos.x)<(b.article.pos.x+b.pos.x+b.dim.x)) && ((article.pos.x+pos.x+dim.x)>(b.article.pos.x+b.pos.x)) &&
				((article.pos.y+pos.y)<(b.article.pos.y+b.pos.y+b.dim.y)) && ((article.pos.y+pos.y+dim.y)>(b.article.pos.y+b.pos.y)) &&
				((article.pos.z+pos.z+article.velocity.z)<(b.article.pos.z+b.pos.z+b.dim.z)) && ((article.pos.z+pos.z+dim.z+article.velocity.z)>(b.article.pos.z+b.pos.z));
	}
	public boolean intersectsFrameX(Box b, Vector3f topPos, Vector3f bottomPos, float velocity) {
		return ((topPos.x+pos.x+velocity)<(bottomPos.x+b.pos.x+b.dim.x)) && ((topPos.x+pos.x+dim.x+velocity)>(bottomPos.x+b.pos.x)) &&
				((topPos.y+pos.y)<(bottomPos.y+b.pos.y+b.dim.y)) && ((topPos.y+pos.y+dim.y)>(bottomPos.y+b.pos.y)) &&
				((topPos.z+pos.z)<(bottomPos.z+b.pos.z+b.dim.z)) && ((topPos.z+pos.z+dim.z)>(bottomPos.z+b.pos.z));
	}public boolean intersectsFrameY(Box b, Vector3f topPos, Vector3f bottomPos, float velocity) {
		return ((topPos.x+pos.x)<(bottomPos.x+b.pos.x+b.dim.x)) && ((topPos.x+pos.x+dim.x)>(bottomPos.x+b.pos.x)) &&
				((topPos.y+pos.y+velocity)<(bottomPos.y+b.pos.y+b.dim.y)) && ((topPos.y+pos.y+dim.y+velocity)>(bottomPos.y+b.pos.y)) &&
				((topPos.z+pos.z)<(bottomPos.z+b.pos.z+b.dim.z)) && ((topPos.z+pos.z+dim.z)>(bottomPos.z+b.pos.z));
	}public boolean intersectsFrameZ(Box b, Vector3f topPos, Vector3f bottomPos, float velocity) {
		return ((topPos.x+pos.x)<(bottomPos.x+b.pos.x+b.dim.x)) && ((topPos.x+pos.x+dim.x)>(bottomPos.x+b.pos.x)) &&
				((topPos.y+pos.y)<(bottomPos.y+b.pos.y+b.dim.y)) && ((topPos.y+pos.y+dim.y)>(bottomPos.y+b.pos.y)) &&
				((topPos.z+pos.z+velocity)<(bottomPos.z+b.pos.z+b.dim.z)) && ((topPos.z+pos.z+dim.z+velocity)>(bottomPos.z+b.pos.z));
	}
}
