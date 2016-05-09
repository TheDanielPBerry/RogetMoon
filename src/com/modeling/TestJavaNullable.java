package com.modeling;

public class TestJavaNullable {
	
	
	public static void main(String[] args) {
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				System.out.println("Go Fawk Yaself");
			}
		};
		
		
		Runnable r2 = () -> System.out.println("Fuhgetaboudit.");
		
		
		
		
		r1.run();
		r2.run();
	}
	

	public void run() {
		
	}
	
	public TestJavaNullable() {
		
	}
	
}
