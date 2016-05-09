package com.modeling;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.preAlpha.Article;
import com.preAlpha.Box;
import com.preAlpha.Light;
import com.preAlpha.Texture;
import com.preAlpha.VBOArticle;
import com.preAlpha.LightWeightArticle;

public class BlenderExport {
	
	
	
	
	private static ModelView mv;
	private static JFrame frame;
	private static JTextField filePath;
	private static JTextField nameField;
	private static JTabbedPane tabbedPane;
	private static JPanel dataPanels;
	private static JButton export;
	private static double granularity = 0.1f;
	private static JCheckBox cullFaces;
	private static String relativeFilePath;
	
	public static void main(String[] args) {
		mv = new ModelView();
		frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,900);
		frame.setLayout(new BorderLayout());
		
		Container filePanel = new Container();
		filePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton browse = new JButton("Browse");
		browse.addActionListener((e) -> openFile());
		filePanel.add(browse);
		filePath = new JTextField(12);
		filePath.setEditable(false);
		filePanel.add(filePath);
		frame.add(filePanel, BorderLayout.NORTH);
		
		
		JPanel cTemp = new JPanel();
		frame.add(cTemp);
		dataPanels = new JPanel();
		
		dataPanels.setLayout(new BorderLayout());
		
		nameField = new JTextField("Name", 12);
		nameField.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				if(nameField.getText().equals("Name")) {
					nameField.setText("");
				}
			}
			public void mouseClicked(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
		});
		dataPanels.add(nameField, BorderLayout.NORTH);
		
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton addBox = new JButton("+ Box");
		addBox.addActionListener((e) -> newBox());
		toolbar.add(addBox);
		JButton addLight = new JButton("+ Light");
		addLight.addActionListener((e) -> newLight());
		toolbar.add(addLight);
		cullFaces = new JCheckBox();
		cullFaces.setText("Cull Faces");
		cullFaces.addActionListener((e) -> ModelView.getModel().cullFaces = cullFaces.isSelected());
		toolbar.add(cullFaces);
		JCheckBox transparent = new JCheckBox();
		transparent.setText("Transparent");
		transparent.addActionListener((e) -> ModelView.getModelLW().transparent = transparent.isSelected());
		toolbar.add(transparent);
		dataPanels.add(toolbar, BorderLayout.CENTER);
		
		
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener((e) -> ModelView.setActiveBox(Byte.parseByte(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).split(":")[1])));
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		dataPanels.add(tabbedPane, BorderLayout.SOUTH);
		
		cTemp.add(dataPanels);
		frame.add(cTemp, BorderLayout.CENTER);
		
		
		export = new JButton("Export");
		export.addActionListener((e) -> export());
		frame.add(export, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	

	public static void openFile() {
		JFileChooser fileChooser = new JFileChooser("res\\assets\\");
		fileChooser.showOpenDialog(null);
		File file = fileChooser.getSelectedFile();
		if(file != null) {
			filePath.setText(file.getAbsolutePath());
			relativeFilePath = "res\\assets\\" + file.getName();
			
			Thread t = new Thread(mv);
			mv.filePath = file.getAbsolutePath();
			t.start();
		}
	}

	public static void newBox() {
		mv.addBox();
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,3));
		for(byte i=0; i<6; i++)
			p.add(new JTextField());
		tabbedPane.add(p, "B:" + (ModelView.boxes.size()-1));
	}
	
	public static void newLight() {
		tabbedPane.add(new JPanel(), "L");
		ModelView.lights.add(new LightSource(new Vector3f(),1f,new Vector3f(1,1,1)));
	}
	
	public static String export() {
		String model = "";
		model += "OBJ=" + relativeFilePath + "\n";
		model += "NAME=" + nameField.getText() + "\n";
		model += "CULL=" + cullFaces.isSelected() + "\n";
		model += "BOXES=" + ModelView.boxes.size() + "\n";
		String temp = "%s|%s|%s\n%s|%s|%s\n";
		for(byte i=0; i<ModelView.boxes.size(); i++) {
			BoundingBox b = ModelView.boxes.get(i);
			model += String.format(temp, b.pos.x, b.pos.y, b.pos.z, b.dim.x, b.dim.y, b.dim.z);
		}
		System.out.println(model);
		return model;
	}
	
	
	
	
	static class ModelView implements Runnable {
		
		private static long window;
	    private static final Dimension FRAME_DIM = new Dimension(800,800);
	    private static ArrayList<Article> articles;
	    private static ArrayList<BoundingBox> boxes;
	    private static ArrayList<LightSource> lights;
	    private static int cameraId = 0;
	    private static Vector2f mousePos;
	    private static boolean[] keys = {false,false,false,false,false,false};
	    public String filePath;
	    private static VBOArticle model;
	    private static boolean negativeSign = false;
	    private static boolean dimensionChoice = false;
	    private static LightWeightArticle modelLW;
	    private static byte boxId = -1;
	    
	    private static Graphics g3;
	    private static BufferedImage buffer;
	    
	    
	    public ModelView() {
	    }
	    
	    private static void mainLoop() {
	    	long timeStart = System.currentTimeMillis();
	    	long timeCurr = 0;
	    	final int TIME_OFFSET = 20;
	        while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
	            if((timeStart+TIME_OFFSET)<timeCurr) {
	                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	                glPushMatrix();
	                glRotatef(articles.get(cameraId).quat.x,1,0,0);
	                glRotatef(articles.get(cameraId).quat.y,0,1,0);
	                glTranslatef(-articles.get(cameraId).pos.x,-articles.get(cameraId).pos.y,-articles.get(cameraId).pos.z);
	                	Iterator<BoundingBox> iter = boxes.iterator();
		                while(iter.hasNext()) {
	    	            	iter.next().render();
	    	            }
		                for(Article a : articles) {
	    	            	a.render();
	    	            }
		        		//glPopMatrix();
		        		//glPushMatrix();
	        		glPopMatrix();
					paintUI();
	                glfwSwapBuffers(window);
	                glfwPollEvents();
	            	tick();
	            }
	        	timeCurr = System.currentTimeMillis();
	        }
	    }

	    public static VBOArticle getModel() {
	    	return model;
	    }
	    public static LightWeightArticle getModelLW() {
	    	return modelLW;
	    }
	    
	    public static void tick() {
	    	Article camera = getCamera();
	    	float speed = (float) (granularity);
	    	if(keys[0]) {
				camera.pos.z-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*speed;
				camera.pos.x+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*speed;
	    	}
	    	if(keys[1]) {
	    		camera.pos.x-=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*speed;
	    		camera.pos.z-=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*speed;
	    	}
	    	if(keys[2]) {
	    		camera.pos.z+=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*speed;
	    		camera.pos.x-=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*speed;
	    	}
	    	if(keys[3]) {
	    		camera.pos.x+=Math.cos(Math.toRadians(articles.get(cameraId).quat.y))*speed;
	    		camera.pos.z+=Math.sin(Math.toRadians(articles.get(cameraId).quat.y))*speed;
	    	}
	    	if(keys[4]) {
	    		camera.pos.y+=speed;
	    	}
	    	if(keys[5]) {
	    		camera.pos.y-=speed;
	    	}
	    }
	    
	    public void addBox() {
            boxes.add(new BoundingBox(new Vector3f(-0.5f,-0.5f,-0.5f),new Vector3f(1,1,1)));
            ModelView.setActiveBox((byte) (boxes.size()-1));
	    }
	    
	    
	    public void run() {
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
	        			ScrollHandler scrollHandler = new ScrollHandler();
	        			glfwSetScrollCallback(window, scrollHandler);
	                    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	                    glfwSetWindowPos(window, (vidmode.width()-FRAME_DIM.getWidth())/2, (vidmode.height()-FRAME_DIM.getHeight())/2);
	                    glfwMakeContextCurrent(window);
	                    glfwSwapInterval(1);
	                    glfwShowWindow(window);
	                    GL.createCapabilities();
	                    
	                    articles = new ArrayList<Article>();
	                	Article player = Article.loadArticle("res\\assets\\player.dmf");
	                	player.dynamic_object = true;
	                    articles.add(new Article(player));
	                    ModelView.model = new VBOArticle(filePath);
	                    articles.add(ModelView.model);
	                    modelLW = new LightWeightArticle(ModelView.model);
	                    
	                    
	                    
	                    boxes = new ArrayList<BoundingBox>();
	                    lights = new ArrayList<LightSource>();
	                    
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
	    
	    
	    
	    public static void paintUI() {
	    	
	    	buffer = new BufferedImage(FRAME_DIM.getWidth(), FRAME_DIM.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    	g3 = buffer.getGraphics();
            g3.setColor(Color.WHITE);
            g3.setFont(new Font("Helvetica", Font.BOLD, 10));
            g3.drawString("Wavefront Export", 150, 150);
            g3.drawString("Granularity: " + (new BigDecimal(granularity).round(new MathContext(4)).doubleValue()), 150, 170);
	    	Texture.loadTexture(buffer);
            
			glBegin(GL_QUADS); {
				glTexCoord2f(1,1); glVertex3f(0.05f, -0.05f, -0.05f);
				glTexCoord2f(1,0); glVertex3f(0.05f, 0.05f, -0.05f);
				glTexCoord2f(0,0); glVertex3f(-0.05f, 0.05f, -0.05f);
				glTexCoord2f(0,1); glVertex3f(-0.05f, -0.05f, -0.05f);
			}glEnd();
	    }
	    
	    
	    public static Article getCamera() {
	    	return articles.get(cameraId);
	    }
	    
	    public static void setActiveBox(byte id) {
	    	if(boxId>=0 && boxId<boxes.size()) boxes.get(boxId).selectedBox = false;
	    	boxId = (byte) (id - lights.size());
	    	if(boxId>=0 && boxId<boxes.size()) boxes.get(boxId).selectedBox = true;
	    }
	    
	    
	    
	    
	    
	    public static void keyPress(int key, int scancode) {
	    	final boolean flag = true;
	    	switch(key) {
			case GLFW_KEY_W:
				ModelView.keys[0] = flag;
				break;
			case GLFW_KEY_A:
				ModelView.keys[1] = flag;
				break;
			case GLFW_KEY_S:
				ModelView.keys[2] = flag;
				break;
			case GLFW_KEY_D:
				ModelView.keys[3] = flag;
				break;
			case GLFW_KEY_SPACE:
				ModelView.keys[4] = flag;
				break;
			case GLFW_KEY_LEFT_SHIFT:
				ModelView.keys[5] = flag;
				break;
			case GLFW_KEY_RIGHT_SHIFT:
				cameraId--;
				break;
			case GLFW_KEY_ESCAPE:
				glfwSetWindowShouldClose(window, GLFW_TRUE);
				break;
			case GLFW_KEY_X:
				if(dimensionChoice) {
					boxes.get(boxId).pos.x += granularity*(negativeSign?-1:1);
				}else {
					boxes.get(boxId).dim.x += granularity*(negativeSign?-1:1);
				}
				break;
			case GLFW_KEY_Y:
				if(dimensionChoice) {
					boxes.get(boxId).pos.y += granularity*(negativeSign?-1:1);
				}else {
					boxes.get(boxId).dim.y += granularity*(negativeSign?-1:1);
				}
				break;
			case GLFW_KEY_Z:
				if(dimensionChoice) {
					boxes.get(boxId).pos.z += granularity*(negativeSign?-1:1);
				}else {
					boxes.get(boxId).dim.z += granularity*(negativeSign?-1:1);
				}
				break;
			case GLFW_KEY_MINUS:
				negativeSign = !negativeSign;
				break;
			case GLFW_KEY_TAB:
				dimensionChoice = !dimensionChoice;
				break;
			case GLFW_KEY_R:
				getCamera().pos = new Vector3f();
				break;
			}
	    }
	    public static void keyRelease(int key, int scancode) {
	    	final boolean flag = false;
	    	switch(key) {
			case GLFW_KEY_W:
				ModelView.keys[0] = flag;
				break;
			case GLFW_KEY_A:
				ModelView.keys[1] = flag;
				break;
			case GLFW_KEY_S:
				ModelView.keys[2] = flag;
				break;
			case GLFW_KEY_D:
				ModelView.keys[3] = flag;
				break;
			case GLFW_KEY_SPACE:
				ModelView.keys[4] = flag;
				break;
			case GLFW_KEY_LEFT_SHIFT:
				ModelView.keys[5] = flag;
				break;
			case GLFW_KEY_ESCAPE:
				glfwSetWindowShouldClose(window, GLFW_TRUE);
				break;
			}
	    }
	    
	    
	    static boolean negativeSignTemp;
	    public static void mousePress(int button) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            if(button == GLFW_MOUSE_BUTTON_2) {
            	negativeSignTemp = negativeSign;
            	negativeSign = true;
            }
            if(button == GLFW_MOUSE_BUTTON_3) {
            	pressed=true;
            }
	    }
	    public static void mouseRelease(int button) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            if(button == GLFW_MOUSE_BUTTON_2) {
            	negativeSign = negativeSignTemp;
            }
            if(button == GLFW_MOUSE_BUTTON_3) {
            	pressed=false;
            }
	    }
	    static boolean pressed = false;
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
	    
	    

	    public static void scroll(double offset) {
	    	if(offset>0 && granularity<100) {
	    		granularity*=10;
	    	}
	    	else if(offset<0 && granularity>0.001f) {
	    		granularity/=10;
	    	}
	    }
	    
	    static class KeyHandler extends GLFWKeyCallback {
	    	@Override
	    	public void invoke(long window, int key, int scancode, int action, int mods) {
	    		switch(action) {
	    		case GLFW_PRESS:
	    			ModelView.keyPress(key, scancode);
	    			break;
	    		case GLFW_RELEASE:
	    			ModelView.keyRelease(key, scancode);
	    			break;
	    		case GLFW_REPEAT:
	    			ModelView.keyPress(key, scancode);
	    			break;
	    		}
	    	}
	    }
	    static class MouseMoveHandler extends GLFWCursorPosCallback {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				ModelView.mouseMoved(new Vector2f((float)xpos, (float)ypos));
			}
	    }
	    static class MouseButtonHandler extends GLFWMouseButtonCallback {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				switch(action) {
				case GLFW_PRESS:
	    			ModelView.mousePress(button);
	    			break;
	    		case GLFW_RELEASE:
	    			ModelView.mouseRelease(button);
	    			break;
	    		}
			}
	    }
	    
	    static class ScrollHandler extends GLFWScrollCallback {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				ModelView.scroll(yoffset);
			}
	    }
	}
}
class BoundingBox extends Box {
	
