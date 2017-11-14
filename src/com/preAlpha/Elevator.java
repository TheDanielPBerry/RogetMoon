package com.preAlpha;

public class Elevator extends LWArticle {
	
	
	public float speed = 0.1f;
	public double oscillator = 0;
	public float oscillationFrequency = 0.01f;
	
	public Elevator(VBOArticle item) {
		super(item);
	}
	
	
	
	@Override
	public void tick() {
		velocity.y = (float) (Math.cos(oscillator)/Math.abs(Math.cos(oscillator))*speed);
		oscillator+=oscillationFrequency;
	}
	
}
