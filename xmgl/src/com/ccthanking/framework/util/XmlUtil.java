package com.ccthanking.framework.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * some userful xml process utility .
 */
public class XmlUtil {
	public XmlUtil() {
	}

	public static void main(String[] args) throws IOException, Exception {
		// XmlUtil xmlUtil1 = new XmlUtil();
		// String s ="###" ;
		// Document document =newXmlDoc() ;
		// Element ele =document.createElement("abc");
		// Text ele2 =document.createTextNode("cde");
		//
		// document.createComment("abc Comment");
		// document.appendChild(ele);
		// ele2.setNodeValue("abc") ;
		// String t = ele2.getNodeName() ;
		// String v =ele2.getNodeValue() ;
		// System.out.println(t) ;
		// System.out.println(v) ;
		// ele.appendChild(ele2) ;
		// writeXml("f:/tt.txt",document) ;
		byte[] b = null;
		String str = toBase64(b);
		System.out.println(str);

	}

	/**
	 * create a new Document object
	 * 
	 * @return
	 */
	public static Document newXmlDoc() {
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException parserconfigurationexception) {
		}
		return document;

	}

	/**
	 * write a Document object to netive file
	 * 
	 * @param fileName
	 * @param document
	 * @throws java.lang.Exception
	 */
	public static void writeXml(String fileName, Document document)
			throws Exception {
		Transformer transform = TransformerFactory.newInstance()
				.newTransformer();
		FileOutputStream outStream = new FileOutputStream(fileName);
		BufferedOutputStream out = new BufferedOutputStream(outStream);
		transform.transform(new DOMSource(document), new StreamResult(out));
		out.close();

	}

	/**
	 * BASE64 encode to byte[] or visa .
	 * 
	 * @param buf
	 * @return
	 */
	public static String toBase64(byte[] buf) {
		return (new BASE64Encoder()).encode(buf);
	}

	public static byte[] fromBase64(String str) throws IOException {
		byte[] byteBuffer = new BASE64Decoder().decodeBuffer(str);
		return byteBuffer;

	}

	/**
	 * process a xml file to a Document instance .
	 * 
	 * @param file
	 *            the xml file to process
	 * @return org.w3c.dom.Document
	 */
	public static Document parseFile(File file) {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			boolean b = builder.isValidating();
			document = builder.parse(file);
		} catch (Exception exception) {
			// exception.printStackTrace() ;
		}
		return document;

	}

}