	boolean selectedBox;
	
	public BoundingBox(Vector3f top, Vector3f bottom) {
		super(top, bottom);
		selectedBox = true;
	}
	
	
	public void render() {
		
		
		
		glDisable(GL_CULL_FACE);
		
		Texture.loadTexture("res\\images\\boundingBox.png").bind();
		if(selectedBox) {
			glColor4f(1,1,1,1);
		}else {
			glColor4f(1,1,1,0.1f);
		}
		glBegin(GL_QUADS); {
			glTexCoord2f(0, 0);
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, (pos.y+0.1f), pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x, (pos.y+0.1f), pos.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, (pos.y-0.1f+dim.y), pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x, (pos.y-0.1f+dim.y), pos.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+0.1f, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+0.1f, pos.y, pos.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x-0.1f, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x-0.1f, pos.y, pos.z);
			
			
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, (pos.y), pos.z+0.1f);
			glTexCoord2f(0, 0); glVertex3f(pos.x, (pos.y), pos.z+0.1f);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+0.1f, (pos.y), pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+0.1f, (pos.y), pos.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x-0.1f, (pos.y), pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x-0.1f, (pos.y), pos.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, (pos.y), pos.z+dim.z-0.1f);
			glTexCoord2f(0, 0); glVertex3f(pos.x, (pos.y), pos.z+dim.z-0.1f);
			
			
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, (pos.y+dim.y), pos.z+0.1f);
			glTexCoord2f(0, 0); glVertex3f(pos.x, (pos.y+dim.y), pos.z+0.1f);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+0.1f, (pos.y+dim.y), pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+0.1f, (pos.y+dim.y), pos.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y+dim.y, pos.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x-0.1f, (pos.y+dim.y), pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x-0.1f, (pos.y+dim.y), pos.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, (pos.y+dim.y), pos.z+dim.z-0.1f);
			glTexCoord2f(0, 0); glVertex3f(pos.x, (pos.y+dim.y), pos.z+dim.z-0.1f);
			
			
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, (pos.y+0.1f), pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x, (pos.y+0.1f), pos.z+dim.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+0.1f, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+0.1f, pos.y, pos.z+dim.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x-0.1f, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x-0.1f, pos.y, pos.z+dim.z);
			
			glTexCoord2f(0, 0); glVertex3f(pos.x, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, pos.y+dim.y, pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x+dim.x, (pos.y-0.1f+dim.y), pos.z+dim.z);
			glTexCoord2f(0, 0); glVertex3f(pos.x, (pos.y-0.1f+dim.y), pos.z+dim.z);
		}glEnd();
		glColor4f(1,1,1,1);
	}
	
}
class LightSource extends Light {
	
	boolean selectedBox;
	
	public LightSource(Vector3f pos, float intesity, Vector3f color) {
		super();
	}
	
	
	public void render() {
		glDisable(GL_CULL_FACE);
		
	}
	
}