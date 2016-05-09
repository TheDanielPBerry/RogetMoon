package com.preAlpha;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.xml.sax.InputSource;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
 
public class Roget {
    private static long window;
    private static final Dimension FRAME_DIM = new Dimension(1000,800);
    private static ArrayList<Article> articles;
    private static int cameraId = 0;
    private static Vector2f mousePos;
    private static boolean[] keys = {false,false,false,false,false,false,false};
    private static boolean jump = false;
    private static boolean sprint = false;
    private static boolean step = false;
    private static boolean firstPerson = true;
    private static final float stepHeight = 0.42f;
    private static final float gravityAcceleration = -0.01f;
    
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
                glTranslatef(-articles.get(cameraId).pos.x,-articles.get(cameraId).pos.y-0.09f,-articles.get(cameraId).pos.z);
    	            for(Article a : articles) {
    	            	if(!(a instanceof LWArticle)) {
    	            		a.render();
    	            	}
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
    	float speed = 0.05f;
    	if(keys[0]) {
    		if(sprint) {
    			speed=0.2f;
    		}
			camera.velocity.z-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*speed;
			camera.velocity.x+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*speed;
    	}
    	if(keys[1]) {
    		camera.velocity.x-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.05f;
    		camera.velocity.z-=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.05f;
    	}
    	if(keys[2]) {
    		camera.velocity.z+=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.05f;
    		camera.velocity.x-=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.05f;
    	}
    	if(keys[3]) {
    		camera.velocity.x+=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.05f;
    		camera.velocity.z+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.05f;
    	}if(keys[4]) {
    		jump = false;
    		if(camera.name.equals("Rocket")) {
    			camera.velocity.z-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*0.03;
    			camera.velocity.y+=Math.cos(Math.toRadians(articles.get(cameraId).quat.x))*0.03;
    			camera.velocity.x+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*0.03;
    		}else
    			camera.velocity.y = 0.1f;
    	}
    	if(camera.velocity.y<0) {
    		jump = false;
    		step = false;
    	}
		if(jump) {
	    	camera.velocity.x *= 0.6;
	    	camera.velocity.z *= 0.6;
		}else {
	    	camera.velocity.x *= 0.7;
	    	camera.velocity.z *= 0.7;
		}
    	for(Article a : articles) {
    		if(a.dynamic_object) {
				a.velocity.y += gravityAcceleration;
    		}
    	}
    	if(keys[6]) {
    		getCamera().velocity.y = 0;
    	}
    	
    	
    	collideX();
    	collideY();
    	collideZ();
    	
    	for(Article a : articles) {
    		a.tick();
    	}
    	
    }
    
    public static void collideX() {
    	for(short i=0; i<articles.size(); i++) {
			Article top = articles.get(i);
			if(!top.static_object && top.velocity.length()>0 && !(top instanceof VBOArticle)) {
	    		for(short j=0; j<articles.size(); j++) {
	    			if(i != j) {
	    				Article bottom = articles.get(j);
	    				if(Vector3f.sub(top.pos, bottom.pos, null).length()<90 && !(bottom instanceof VBOArticle)) {
		    				for(short k=0; k<top.boxes.length; k++) {
		    					for(short h=0; h<bottom.boxes.length; h++) {
		    						if(top.boxes[k].intersectsFrameX(bottom.boxes[h], top.pos, bottom.pos, top.velocity.x)) {
		    							if(i==cameraId && step && jump) {
		    								if(Math.abs((bottom.pos.y+bottom.boxes[h].pos.y+bottom.boxes[h].dim.y)-(top.pos.y+top.boxes[k].pos.y))<=stepHeight) {
			    								//top.velocity.y = (float)Math.sqrt(Math.abs((bottom.pos.y+bottom.boxes[h].pos.y+bottom.boxes[h].dim.y)-(top.pos.y+top.boxes[k].pos.y))*4*-(gravityAcceleration));
			    								//step = false;
			    								float yJump = ((bottom.pos.y+bottom.boxes[h].pos.y+bottom.boxes[h].dim.y)-(top.pos.y+top.boxes[k].pos.y));
	    										for(short l=0; l<articles.size(); l++) {
	    											if(l!=i) {
	    												for(short n=0; n<articles.get(l).boxes.length; n++) {
	    													for(short m=0; m<top.boxes.length; m++) {
		    													if(top.boxes[m].intersectsFrameY(articles.get(l).boxes[n], Vector3f.add(top.pos, new Vector3f(top.velocity.x,0,0), null), articles.get(l).pos, yJump)) {
		    			    										yJump = 0;
		    			    									}
	    													}
	    												}
	    											}
	    										}
	    										top.pos.y += yJump;
	    										if(yJump==0) {
				    								top.velocity.x = 0;
		    									}
		    								}else {
			    								top.velocity.x = 0;
			    							}
		    							}else {
		    								top.velocity.x = 0;
		    							}
		    						}
		    					}
		    				}
	    				}
	    			}
	    		}
	        	if(Math.abs(articles.get(i).velocity.x)<0.001) {
	        		articles.get(i).velocity.x = 0;
	        	}
	        	top.pos.x += top.velocity.x;
			}
    	}
    }
    
