package com.preAlpha;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mapping.Parsing;

public class XMLGen {
	
	
	
	public static ArrayList<Article> BuildXML(InputSource source) {
		ArrayList<Article> articles = new ArrayList<Article>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(source);
			document.getDocumentElement().normalize();
			Element element = null;
			element = document.getDocumentElement();
			switch(element.getNodeName()) {
			case "Map":
				articles = XMLGen.handleMapElement(element);
				break;
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return articles;
	}
	
	public static ArrayList<Article> BuildXML(Article camera, InputSource source) {
		ArrayList<Article> articles = new ArrayList<Article>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(source);
			document.getDocumentElement().normalize();
			Element element = null;
			element = document.getDocumentElement();
			switch(element.getNodeName()) {
			case "Map":
				articles.add(camera);
				articles.addAll(XMLGen.handleMapElement(element));
				break;
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return articles;
	}
	
	
	
	
	
	public static ArrayList<Article> handleMapElement(Node el) {
		if(el instanceof Element) {
			Element element = (Element)el;
			ArrayList<Article> articleList = new ArrayList<Article>();
			
			ArrayList<Article> children = new ArrayList<Article>();
			if(element.getNodeType() == Element.ELEMENT_NODE) {
				NodeList elements = element.getChildNodes();
				for(short i=0; i<elements.getLength(); i++) {
					ArrayList<Article> child = handleMapElement(elements.item(i));
					if(child != null) {
						children.addAll(child);
					}
				}
			}
			
			
			switch(element.getNodeName().toUpperCase()) {
			case "MAP":
				for(short k=0; k<children.size(); k++) {
					Vector3f offset = parseOffset(element);
					children.get(k).pos.x += offset.x;
					children.get(k).pos.y += offset.y;
					children.get(k).pos.z += offset.z;
					articleList.add(children.get(k));
				}
				break;
			case "STRIP":
				Short repeat = 1;
				try {
					repeat = Short.parseShort(element.getAttribute("repeat"));
				} catch(Exception e) {	repeat = 1;	}

				float sizeX = 0f;
				try {
					String expression = element.getAttribute("xstep");
					if(!expression.isEmpty()) {
						sizeX = (float) Parsing.eval(expression);
					}
				}catch(Exception e) {	sizeX = 0f;	}
				
				float sizeY = 0f;
				try {
					String expression = element.getAttribute("ystep");
					if(!expression.isEmpty()) {
						sizeY = (float) Parsing.eval(expression);
					}
				}catch(Exception e) {	sizeY = 0f;	}
				
				float sizeZ = 0f;
				try {
					String expression = element.getAttribute("zstep");
					if(!expression.isEmpty()) {
						sizeZ = (float) Parsing.eval(expression);
					}
				}catch(Exception e) {	sizeZ = 0f;	}
				
				for(short i=0; i<repeat; i++) {
					for(short k=0; k<children.size(); k++) {
						Vector3f offset = parseOffset(element);
						Article child = new Article(children.get(k));
						child.pos.x += (i*sizeX) + offset.x;
						child.pos.y	+= (i*sizeY) + offset.y;
						child.pos.z	+= (i*sizeZ) + offset.z;
						articleList.add(child);
					}
				}
				break;
			case "LANDSCAPE":
				
				break;
			default:
				articleList.add(buildArticle(element));
				break;
			}
			
			return articleList;
		}
		return null;
	}
	
	
	
	public static Article buildArticle(Element element) {
		Article article = Article.loadArticlePiecewise("res\\assets\\" + element.getNodeName() + ".dmf", true);
		NamedNodeMap attributes = element.getAttributes();
		article.setAttributes(attributes);
		return article;
	}
	
	
	
	
	public static Vector3f parseOffset(Element element) {
		Vector3f offset = new Vector3f();
		try {
			String expression = element.getAttribute("x");
			if(!expression.isEmpty()) {
				offset.x = (float) Parsing.eval(expression);
			}
		}catch(NumberFormatException e) {
		}
		try {
			String expression = element.getAttribute("y");
			if(!expression.isEmpty()) {
				offset.y = (float) Parsing.eval(expression);
			}
		}catch(NumberFormatException e) {
		}
		try {
			String expression = element.getAttribute("z");
			if(!expression.isEmpty()) {
				offset.z = (float) Parsing.eval(expression);
			}
		}catch(NumberFormatException e) {
		}
		return offset;
	}
	
	public static Vector3f parseVelocity(Element element) {
		Vector3f offset = new Vector3f();
		try {
			String expression = element.getAttribute("vx");
			if(!expression.isEmpty()) {
				offset.x = (float) Parsing.eval(expression);
			}
		}catch(NumberFormatException e) {
		}
		try {
			String expression = element.getAttribute("vy");
			if(!expression.isEmpty()) {
				offset.y = (float) Parsing.eval(expression);
			}
		}catch(NumberFormatException e) {
		}
		try {
			String expression = element.getAttribute("vz");
			if(!expression.isEmpty()) {
				offset.z = (float) Parsing.eval(expression);
			}
		}catch(NumberFormatException e) {
		}
		return offset;
	}
}
