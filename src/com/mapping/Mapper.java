package com.mapping;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
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
import javax.swing.text.StyledEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.modeling.Modeler;
import com.preAlpha.Article;
import com.preAlpha.Roget;
import com.preAlpha.Texture;
import com.preAlpha.XMLGen;

public class Mapper extends JPanel implements KeyListener, ActionListener, PropertyChangeListener, MouseListener {
	
	
	
	
	
	private static final long serialVersionUID = -7749147052658260398L;
	static boolean keys[] = { false, false, false, false };
	static String mapPath = "";
	static JMenuBar menuBar;
	static JMenu fileMenu;
	static JEditorPane editor = new JEditorPane();
	static JEditorPane scriptEditor = new JEditorPane();
	static JMenuItem[] fileItems = {
		new JMenuItem("Open..."),
		new JMenuItem("Save"),
		new JMenuItem("Save As..."),
		new JMenuItem("Export"),
		new JMenuItem("Export As...")
	};
	static String[] paneNames = {
		"New *",
		"Script",
		"Models"
	};
	static JTabbedPane tabbedPane;
	static JComponent panes[] = {
		new JScrollPane(editor),
		new JScrollPane(scriptEditor),
		new JPanel()
	};
	static JButton[][] buttons = {
		{
			
		}
	};
	static JCheckBox[][] checkBoxes = {
		{
			
		}
	};
	final static Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
	
	
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setUndecorated(true);
		f.setSize(SCREEN.width/3, SCREEN.height-(SCREEN.height/8));
		
		
		Mapper THIS = new Mapper();
		f.addMouseListener(THIS);
		f.addKeyListener(THIS);
		
		
		menuBar = new JMenuBar();
		menuBar.add(fileMenu = new JMenu("File"));
		for(JMenuItem jmi : fileItems) {
			jmi.addActionListener(THIS);
			fileMenu.add(jmi);
		}
		f.add(menuBar, BorderLayout.NORTH);
		
		tabbedPane = new JTabbedPane();
		for(byte i=0; i<panes.length; i++) {
			tabbedPane.add(panes[i], paneNames[i]);
		}
		f.add(tabbedPane);

		editor.setEditorKit(new StyledEditorKit());
		editor.setText("<?xml version=\"1.0\" ?>\n\n<Map>\n</Map>");
		editor.addKeyListener(THIS);
		
		scriptEditor.setEditorKit(new StyledEditorKit());
		scriptEditor.addKeyListener(THIS);
		
		
		f.setVisible(true);
		
		RogetMapper.modelView(SCREEN.width/3, 0, 2*SCREEN.width/3, SCREEN.height-(SCREEN.height/8));
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton button = (JButton)(e.getSource());
			
		}else if(e.getSource() instanceof JTextField) {
			JTextField textField = (JTextField)e.getSource();
			
		} else if(e.getSource() instanceof JCheckBox) {
			JCheckBox box = (JCheckBox)e.getSource();
			
		} else if(e.getSource() instanceof JMenuItem) {
			JMenuItem option = (JMenuItem)e.getSource();
			switch(option.getText()) {
			case "Save As...":
				Mapper.saveFileAs();
				break;
			case "Save":
				if(Mapper.mapPath.isEmpty()) {
					Mapper.saveFileAs();
				}else {
					Mapper.saveFile(mapPath);
				}
				break;
				case "Open...":
					openFile();
				break;
			}
		}
	}
	
	
	public static void openFile() {
		JFileChooser fileChooser = new JFileChooser("res\\maps\\");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Super Map Dan", "smd");
	    fileChooser.setFileFilter(filter);
		fileChooser.showOpenDialog(null);
		if(fileChooser.getSelectedFile() != null) {
			String path = fileChooser.getSelectedFile().getPath();
			byte[] encoded = null;
			try {
				encoded = Files.readAllBytes(Paths.get(path));
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally {
				mapPath = path;
				editor.setText(new String(encoded, Charset.defaultCharset()));
			}
		}
	}
	
	public static void saveFileAs() {
		JFileChooser fileChooser = new JFileChooser("res\\maps\\");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Super Mad Dan", "smd");
	    fileChooser.setFileFilter(filter);
		fileChooser.showSaveDialog(null);
		if(fileChooser.getSelectedFile() != null) {
			saveFile(fileChooser.getSelectedFile().getPath());
		}
	}
	
	public static void saveFile(String path) {
		try {
			PrintWriter fileOut = new PrintWriter(new File(path));
			fileOut.print(Mapper.editor.getText());
			fileOut.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_CONTROL:
			keys[0] = true;
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_CONTROL:
			keys[0] = false;
			break;
		case KeyEvent.VK_F5:
			String xml = Mapper.editor.getText();
		    InputSource input = new InputSource(new StringReader(xml));
			RogetMapper.articles = XMLGen.BuildXML(RogetMapper.getCamera(), input);
			break;
		case KeyEvent.VK_F6:
			xml = Mapper.editor.getText();
		    input = new InputSource(new StringReader(xml));
			JavaScriptParse.test(input);
			break;
		case KeyEvent.VK_F11:
			xml = Mapper.editor.getText();
		    input = new InputSource(new StringReader(xml));
			Roget.NewGame(input);
			break;
		case KeyEvent.VK_S:
			if(keys[0]) {
				if(Mapper.mapPath.isEmpty()) {
					Mapper.saveFileAs();
				}else {
					Mapper.saveFile(mapPath);
				}
			}
			break;
		case KeyEvent.VK_O:
			if(keys[0]) {
				openFile();
			}
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
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
		
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	
	
}





