package com.mapping;


import java.util.ArrayList;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.preAlpha.Article;
import com.preAlpha.Box;
import com.preAlpha.Texture;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
 
public class RogetMapper {
    private static long window;
    static ArrayList<Article> articles;
    static int cameraId = 0;
    private static Vector2f mousePos;
    private static boolean[] keys = {false,false,false,false,false,false};
    private static boolean sprint = false;
    private static boolean firstPerson = true;
    
    private static void mainLoop() {
    	long timeStart = System.currentTimeMillis();
    	long timeCurr = 0;
    	final int TIME_OFFSET = 20;
        while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
            if((timeStart+TIME_OFFSET)<timeCurr) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        		glPushMatrix();
	            if(!firstPerson) {
	            	glTranslatef(0,-2f,-4f);
	            }
                glRotatef(articles.get(cameraId).quat.x,1,0,0);
                glRotatef(articles.get(cameraId).quat.y,0,1,0);
                glTranslatef(-articles.get(cameraId).pos.x,-articles.get(cameraId).pos.y,-articles.get(cameraId).pos.z);
    	            for(Article a : articles) {
    	            	if(a.texture == null && a.texturePath != null) {
    	            		a.texture = Texture.loadTexture(a.texturePath);
    	            	}
    	            	a.render();
    	            }
        		glPopMatrix();
                glfwSwapBuffers(window);
                glfwPollEvents();
            	tick();
            }
        	timeCurr = System.currentTimeMillis();
        }
    }
    
    
    public static void tick() {
    	Article camera = getCamera();
    	float speed = 0.02f;
    	if(keys[0]) {
			camera.velocity.z-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*speed;
			camera.velocity.x+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*speed;
    	}
    	if(keys[1]) {
    		camera.velocity.x-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.02f;
    		camera.velocity.z-=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.02f;
    	}
    	if(keys[2]) {
    		camera.velocity.z+=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.02f;
    		camera.velocity.x-=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.02f;
    	}
    	if(keys[3]) {
    		camera.velocity.x+=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.02f;
    		camera.velocity.z+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.02f;
    	}if(keys[4]) {
			camera.velocity.y+=0.02f;
    	}if(keys[5]) {
			camera.velocity.y-=0.02f;
    	}
		if(sprint) {
			camera.velocity.x = 0;
			camera.velocity.y = 0;
			camera.velocity.z = 0;
		}
		
		for(Article a : articles) {
			Vector3f.add(a.pos, a.velocity, a.pos);
		}
    	
    }
    
    
    
    
    
    public static void modelView(int x, int y, int w, int h) {
    	try {
        	if (glfwInit() == GLFW_TRUE) {
        		glfwDefaultWindowHints();
        		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        		window = glfwCreateWindow(w, h, "", NULL, NULL);
        		glfwSetWindowPos(window, x, y);
        		if(window == NULL) {
        			System.out.println("Broke Window");
        		} else {
        			KeyHandler keyHandler = new KeyHandler();
        			glfwSetKeyCallback(window, keyHandler);
        			MouseMoveHandler mouseMoveHandler = new MouseMoveHandler();
        			glfwSetCursorPosCallback(window, mouseMoveHandler);
        			MouseButtonHandler mouseButtonHandler = new MouseButtonHandler();
        			glfwSetMouseButtonCallback(window, mouseButtonHandler);
                    glfwMakeContextCurrent(window);
                    glfwSwapInterval(1);
                    glfwShowWindow(window);
                    GL.createCapabilities();
                    
                    articles = new ArrayList<Article>();
                    Article camera = new Article(false);
                    camera.cullFaces = true;
                    articles.add(camera);
                    mousePos = new Vector2f();
                    
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    final float aspect = w/h;
                    final float fH = (float)(Math.tan(0.25*Math.PI)*0.01f);
                    final float fW = fH*aspect;
                    glFrustum(-fW, fW, -fH, fH, 0.01f, 1000);
                    glViewport(0, 0, w, h);
                    glMatrixMode(GL_MODELVIEW);
                    glEnable(GL_DEPTH_TEST);
                    glEnable(GL_CULL_FACE);
                    glEnable(GL_TEXTURE_2D);
                    glEnable(GL_BLEND);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    
                    
                    mainLoop();
                    for(Article a : articles) {
                    	if(a.texture != null) a.texture.delete();
                    }
                    glfwDestroyWindow(window);
        		}
        	}
        } finally {
            glfwTerminate();
        }
    	System.exit(0);
    }
    
    public static Article getCamera() {
    	return articles.get(cameraId);
    }
    
    
    public static void GenerateBoundBoxArticles() {
    	ArrayList<Article> boxes = new ArrayList<Article>();
    	for(Article art : articles) {
    		for(Box box : art.boxes) {
    			boxes.add(box.buildArticle());
    		}
    	}
    	for(Article b : boxes) {
    		articles.add(b);
    	}
    }
    
    public static void keyPress(int key, int scancode) {
    	final boolean flag = true;
    	switch(key) {
		case GLFW_KEY_W:
			RogetMapper.keys[0] = flag;
			break;
		case GLFW_KEY_A:
			RogetMapper.keys[1] = flag;
			break;
		case GLFW_KEY_S:
			RogetMapper.keys[2] = flag;
			break;
		case GLFW_KEY_D:
			RogetMapper.keys[3] = flag;
			break;
		case GLFW_KEY_SPACE:
			RogetMapper.keys[4] = flag;
			break;
		case GLFW_KEY_LEFT_ALT:
			RogetMapper.sprint = true;
			break;
		case GLFW_KEY_LEFT_SHIFT:
			RogetMapper.keys[5] = flag;
			break;
		case GLFW_KEY_RIGHT_SHIFT:
			break;
		case GLFW_KEY_ESCAPE:
			glfwSetWindowShouldClose(window, GLFW_TRUE);
			break;
		}
    }
    public static void keyRelease(int key, int scancode) {
    	final boolean flag = false;
    	switch(key) {
		case GLFW_KEY_W:
			RogetMapper.keys[0] = flag;
			break;
		case GLFW_KEY_A:
			RogetMapper.keys[1] = flag;
			break;
		case GLFW_KEY_S:
			RogetMapper.keys[2] = flag;
			break;
		case GLFW_KEY_D:
			RogetMapper.keys[3] = flag;
			break;
		case GLFW_KEY_SPACE:
			RogetMapper.keys[4] = flag;
			break;
		case GLFW_KEY_LEFT_ALT:
			RogetMapper.sprint = false;
			break;
		case GLFW_KEY_LEFT_SHIFT:
			RogetMapper.keys[5] = flag;
			break;
		case GLFW_KEY_ESCAPE:
			glfwSetWindowShouldClose(window, GLFW_TRUE);
			break;
		}
    }
    
    static boolean pressed = false;
    public static void mousePress(int button) {
    	if(button == GLFW_MOUSE_BUTTON_3) {
        	pressed=true;
    	}
    }
    public static void mouseRelease(int button) {
    	if(button == GLFW_MOUSE_BUTTON_3) {
        	pressed=false;
    	}
    }
    
    public static void mouseMoved(Vector2f pos) {
    	if(pressed) {
		    	Vector2f temp = new Vector2f();
		    	temp.y=mousePos.x-pos.x;
		    	temp.x=mousePos.y-pos.y;
		    	float xRot = articles.get(cameraId).quat.x-(temp.x/10);
	    		if(xRot>-87 && xRot<87) {
	    			articles.get(cameraId).quat.x = xRot;
	    		}
		    	articles.get(cameraId).quat.y -= temp.y/10;
    	}
    	mousePos = pos;
    }
    
    
    static class KeyHandler extends GLFWKeyCallback {
    	@Override
    	public void invoke(long window, int key, int scancode, int action, int mods) {
    		switch(action) {
    		case GLFW_PRESS:
    			RogetMapper.keyPress(key, scancode);
    			break;
    		case GLFW_RELEASE:
    			RogetMapper.keyRelease(key, scancode);
    			break;
    		case GLFW_REPEAT:
    			RogetMapper.keyPress(key, scancode);
    			break;
    		}
    	}
    }
    static class MouseMoveHandler extends GLFWCursorPosCallback {
		@Override
		public void invoke(long window, double xpos, double ypos) {
			RogetMapper.mouseMoved(new Vector2f((float)xpos, (float)ypos));
		}
    }
    static class MouseButtonHandler extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			switch(action) {
			case GLFW_PRESS:
    			RogetMapper.mousePress(button);
    			break;
    		case GLFW_RELEASE:
    			RogetMapper.mouseRelease(button);
    			break;
    		}
		}
    }
}

