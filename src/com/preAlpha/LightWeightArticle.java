package com.preAlpha;


import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public 
class LightWeightArticle extends Article {
	
	
	/**A physical rotation model that is both visual and effects bounding boxes.*/
	public Vector3f rotation;
	/**Used for modeling to make sure bounding boxes are visible.*/
	public boolean transparent = false;
	/**The rotation of the article and the order in which it should occur.*/
	public String rotateOrder = "";
	
	public LightWeightArticle(VBOArticle copy) {
		super();
		name = copy.name;
		pos = new Vector3f();
		quat = new Vector4f(0,0,0,0);
		rotation = new Vector3f(0,0,0);
		billboard = new boolean[] {false, false, false};
		cullFaces = false;
		velocity = new Vector3f();
		boxes = copy.boxes;
		scale = copy.scale;
		copy.instances.add(this);
	}
	
	
	@Override
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
	
	@Override
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
	
	@Override
	public void rotate(String rot) {
		String[] tags = rot.split("=");
		for(byte i=0; i<tags.length; i++) {
			String axis = tags[i].substring(tags[i].indexOf(";")+1);
			rotateOrder.replace(axis, "");
			rotateOrder = axis + rotateOrder;
			if(axis.toUpperCase().equals("X")) {
				rotation.x += Float.parseFloat(tags[i+1].substring(0, tags[i+1].indexOf(";")));
				rotateBoxes((byte) 0, rotation.x);
			}
			else if(axis.toUpperCase().equals("Y")) {
				rotation.y += Float.parseFloat(tags[i+1].substring(0, tags[i+1].indexOf(";")));
				rotateBoxes((byte) 1, rotation.y);
			}
			else if(axis.toUpperCase().equals("Z")) {
				rotation.z += Float.parseFloat(tags[i+1].substring(0, tags[i+1].indexOf(";")));
				rotateBoxes((byte) 2, rotation.z);
			}
		}
	}
	
	
	public void rotateBoxes(byte axis, float angle) {
		switch(axis) {
		case 0:	//X
			Box[] boxList = new Box[boxes.length];
			for(short i=0; i<boxes.length; i++) {
				boxList[i] = new Box(boxes[i]);
				if(Math.abs(angle)>45 && Math.abs(angle)<135) {
					float temp = boxList[i].pos.y;
					boxList[i].pos.y = boxList[i].pos.z;
					boxList[i].pos.z= temp;
					temp = boxList[i].dim.y;
					boxList[i].dim.y = boxList[i].dim.z;
					boxList[i].dim.z= temp;
					if(angle<0) {
						boxList[i].pos.y = -boxList[i].pos.y - boxList[i].dim.y;
						boxList[i].pos.z = -boxList[i].pos.z - boxList[i].dim.z;
					}
				}
				scale = new Vector3f(scale.x, scale.z, scale.y);
			}
			boxes = boxList;
			break;
		case 1:	//Y
			boxList = new Box[boxes.length];
			for(short i=0; i<boxes.length; i++) {
				boxList[i] = new Box(boxes[i]);
				if(Math.abs(angle)>45 && Math.abs(angle)<135) {
					float temp = boxList[i].pos.x;
					boxList[i].pos.x = boxList[i].pos.z;
					boxList[i].pos.z= temp;
					temp = boxList[i].dim.x;
					boxList[i].dim.x = boxList[i].dim.z;
					boxList[i].dim.z= temp;
					if(angle<0) {
						boxList[i].pos.x = -boxList[i].pos.x - boxList[i].dim.x;
						boxList[i].pos.z = -boxList[i].pos.z - boxList[i].dim.z;
					}
				}
				scale = new Vector3f(scale.z, scale.y, scale.x);
			}
			boxes = boxList;
			break;
		case 2:	//Z
			boxList = new Box[boxes.length];
			for(short i=0; i<boxes.length; i++) {
				boxList[i] = new Box(boxes[i]);
				if(Math.abs(angle)>45 && Math.abs(angle)<135) {
					float temp = boxList[i].pos.x;
					boxList[i].pos.x = boxList[i].pos.y;
					boxList[i].pos.y = temp;
					temp = boxList[i].dim.x;
					boxList[i].dim.x = boxList[i].dim.y;
					boxList[i].dim.y = temp;
					if(angle>0) {
						boxList[i].pos.x = -boxList[i].pos.x - boxList[i].dim.x;
						boxList[i].pos.y = -boxList[i].pos.y - boxList[i].dim.y;
					}
				}
				scale = new Vector3f(scale.y, scale.x, scale.z);
			}
			boxes = boxList;
			break;
		}
	}
			
	
	
	
	@Override
	public void tick() {
	}
}


class LWArticle extends LightWeightArticle {

	public LWArticle(VBOArticle copy) {
		super(copy);
	}
	
}
