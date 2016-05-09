package com.modeling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.preAlpha.Article;

public class Modeler extends JPanel implements ActionListener, PropertyChangeListener, MouseListener {
	
	
	
	private static final long serialVersionUID = 8642987482553097509L;
	static JMenuBar menuBar;
	static JMenu fileMenu;
	static JMenuItem[] fileItems = {
		new JMenuItem("Open..."),
		new JMenuItem("Save"),
		new JMenuItem("Save As..."),
		new JMenuItem("Export"),
		new JMenuItem("Export As...")
	};
	static JTabbedPane tabbedPane;
	static JPanel facePane = new JPanel();
	static JComponent panes[] = {
		new JScrollPane(facePane),
		new JPanel(),
		new JPanel(),
		new JPanel()
	};
	static JButton[][] buttons = {
		{
			new JButton("Copy Plane"),
			new JButton("+ Triangle"),
			new JButton("+ Quad"),
			new JButton("-X"),
			new JButton("-Y"),
			new JButton("-Z"),
			new JButton("-TX"),
			new JButton("-TY")
		},
		{
			new JButton("Browse...")
		}
	};
	static JCheckBox[][] checkBoxes = {
		{
			new JCheckBox("Cull Faces"),
			new JCheckBox("Normals")
		}
	};
	final static Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
	static ArrayList<Plane> planes = new ArrayList<Plane>();
	static JPanel infoEdit = new JPanel();
	static JPanel planeList = new JPanel();
	static JPanel extraPlaneInfo = new JPanel();
	static short currentPlane = 0;
	static JPanel topFacePanel;
	static JLabel title = new JLabel("");
	static JTextField textureField = new JTextField(30);
	static JTextField articleName;
	static ArrayList<Article> normalArrows = new ArrayList<Article>();
	
	
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else if(e.getKeyCode()==KeyEvent.VK_R) {
					GenerateArticle();
				}
			}
			public void keyTyped(KeyEvent e) {
				
			}
		});
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setSize(SCREEN.width/2, SCREEN.height-(SCREEN.height/8));
		Modeler THIS = new Modeler();
		f.addMouseListener(THIS);
		
		menuBar = new JMenuBar();
		menuBar.add(fileMenu = new JMenu("File"));
		for(JMenuItem jmi : fileItems) {
			jmi.addActionListener(THIS);
			fileMenu.add(jmi);
		}
		f.add(menuBar, BorderLayout.NORTH);
		
		
		
		tabbedPane = new JTabbedPane();
		tabbedPane.add("Faces", panes[0]);
		tabbedPane.add("Texture", panes[1]);
		tabbedPane.add("Boxes", panes[3]);
		tabbedPane.add("Settings", panes[2]);
		f.add(tabbedPane, BorderLayout.CENTER);
		facePane.setLayout(new BorderLayout());
		topFacePanel = new JPanel();
		topFacePanel.add(title);
		for(JButton b : buttons[0]) {
			b.addActionListener(THIS);
			topFacePanel.add(b);
		}
		facePane.add(topFacePanel, BorderLayout.NORTH);
		planeList.setLayout(new BoxLayout(planeList, BoxLayout.Y_AXIS));
		facePane.add(planeList, BorderLayout.WEST);
		
		facePane.add(extraPlaneInfo, BorderLayout.EAST);
		
		panes[1].setLayout(new BorderLayout());
		buttons[1][0].addActionListener(THIS);
		JPanel pln5 = new JPanel();
		pln5.add(buttons[1][0]);
		pln5.add(textureField);
		panes[1].add(pln5, BorderLayout.NORTH);
		panes[1].add(THIS, BorderLayout.CENTER);
		
		panes[2].setLayout(new BoxLayout(panes[2], BoxLayout.Y_AXIS));
		for(JCheckBox b : checkBoxes[0]) {
			b.addActionListener(THIS);
			panes[2].add(b);
		}
		JPanel p10 = new JPanel();
		p10.add(new JLabel("Article Name"));
		articleName = new JTextField(20);
		p10.add(articleName);
		panes[2].add(p10);
		
		f.setVisible(true);
		
		
		RogetModel.modelView(SCREEN.width/2, 0, SCREEN.width/2, SCREEN.height-(SCREEN.height/8), null);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Modeler.GenerateArticle();
		if(e.getSource() instanceof JButton) {
			JButton button = (JButton)(e.getSource());
			JPanel p = new JPanel();
			switch(button.getText()) {
			case "+ Triangle":
				if(planes.size()>0) {
					currentPlane = (short) planes.size();
				}
				Plane p4 = new Plane((byte) 3);
				Modeler.planes.add(p4);
				p.setLayout(new GridLayout(3,5, 20, 40));
				for(byte i=0; i<p4.fields.length; i++) {
					p.add(p4.fields[i]);
				}
				Modeler.planeList.add(p4.button);
				Modeler.facePane.remove(infoEdit);
				Modeler.facePane.add(p, BorderLayout.CENTER);
				Modeler.infoEdit = p;
				Modeler.facePane.revalidate();
				title.setText(p4.button.getText());
				Modeler.topFacePanel.revalidate();
				break;
			case "+ Quad":
				if(planes.size()>0) {
					currentPlane = (short) planes.size();
				}
				p4 = new Plane((byte) 4);
				Modeler.planes.add(p4);
				p.setLayout(new GridLayout(4,5, 20, 40));
				for(byte i=0; i<p4.fields.length; i++) {
					p.add(p4.fields[i]);
				}
				Modeler.planeList.add(p4.button);
				Modeler.facePane.remove(infoEdit);
				Modeler.facePane.add(p, BorderLayout.CENTER);
				Modeler.infoEdit = p;
				Modeler.facePane.revalidate();
				title.setText(p4.button.getText());
				break;
			case "Copy Plane":
				if(planes.size()>0) {
					Plane pln = new Plane(planes.get(currentPlane));
					currentPlane = (short) planes.size();
					Modeler.planes.add(pln);
					p.setLayout(new GridLayout(pln.points.length,5, 20, 40));
					for(byte i=0; i<pln.fields.length; i++) {
						p.add(pln.fields[i]);
					}
					Modeler.planeList.add(pln.button);
					Modeler.facePane.remove(infoEdit);
					Modeler.facePane.add(p, BorderLayout.CENTER);
					Modeler.infoEdit = p;
					Modeler.facePane.revalidate();
					title.setText(pln.button.getText());
				}
				break;
			case "Browse...":
				JFileChooser fileChooser = new JFileChooser("res\\images\\");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "Portable Network Graphics", "png");
			    fileChooser.setFileFilter(filter);
				fileChooser.showSaveDialog(null);
				if(fileChooser.getSelectedFile() != null) {
					String path = fileChooser.getSelectedFile().getPath();
					Modeler.textureField.setText(path.substring(path.indexOf("res")));
					repaint();
				}
				break;
			}
			if(button.getText().charAt(0)=='Q' || button.getText().charAt(0)=='T') {
				String items[] = button.getText().split(":");
				if(items[0].equals("T") || items[0].equals("Q")) {
					Plane pln = Modeler.planes.get(Integer.parseInt(items[1]));
					planes.get(currentPlane).refresh();
					currentPlane = Short.parseShort(items[1]);
					p.setLayout(new GridLayout(pln.points.length,5,20,40));
					for(byte i=0; i<pln.fields.length; i++) {
						p.add(pln.fields[i]);
					}
					Modeler.facePane.remove(infoEdit);
					Modeler.facePane.add(p, BorderLayout.CENTER);
					Modeler.infoEdit = p;
					Modeler.facePane.revalidate();
					title.setText(button.getText());
				}
			}
		}
		else if(e.getSource() instanceof JTextField) {
			
		} else if(e.getSource() instanceof JCheckBox) {
			JCheckBox box = (JCheckBox)e.getSource();
			switch(box.getText()) {
			case "Cull Faces":
				RogetModel.articles.get(1).cullFaces = box.isSelected();
				break;
			case "Normals":
				if(box.isSelected()) {
					for(Plane face : planes) {
						Vector3f src = new Vector3f(((face.points[0].x + face.points[1].x + face.points[2].x + ((face.points.length>3)?face.points[3].x:0)) / face.points.length),
								((face.points[0].y + face.points[1].y + face.points[2].y + ((face.points.length>3)?face.points[3].y:0)) / face.points.length),
								((face.points[0].z + face.points[1].z + face.points[2].z + ((face.points.length>3)?face.points[3].z:0)) / face.points.length));
						Vector3f dest = new Vector3f();
						Vector3f.cross(Vector3f.sub(face.points[1], face.points[0], null), Vector3f.sub(face.points[2], face.points[0], null), dest);
						Article arrow = new Article(false);
						arrow.texture = RogetModel.arrow.texture;
						arrow.triangles = new Vector3f[][] {
								{
									new Vector3f(dest.x+0.05f, dest.y, dest.z),
									new Vector3f(dest.x-0.05f, dest.y, dest.z),
									new Vector3f(src.x+0.05f, src.y, src.z)
								},
								{
									new Vector3f(src.x-0.05f, src.y, src.z),
									new Vector3f(src.x+0.05f, src.y, src.z),
									new Vector3f(dest.x-0.05f, dest.y, dest.z)
								}
						};
						arrow.textureMaps = new Vector2f[][] {
								{
									new Vector2f(1,0),
									new Vector2f(0,0),
									new Vector2f(1,1)
								},
								{
									new Vector2f(0,1),
									new Vector2f(1,1),
									new Vector2f(0,0)
								}
						};
						arrow.cullFaces = false;
						arrow.boxes = null;
						RogetModel.articles.add(arrow);
						normalArrows.add(arrow);
					}
				}else {
					for(short i=0; i<normalArrows.size(); i++) {
						RogetModel.articles.remove(normalArrows.get(i));
					}
				}
				break;
			}
		} else if(e.getSource() instanceof JMenuItem) {
			JMenuItem option = (JMenuItem)e.getSource();
			switch(option.getText()) {
			case "Save As...":
				JFileChooser fileChooser = new JFileChooser("res\\assets\\");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "DanModelEditor Asset", "dtf");
			    fileChooser.setFileFilter(filter);
				fileChooser.showSaveDialog(null);
				File file = fileChooser.getSelectedFile();
				if(file != null) {
					Modeler.saveFile(file);
				}
				break;
			case "Save":
				System.out.println(option.getText());
				break;
			case "Open...":
				fileChooser = new JFileChooser("res\\assets\\");
				filter = new FileNameExtensionFilter(
				        "DanModelEditor Asset", "dtf");
			    fileChooser.setFileFilter(filter);
				fileChooser.showOpenDialog(null);
				file = fileChooser.getSelectedFile();
				if(file != null) {
					Modeler.openFile(file);
				}
				break;
			case "Export":
			case "Export As...":
				fileChooser = new JFileChooser("res\\assets\\");
				filter = new FileNameExtensionFilter(
				        "DanModelFormat Asset", "dmf");
			    fileChooser.setFileFilter(filter);
				fileChooser.showSaveDialog(null);
				file = fileChooser.getSelectedFile();
				if(file != null) {
					Modeler.exportFile(file);
				}
				break;
			}
		}
	}
	
	public static void GenerateArticle() {
		if(planes.size()>0)planes.get(currentPlane).refresh();
		if(RogetModel.articles != null) {
			Article article = RogetModel.articles.get(1);
			try {
				if(!textureField.getText().isEmpty())
					RogetModel.path = textureField.getText();
			} catch(Exception e) {
				System.out.println("Texture:" + textureField.getText() + " Not Found");
				e.printStackTrace();
			}
			short accumPlane = 0;
			for(short i=0; i<planes.size(); i++) {
				if(planes.get(i).points.length==3) {
					accumPlane++;
				}else if(planes.get(i).points.length==4) {
					accumPlane+=2;
				}
			}
			article.triangles = new Vector3f[accumPlane][3];
			article.textureMaps = new Vector2f[accumPlane][2];
			boolean quad = false;
			for(short i=accumPlane=0; i<article.triangles.length; i++) {
				article.triangles[i] = new Vector3f[] { planes.get(accumPlane).points[0],planes.get(accumPlane).points[1+(quad?1:0)],planes.get(accumPlane).points[2+(quad?1:0)] };
				article.textureMaps[i] = new Vector2f[] { planes.get(accumPlane).tex_coords[0], planes.get(accumPlane).tex_coords[1+(quad?1:0)], planes.get(accumPlane).tex_coords[2+(quad?1:0)] };
				if(planes.get(accumPlane).points.length==4) {
					if(quad) {
						accumPlane++;
						quad = false;
					} else {
						quad= true;
					}
				}else {
					accumPlane++;
				}
			}
		}
	}
	
	

	public static void exportFile(File f) {
		try {
			String fileString = "";
			
			fileString += "NAME=" + articleName.getText() + "\n";
			fileString += "TEXTURE=" + textureField.getText() + "\n";
			fileString += "CULL=" + checkBoxes[0][0].isSelected() + "\n";
			

			short accumPlane = 0;
			for(short i=0; i<planes.size(); i++) {
				if(planes.get(i).points.length==3) {
					accumPlane++;
				}else if(planes.get(i).points.length==4) {
					accumPlane+=2;
				}
			}
			
			fileString += "FACES=" + accumPlane + "\n";
			for(short i=0; i<planes.size(); i++) {
				fileString += planes.get(i).points[0].x + "|" + planes.get(i).points[0].y + "|" + planes.get(i).points[0].z + "\n";
				fileString += planes.get(i).points[1].x + "|" + planes.get(i).points[1].y + "|" + planes.get(i).points[1].z + "\n";
				fileString += planes.get(i).points[2].x + "|" + planes.get(i).points[2].y + "|" + planes.get(i).points[2].z + "\n";
				if(planes.get(i).points.length>3) {
					fileString += planes.get(i).points[0].x + "|" + planes.get(i).points[0].y + "|" + planes.get(i).points[0].z + "\n";
					fileString += planes.get(i).points[2].x + "|" + planes.get(i).points[2].y + "|" + planes.get(i).points[2].z + "\n";
					fileString += planes.get(i).points[3].x + "|" + planes.get(i).points[3].y + "|" + planes.get(i).points[3].z + "\n";
				}
			}
			fileString += "TEXTURE_MAPS=" + accumPlane + "\n";
			for(short i=0; i<planes.size(); i++) {
				fileString += planes.get(i).tex_coords[0].x + "/" + planes.get(i).tex_coords[0].y + "\n";
				fileString += planes.get(i).tex_coords[1].x + "/" + planes.get(i).tex_coords[1].y + "\n";
				fileString += planes.get(i).tex_coords[2].x + "/" + planes.get(i).tex_coords[2].y + "\n";
				if(planes.get(i).points.length>3) {
					fileString += planes.get(i).tex_coords[0].x + "/" + planes.get(i).tex_coords[0].y + "\n";
					fileString += planes.get(i).tex_coords[2].x + "/" + planes.get(i).tex_coords[2].y + "\n";
					fileString += planes.get(i).tex_coords[3].x + "/" + planes.get(i).tex_coords[3].y + "\n";
				}
			}
			fileString += "BOXES=0\n";
			PrintWriter fileOut = new PrintWriter(f);
			fileOut.print(fileString);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveFile(File f) {
		try {
			String fileString = "";
			
			fileString += "NAME=" + articleName.getText() + "\n";
			fileString += "TEXTURE=" + textureField.getText() + "\n";
			fileString += "CULL=" + checkBoxes[0][0].isSelected() + "\n";
			fileString += "FACES=" + planes.size();
			for(short i=0; i<planes.size(); i++) {
				fileString += "|" + planes.get(i).points.length;
			}
			fileString += "\n";
			for(short i=0; i<planes.size(); i++) {
				fileString += planes.get(i).points[0].x + "|" + planes.get(i).points[0].y + "|" + planes.get(i).points[0].z + "\n";
				fileString += planes.get(i).points[1].x + "|" + planes.get(i).points[1].y + "|" + planes.get(i).points[1].z + "\n";
				fileString += planes.get(i).points[2].x + "|" + planes.get(i).points[2].y + "|" + planes.get(i).points[2].z + "\n";
				if(planes.get(i).points.length>3) {
					fileString += planes.get(i).points[3].x + "|" + planes.get(i).points[3].y + "|" + planes.get(i).points[3].z + "\n";
				}
			}
			fileString += "TEXTURE_MAPS=" + planes.size() + "\n";
			for(short i=0; i<planes.size(); i++) {
				fileString += planes.get(i).tex_coords[0].x + "/" + planes.get(i).tex_coords[0].y + "\n";
				fileString += planes.get(i).tex_coords[1].x + "/" + planes.get(i).tex_coords[1].y + "\n";
				fileString += planes.get(i).tex_coords[2].x + "/" + planes.get(i).tex_coords[2].y + "\n";
				if(planes.get(i).points.length>3) {
					fileString += planes.get(i).tex_coords[3].x + "/" + planes.get(i).tex_coords[3].y + "\n";
				}
			}
			fileString += "BOXES=0\n";
			PrintWriter fileOut = new PrintWriter(f);
			fileOut.print(fileString);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void openFile(File f) {
		Scanner s;
		String mode = "";
		try {
			s = new Scanner(f);
			String line = "";
			Plane currPlane = null;
			short planeIndex = 0;
			byte pointCount = 0;
			while(s.hasNext()) {
				line = s.nextLine();
				if(line.indexOf("=")>=0) {
					String[] tags = line.split("=");
					mode = tags[0].toUpperCase();
					switch(tags[0].toUpperCase()) {
					case "NAME":
						if(tags.length>1)
							articleName.setText(tags[1]);
						break;
					case "FACES":
						planes = new ArrayList<Plane>();
						String[] planeSizes = line.split("\\|");
						for(short i=1; i<planeSizes.length; i++) {
							planes.add(new Plane(Byte.parseByte(planeSizes[i])));
						}
						currPlane = planes.get(0);
						break;
					case "CULL":
						RogetModel.articles.get(1).cullFaces = Boolean.parseBoolean(tags[1]);
						checkBoxes[0][0].setSelected(RogetModel.articles.get(1).cullFaces);
						break;
					case "TEXTURE_MAPS":
						currPlane = planes.get(0);
						planeIndex = 0;
						pointCount = 0;
						break;
					case "TEXTURE":
						textureField.setText(tags[1]);
						break;
					}
				}
				else {
					switch(mode) {
					case "FACES":
						String tags[] = line.split("\\|");
						Vector3f v3 = new Vector3f(Float.parseFloat(tags[0]),Float.parseFloat(tags[1]),Float.parseFloat(tags[2]));
						currPlane.points[pointCount] = v3;
						pointCount++;
						if(currPlane.points.length==pointCount) {
							pointCount = 0;
							if(planeIndex+1 != planes.size()) {
								currPlane = planes.get(++planeIndex);
							}
						}
						break;
					case "TEXTURE_MAPS":
						tags = line.split("/");
						Vector2f v2 = new Vector2f(Float.parseFloat(tags[0]),Float.parseFloat(tags[1]));
						currPlane.tex_coords[pointCount] = v2;
						pointCount++;
						if(currPlane.points.length==pointCount) {
							pointCount = 0;
							if(planeIndex+1 != planes.size()) {
								currPlane = planes.get(++planeIndex);
							}
						}
						break;
					}
				}
			}
			for(Plane p : planes) {
				for(byte i=0; i<p.points.length; i++) {
					p.fields[(i*5)].setText("" + p.points[i].x);
					p.fields[(i*5)+1].setText("" + p.points[i].y);
					p.fields[(i*5)+2].setText("" + p.points[i].z);
					p.fields[(i*5)+3].setText("" + p.tex_coords[i].x);
					p.fields[(i*5)+4].setText("" + p.tex_coords[i].y);
				}
				JPanel pln = new JPanel();
				pln.setLayout(new GridLayout(p.points.length, 5, 20, 40));
				for(byte i=0; i<p.fields.length; i++) {
					pln.add(p.fields[i]);
				}
				Modeler.planeList.add(p.button);
				Modeler.facePane.remove(infoEdit);
				Modeler.facePane.add(pln, BorderLayout.CENTER);
				Modeler.infoEdit = pln;
				Modeler.facePane.revalidate();
				title.setText(p.button.getText());
				p.refresh();
			}
			
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		GenerateArticle();
	}
	
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,1000,1000);
		g.drawImage(new ImageIcon(textureField.getText()).getImage(), 20, 20, null);
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		Modeler.GenerateArticle();
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
}


class Plane {
	
	public JButton button;
	public JTextField[] fields;
	public Vector3f points[];
	public Vector2f tex_coords[];
	
	public Plane(byte n) {
		button = new JButton((n==3?'T':'Q') + ":" + Modeler.planes.size());
		fields = new JTextField[(n*3)+(n*2)];
		points = new Vector3f[n];
		tex_coords = new Vector2f[n];
		Modeler m = new Modeler();
		for(byte i=0; i<fields.length; i++) {
			fields[i] = new JTextField("0.0", 5);
			fields[i].addActionListener(m);
			fields[i].setBorder(BorderFactory.createCompoundBorder(
			        fields[i].getBorder(), 
			        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		}
		button.addActionListener(m);
	}
	
	public Plane(Plane p) {
		button = new JButton((p.points.length==3?'T':'Q') + ":" + Modeler.planes.size());
		fields = new JTextField[(p.points.length*3)+(p.points.length*2)];
		points = new Vector3f[p.points.length];
		tex_coords = new Vector2f[p.points.length];
		Modeler m = new Modeler();
		for(byte i=0; i<fields.length; i++) {
			fields[i] = new JTextField(p.fields[i].getText(), 5);
			fields[i].addActionListener(m);
			fields[i].setBorder(BorderFactory.createCompoundBorder(
			        fields[i].getBorder(), 
			        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		}
		button.addActionListener(m);
	}
	
	public void refresh() {
		points = new Vector3f[fields.length/5];
		tex_coords = new Vector2f[fields.length/5];
		for(byte i=0; i<points.length; i++) {
			points[i] = new Vector3f(Float.parseFloat(((JTextField)Modeler.infoEdit.getComponent((i*5)+0)).getText()),
					Float.parseFloat(((JTextField)Modeler.infoEdit.getComponent((i*5)+1)).getText()),
					Float.parseFloat(((JTextField)Modeler.infoEdit.getComponent((i*5)+2)).getText()));
			tex_coords[i] = new Vector2f(Float.parseFloat(((JTextField)Modeler.infoEdit.getComponent((i*5)+3)).getText()),
					Float.parseFloat(((JTextField)Modeler.infoEdit.getComponent((i*5)+4)).getText()));
		}
	}
	
}