    public static void collideY() {
    	for(short i=0; i<articles.size(); i++) {
			Article top = articles.get(i);
			if(!top.static_object && top.velocity.length()>0 && !(top instanceof VBOArticle)) {
	    		for(short j=0; j<articles.size(); j++) {
	    			if(i != j) {
	    				Article bottom = articles.get(j);
	    				if(Vector3f.sub(top.pos, bottom.pos, null).length()<90 && !(bottom instanceof VBOArticle)) {
		    				for(short k=0; k<top.boxes.length; k++) {
		    					for(short h=0; h<bottom.boxes.length; h++) {
		    						if(top.boxes[k].intersectsFrameY(bottom.boxes[h], top.pos, bottom.pos, top.velocity.y)) {
		    							if(i==cameraId && top.velocity.y<0) {
		    								jump = true;
		    								step = true;
		    							}
	    								top.velocity.y = -top.velocity.y;
		    							break;
		    						}
		    					}
		    				}
	    				}
	    			}
	    		}
	        	if(Math.abs(articles.get(i).velocity.y)<0.001) {
	        		articles.get(i).velocity.y = 0;
	        	}
	        	top.pos.y += top.velocity.y;
			}
    	}
    }
    
    public static void collideZ() {
    	for(short i=0; i<articles.size(); i++) {
			Article top = articles.get(i);
			if(!top.static_object && top.velocity.length()>0 && !(top instanceof VBOArticle)) {
	    		for(short j=0; j<articles.size(); j++) {
	    			if(i != j) {
	    				Article bottom = articles.get(j);
		    				if(Vector3f.sub(top.pos, bottom.pos, null).length()<90 && !(bottom instanceof VBOArticle) && !(bottom.static_object)) {
		    				for(short k=0; k<top.boxes.length; k++) {
		    					for(short h=0; h<bottom.boxes.length; h++) {
		    						if(top.boxes[k].intersectsFrameZ(bottom.boxes[h], top.pos, bottom.pos, top.velocity.z)) {
		    							if(i==cameraId && step && jump) {
		    								if(Math.abs((bottom.pos.y+bottom.boxes[h].pos.y+bottom.boxes[h].dim.y)-(top.pos.y+top.boxes[k].pos.y))<=stepHeight) {
			    								//top.velocity.y = (float)Math.sqrt(Math.abs((bottom.pos.y+bottom.boxes[h].pos.y+bottom.boxes[h].dim.y)-(top.pos.y+top.boxes[k].pos.y))*4*-(gravityAcceleration));
			    								//step = false;
	    										float yJump = ((bottom.pos.y+bottom.boxes[h].pos.y+bottom.boxes[h].dim.y)-(top.pos.y+top.boxes[k].pos.y));
	    										for(short l=0; l<articles.size(); l++) {
	    											if(l!=i) {
	    												for(short n=0; n<articles.get(l).boxes.length; n++) {
	    													for(short m=0; m<top.boxes.length; m++) {
		    													if(top.boxes[m].intersectsFrameY(articles.get(l).boxes[n], Vector3f.add(top.pos, new Vector3f(0,0,top.velocity.z), null), articles.get(l).pos, yJump)) {
		    			    										yJump = 0;
		    			    									}
	    													}
	    												}
	    											}
	    										}
	    										top.pos.y += yJump;
	    										if(yJump==0) {
				    								top.velocity.z = 0;
		    									}
		    								}else {
			    								top.velocity.z = 0;
			    							}
		    							} else {
		    								top.velocity.z = 0;
		    							}
		    						}
		    					}
		    				}
	    				}
	    			}
	    		}
	        	if(Math.abs(articles.get(i).velocity.z)<0.001) {
	        		articles.get(i).velocity.z = 0;
	        	}
	        	top.pos.z += top.velocity.z;
			}
    	}
    }
    
