package com.mapping;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.preAlpha.Article;

public class JavaScriptParse {
	
	
	
	public static void test(InputSource src) {
		
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document document = null;
		Element element = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			document = dBuilder.parse(src);
			document.getDocumentElement().normalize();
			element = document.getDocumentElement();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		
		
		
		
		
		SimpleBindings bindings = new SimpleBindings();
		bindings.put("element", element);
		bindings.put("document", document);
		engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
		
		Map.articles = RogetMapper.articles;
		
		String js = "var map = Java.type('com.mapping.Map');\n";
		
		
		
		
		js += Mapper.scriptEditor.getText();
		
		
		
		try {
			engine.eval(js);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
