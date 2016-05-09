package com.modeling;


import java.util.ArrayList;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.preAlpha.Article;
import com.preAlpha.Texture;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
 
public class RogetModel {
    private static long window;
    private static final Dimension FRAME_DIM = new Dimension(1000,800);
    static ArrayList<Article> articles;
    private static int cameraId = 0;
    private static Vector2f mousePos;
    static Article arrow;
    static String path = "res\\images\\crate.png";
    
    private static void mainLoop() {
        while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
            /*glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    		glPushMatrix();
            glRotatef(articles.get(cameraId).quat.x,1,0,0);
            glRotatef(articles.get(cameraId).quat.y,0,1,0);
            glTranslatef(-articles.get(cameraId).pos.x,-articles.get(cameraId).pos.y,-articles.get(cameraId).pos.z);
	            for(Article a : articles) {
	            	a.render();
	            }
    		glPopMatrix();
            glfwSwapBuffers(window);
            glfwPollEvents();*/
            long timeStart = System.currentTimeMillis();
        	long timeCurr = 0;
        	final int TIME_OFFSET = 40;
            while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
                if((timeStart+TIME_OFFSET)<timeCurr) {
                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            		glPushMatrix();
                    glRotatef(articles.get(cameraId).quat.x,1,0,0);
                    glRotatef(articles.get(cameraId).quat.y,0,1,0);
                    glTranslatef(-articles.get(cameraId).pos.x,-articles.get(cameraId).pos.y,-articles.get(cameraId).pos.z);
        	            for(Article a : articles) {
        	            	a.render();
        	            }
            		glPopMatrix();
                    glfwSwapBuffers(window);
                    glfwPollEvents();
                    articles.get(0).texture = Texture.loadTexture(path);
                }
            	timeCurr = System.currentTimeMillis();
            }
        }
    }
    
    public static void modelView(int x, int y, int w, int h, Article arts) {
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
                    if(arts == null) {
                    	arts = new Article(false);
                    }
                    Article camera = new Article();
                    camera.cullFaces = true;
                    articles.add(camera);
                    camera.pos.z+=2;
                    articles.add(arts);
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
                    
                    
                    arrow = new Article(false);
					arrow.texture = Texture.loadTexture("res\\images\\normalArrow.png");
					arrow.cullFaces = false;
					arrow.boxes = null;
					
					
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
    
    public static void setFlags(byte x) {
    	final int[] settings = {
    		GL_CULL_FACE
    	};
    	for(byte i=0;i<8;x/=i++) {
    		if(x%2==0) {
    			glDisable(settings[i]);
    		}else {
    			glEnable(settings[i]);
    		}
    		x/=2;
    	}
    	glDisable(GL_CULL_FACE);
    }
    
    public static void keyPress(int key, int scancode) {
    	switch(key) {
		case GLFW_KEY_W:
			articles.get(cameraId).pos.z-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.1f;
			articles.get(cameraId).pos.x+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.1f;
			break;
		case GLFW_KEY_A:
			articles.get(cameraId).pos.x-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.1f;
			articles.get(cameraId).pos.z-=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.1f;
			break;
		case GLFW_KEY_S:
			articles.get(cameraId).pos.z+=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.1f;
			articles.get(cameraId).pos.x-=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.1f;
			break;
		case GLFW_KEY_D:
			articles.get(cameraId).pos.x+=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.1f;
			articles.get(cameraId).pos.z+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.1f;
			break;
		case GLFW_KEY_SPACE:
			articles.get(cameraId).pos.y+=0.1f;
			break;
		case GLFW_KEY_LEFT_SHIFT:
			articles.get(cameraId).pos.y-=0.1f;
			break;
		case GLFW_KEY_ESCAPE:
			glfwSetWindowShouldClose(window, GLFW_TRUE);
			break;
		}
    }
    public static void keyRelease(int key, int scancode) {
    }

    public static void mousePress(int button) {
    	if(button==GLFW_MOUSE_BUTTON_3) {
    		pressed=true;
    	}
    	else if(button == GLFW_MOUSE_BUTTON_2) {
    		for(Vector3f v = new Vector3f(articles.get(cameraId).pos); Vector3f.sub(v, articles.get(cameraId).pos, null).length()<10;) {
        		ArrayList<Vector3f> pointSelected = new ArrayList<Vector3f>();
    			for(Plane p : Modeler.planes) {
    				for(Vector3f vF : p.points) {
    					if(Vector3f.sub(v, vF, null).length()<0.01f) {
    						pointSelected.add(vF);
	    				}
    				}
    			}
    			if(pointSelected.size()>0) {
					Article a = Article.loadArticle("res\\assets\\square.dmf");
					v.z-=1;
					a.pos = new Vector3f(v);
					articles.add(a);
					break;
    			}
    			v.z-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.01f;
    			v.x+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.01f;
    		}
    	}
    }
    public static void mouseRelease(int button) {
    	if(button==GLFW_MOUSE_BUTTON_3) {
    		pressed=false;
    	}
    }
    static boolean pressed = false;
    public static void mouseMoved(Vector2f pos) {
    	if(pressed) {
		    	Vector2f temp = new Vector2f();
		    	temp.y=mousePos.x-pos.x;
		    	temp.x=mousePos.y-pos.y;
		    	float xRot = articles.get(cameraId).quat.x+(temp.x/10);
	    		if(xRot>-90 && xRot<90) {
	    			articles.get(cameraId).quat.x = xRot;
	    		}
		    	articles.get(cameraId).quat.y += temp.y/10;
    	}
    	mousePos = pos;
    }
    
    
    static class KeyHandler extends GLFWKeyCallback {
    	@Override
    	public void invoke(long window, int key, int scancode, int action, int mods) {
    		switch(action) {
    		case GLFW_PRESS:
    			RogetModel.keyPress(key, scancode);
    			break;
    		case GLFW_RELEASE:
    			RogetModel.keyRelease(key, scancode);
    			break;
    		case GLFW_REPEAT:
    			RogetModel.keyPress(key, scancode);
    			break;
    		}
    	}
    }
    static class MouseMoveHandler extends GLFWCursorPosCallback {
		@Override
		public void invoke(long window, double xpos, double ypos) {
			RogetModel.mouseMoved(new Vector2f((float)xpos, (float)ypos));
		}
    }
    static class MouseButtonHandler extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			switch(action) {
			case GLFW_PRESS:
    			RogetModel.mousePress(button);
    			break;
    		case GLFW_RELEASE:
    			RogetModel.mouseRelease(button);
    			break;
    		}
		}
    }
}