    public static void main(String[] args) {
    	if(args.length>0) {
    		try {
				NewGame(new InputSource(new FileReader(new File(args[0]))));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
    	} else {
	        try {
	        	if (glfwInit() == GLFW_TRUE) {
	        		glfwDefaultWindowHints();
	        		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
	        		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
	        		window = glfwCreateWindow(FRAME_DIM.getWidth(), FRAME_DIM.getHeight(), "Hola Mundo!", NULL, NULL);
	        		if(window == NULL) {
	        			System.out.println("Broke Window");
	        		} else {
	        			KeyHandler keyHandler = new KeyHandler();
	        			glfwSetKeyCallback(window, keyHandler);
	        			MouseMoveHandler mouseMoveHandler = new MouseMoveHandler();
	        			glfwSetCursorPosCallback(window, mouseMoveHandler);
	        			MouseButtonHandler mouseButtonHandler = new MouseButtonHandler();
	        			glfwSetMouseButtonCallback(window, mouseButtonHandler);
	                    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	                    glfwSetWindowPos(window, (vidmode.width()-FRAME_DIM.getWidth())/2, (vidmode.height()-FRAME_DIM.getHeight())/2);
	                    glfwMakeContextCurrent(window);
	                    glfwSwapInterval(1);
	                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	                    glfwShowWindow(window);
	                    GL.createCapabilities();
	                    
	                    
				        /*GL11.glEnable(GL11.GL_NORMALIZE);
				        GL11.glEnable ( GL11.GL_LIGHTING );
		        		glShadeModel(GL_SMOOTH);*/
		        		
		        		
	                    articles = new ArrayList<Article>();
	                	VBOArticle player = (VBOArticle) Article.loadArticle("res\\assets\\Person.dmf");
	                	player.scale(new Vector3f(0.2f, 0.2f, 0.2f));
	                    LWArticle p1 = new LWArticle(player);
	                	p1.pos.x += 10;
	                	p1.pos.y += 10;
	                	p1.dynamic_object = true;
	                    articles.add(p1);
	                    articles.add(player);
	                    
	                   /* HeightMap h = new HeightMap("res\\images\\earth2.jpg", "res\\images\\earth2.jpg");
	                    h.scale(new Vector3f(0.5f, 0.5f, 0.5f));
	                    h.pos = new Vector3f(0,15,-120f);
	                    //h.rotate("X=90;");
	                    articles.add(h);*/
	                    HeightMap h2 = new HeightMap("res\\images\\landscape.png", "res\\images\\grass.png");
	                    h2.pos = new Vector3f(0,10,-50f);
	                    articles.add(h2);
	                    
	                    VBOArticle earth = new VBOArticle("res\\assets\\earth.obj");
	                	earth.cullFaces = true;
                		earth.scale(new Vector3f(10,10,10));
	                	articles.add(earth);
	                	
	                	VBOArticle ax = (VBOArticle) Article.loadArticle("res\\assets\\Sword.dmf");
	                	ax.scale(new Vector3f(0.05f,0.08f,0.05f));
                		articles.add(ax);
                		
                		
                		
                		for(float i=0; i<4*Math.PI; i+=0.1) {
	                		LWArticle sword = new LWArticle(ax);
	                		sword.dynamic_object = true;
		                	sword.pos = new Vector3f((float)Math.cos(i)*2+20f,i,(float)Math.sin(i)*2);
		                	sword.rotate("Y="+ (-i/Math.PI*180) + ";");
	                		articles.add(sword);
                		}
                		LWArticle sword = new LWArticle(ax);
                		sword.dynamic_object = true;
                		sword.rotate("Y=90;X=90;");
            			//sword.rotateOrder = "X" + sword.rotateOrder;
	                	sword.pos = new Vector3f(16,9f,3.5f);
                		articles.add(sword);
                		
                		
	                	for(short i=0; i<10; i++) {
	                		LWArticle cool = new LWArticle(earth);
		                	cool.pos = new Vector3f(-20f,12f,i*20);
	                		articles.add(cool);
	                	}
                		
	                    /*VBOArticle skyBox = (VBOArticle) Article.loadArticle("res\\assets\\SkyBox.dmf");
	                    articles.add(skyBox);
	                    LWArticle sky = new LWArticle(skyBox);
	                    sky.scale = new Vector3f(30,12,30);
	                    sky.static_object = true;
	                    articles.add(sky);*/
	                    
	                	
	                	
	                	VBOArticle stepModel = (VBOArticle) Article.loadArticle("res\\assets\\Box.dmf");
	                	//stepModel.scale(new Vector3f(0.5f,0.15f,2f));
	                    articles.add(stepModel);
	                    /*for(float x=-58; x>-60; x--) {
		                    for(float y=0; y<Math.abs(x)-10; y++) {
			                	LWArticle step = new LWArticle(stepModel);
			                	step.dynamic_object = true;
			                	step.pos = new Vector3f(x,y,50);
			                	articles.add(step);
		                    }
	                    }*/

	                	LWArticle floor2 = new LWArticle(stepModel);
	                	floor2.dynamic_object = true;
	                	floor2.pos = new Vector3f(15.8f,12, 35f);
	                	floor2.scale(new Vector3f(20f,0.15f,20f));
	                	articles.add(floor2);
	                	

	                	VBOArticle bx = (VBOArticle) Article.loadArticle("res\\assets\\Sword.dmf");
	                	bx.scale(new Vector3f(0.5f,0.25f,0.5f));
                		articles.add(bx);
	                	for(byte x=0; x<40; x+=10) {
	                		for(byte z=20; z<50; z+=10) {
			                	LWArticle column = new LWArticle(bx);
			                	column.dynamic_object = true;
			                	column.pos = new Vector3f(x,-4f,z);
		                		articles.add(column);
	                		}
	                	}
	                	
	                    
	                    
	                    
	                	Article floor = Article.loadArticle("res\\assets\\whitetile.dmf");
	                	floor.scale(new Vector3f(4f, 1f, 4f));
	                    for(byte x=-60; x<60; x+=4) {
	                        for(byte y=-60; y<60; y+=4) {
	                        	Article a  = new Article(floor);
	                        	a.pos.x = x;
	                        	a.pos.y = -10f;
	                        	a.pos.z = y;
	                            articles.add(a);
	                        }
	                    }
	                    
	                	Article pole = Article.loadArticle("res\\assets\\rocket.dmf");
	                	pole.dynamic_object = true;
	                	pole.scale(new Vector3f(1.2f,4f, 1.2f));
	                    for(byte x=0; x<10; x++) {
	                    	Article a = new Article(pole);
	                    	a.pos.x = (float)(Math.random()*18)-12;
	                    	a.pos.y = (float)(Math.random()*30)-5;
	                    	a.pos.z = (float)(Math.random()*20)-10;
	                        articles.add(a);
	                    }
	                    Article lightPole = Article.loadArticle("res\\assets\\lightPole.dmf");
	                	lightPole.dynamic_object = true;
	                	lightPole.scale(new Vector3f(1f,1.5f, 1f));
	                	
	                    Article chairC = Article.loadArticle("res\\assets\\chair.dmf");
	                    chairC.dynamic_object = true;
	                    chairC.rotate("Z=90;");
	                    
	                    
	                	for(byte i=-10; i<10; i++) {
	                		Article a = new Article(lightPole);
	                		a.pos.x = i*5;
	                    	a.pos.y = 20;
	                    	a.pos.z = -15;
	                		articles.add(a);
	                		Article b = new Article(chairC);
	                		b.pos.x = i*5;
	                    	b.pos.y = 20;
	                    	b.pos.z = 18f;
	                		articles.add(b);
	                	}
	                	
	                	
		                
	                    Article chairL = Article.loadArticle("res\\assets\\chair.dmf");
	                    chairL.dynamic_object = true;
	                    chairL.rotate("Y=90;");
	                    chairL.scale(new Vector3f(1.3f,1,1));
	                    Article chairR = Article.loadArticle("res\\assets\\chair.dmf");
	                    chairR.dynamic_object = true;
	                    chairR.rotate("Y=-90;");
	                    chairR.scale(new Vector3f(1.3f,1,1));
	                    Article table = Article.loadArticle("res\\assets\\desk.dmf");
	                    table.dynamic_object = true;
	                    table.scale(new Vector3f(1f, 0.9f, 1f));
	                    for(byte x=0; x<5; x++) {
	                    	Article left  = new Article(chairL);
	                    	left.pos.x = 13;
	                    	left.pos.y = 20;
	                    	left.pos.z = x;
	                        left.velocity = new Vector3f(0.01f, 0,0);
	                        articles.add(left);
	                        
	                    	Article right  = new Article(chairR);
	                    	right.pos.x = 19;
	                    	right.pos.y = 20;
	                    	right.pos.z = x;
	                        right.velocity = new Vector3f(-0.01f, 0,0);
	                        articles.add(right);
	                    }
	                    for(byte x=0; x<3; x++) {
	                    	Article center  = new Article(table);
	                    	center.pos.x = 16;
	                    	center.pos.y = 0;
	                    	center.pos.z = x*2;
	                        articles.add(center);
	                    }
	
	                	Article center  = new Article(table);
	                	center.pos.x = 16;
	                	center.pos.y = 2;
	                	center.pos.z = -1.9f;
	                    articles.add(center);
	                    
	                    
	                    Article box = Article.loadArticle("res\\assets\\crate.dmf");
	                	box.dynamic_object = false;
	                	box.scale(new Vector3f(1f,0.2f,0.5f));
	                	Article box2 = Article.loadArticle("res\\assets\\crate.dmf");
	                	Article step = Article.loadArticle("res\\assets\\crate.dmf");
	                	step.pos = new Vector3f(-10f,-7.5f, 16.3f);
	                	step.dynamic_object = false;
	                	articles.add(step);
	                	box.dynamic_object = false;
	                	box2.scale(new Vector3f(0.5f,0.2f,1f));
	                    for(byte i=0; i<10; i++) {
	                    	Article a = new Article(box);
	                    	a.pos.x = -10f;
	                    	a.pos.y = -10.1f+(i*0.32f);
	                    	a.pos.z = 11f+(i*0.5f);
	                        articles.add(a);
	                    	Article b = new Article(box2);
	                    	b.pos.x = -9f + (i*0.5f);
	                    	b.pos.y = -7f+(i*0.32f);
	                    	b.pos.z = 16.3f;
	                        articles.add(b);
	                    }
	                    
	                    //GenerateBoundBoxArticles();
	                    mousePos = new Vector2f();
	                    
	                    glMatrixMode(GL_PROJECTION);
	                    glLoadIdentity();
	                    final float aspect = FRAME_DIM.getWidth()/FRAME_DIM.getHeight();
	                    final float fH = (float)(Math.tan(0.2*Math.PI)*0.01f);
	                    final float fW = fH*aspect;
	                    glFrustum(-fW, fW, -fH, fH, 0.01f, 100);
	                    glViewport(0, 0, FRAME_DIM.getWidth(), FRAME_DIM.getHeight());
	                    glMatrixMode(GL_MODELVIEW);
	                    glEnable(GL_DEPTH_TEST);
	                    glEnable(GL_CULL_FACE);
	                    glEnable(GL_TEXTURE_2D);
	                    glEnable(GL_BLEND);
	                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	                    mainLoop();
	                    for(Article a : articles) {
	                    	if(a.texture != null) {
	                    		a.texture.delete();
	                    	}
	                    }
	                    glfwDestroyWindow(window);
	        		}
	        	}
	        } finally {
	            glfwTerminate();
	        }
    	}
    }
    
    public static void NewGame(InputSource src) {
    	if (glfwInit() == GLFW_TRUE) {
    		glfwDefaultWindowHints();
    		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    		window = glfwCreateWindow(FRAME_DIM.getWidth(), FRAME_DIM.getHeight(), "Hola Mundo!", NULL, NULL);
    		if(window == NULL) {
    			System.out.println("Broke Window");
    		} else {
    			KeyHandler keyHandler = new KeyHandler();
    			glfwSetKeyCallback(window, keyHandler);
    			MouseMoveHandler mouseMoveHandler = new MouseMoveHandler();
    			glfwSetCursorPosCallback(window, mouseMoveHandler);
    			MouseButtonHandler mouseButtonHandler = new MouseButtonHandler();
    			glfwSetMouseButtonCallback(window, mouseButtonHandler);
                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                glfwSetWindowPos(window, (vidmode.width()-FRAME_DIM.getWidth())/2, (vidmode.height()-FRAME_DIM.getHeight())/2);
                glfwMakeContextCurrent(window);
                glfwSwapInterval(1);
                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                glfwShowWindow(window);
                GL.createCapabilities();
                
                articles = new ArrayList<Article>();
            	Article player = Article.loadArticle("res\\assets\\player.dmf");
            	player.dynamic_object = true;
                articles.add(new Article(player));
                
                articles = XMLGen.BuildXML(player, src);
                
                mousePos = new Vector2f();
                
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                final float aspect = FRAME_DIM.getWidth()/FRAME_DIM.getHeight();
                final float fH = (float)(Math.tan(0.25*Math.PI)*0.01f);
                final float fW = fH*aspect;
                glFrustum(-fW, fW, -fH, fH, 0.01f, 100);
                glViewport(0, 0, FRAME_DIM.getWidth(), FRAME_DIM.getHeight());
                glMatrixMode(GL_MODELVIEW);
                glEnable(GL_DEPTH_TEST);
                glEnable(GL_CULL_FACE);
                glEnable(GL_TEXTURE_2D);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                mainLoop();
                for(Article a : articles) {
                	if(a.texture!=null) a.texture.delete();
                }
                glfwDestroyWindow(window);
    		}
    	}
    }
    
    
    public static Article getCamera() {
    	return articles.get(cameraId);
    }
    
    
    public static void GenerateBoundBoxArticles() {
    	ArrayList<Article> boxes = new ArrayList<Article>();
    	for(Article art : articles) {
    		if(!(art instanceof VBOArticle)) {
	    		for(Box box : art.boxes) {
	    			boxes.add(box.buildArticle(art.pos));
	    		}
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
			Roget.keys[0] = flag;
			break;
		case GLFW_KEY_A:
			Roget.keys[1] = flag;
			break;
		case GLFW_KEY_S:
			Roget.keys[2] = flag;
			break;
		case GLFW_KEY_D:
			Roget.keys[3] = flag;
			break;
		case GLFW_KEY_SPACE:
			Roget.keys[4] = flag;
			break;
		case GLFW_KEY_LEFT_ALT:
			Roget.sprint = true;
			break;
		case GLFW_KEY_LEFT_SHIFT:
			keys[6] = flag;
			//cameraId++;
			break;
		case GLFW_KEY_RIGHT_SHIFT:
			cameraId--;
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
			Roget.keys[0] = flag;
			break;
		case GLFW_KEY_A:
			Roget.keys[1] = flag;
			break;
		case GLFW_KEY_S:
			Roget.keys[2] = flag;
			break;
		case GLFW_KEY_D:
			Roget.keys[3] = flag;
			break;
		case GLFW_KEY_SPACE:
			Roget.keys[4] = flag;
			break;
		case GLFW_KEY_LEFT_ALT:
			Roget.sprint = false;
			break;
		case GLFW_KEY_LEFT_SHIFT:
			keys[6] = flag;
			break;
		case GLFW_KEY_ESCAPE:
			glfwSetWindowShouldClose(window, GLFW_TRUE);
			break;
		}
    }

    public static void mousePress(int button) {
    	pressed=true;
    }
    public static void mouseRelease(int button) {
    	if(button == GLFW_MOUSE_BUTTON_3) {
    		firstPerson=!firstPerson;
    	}
    	pressed=false;
    }
    static boolean pressed = false;
    public static void mouseMoved(Vector2f pos) {
    	if(!pressed) {
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
    			Roget.keyPress(key, scancode);
    			break;
    		case GLFW_RELEASE:
    			Roget.keyRelease(key, scancode);
    			break;
    		case GLFW_REPEAT:
    			Roget.keyPress(key, scancode);
    			break;
    		}
    	}
    }
    static class MouseMoveHandler extends GLFWCursorPosCallback {
		@Override
		public void invoke(long window, double xpos, double ypos) {
			Roget.mouseMoved(new Vector2f((float)xpos, (float)ypos));
		}
    }
    static class MouseButtonHandler extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			switch(action) {
			case GLFW_PRESS:
    			Roget.mousePress(button);
    			break;
    		case GLFW_RELEASE:
    			Roget.mouseRelease(button);
    			break;
    		}
		}
    }
}

