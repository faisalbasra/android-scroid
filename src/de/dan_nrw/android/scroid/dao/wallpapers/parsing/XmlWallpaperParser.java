package de.dan_nrw.android.scroid.dao.wallpapers.parsing;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.dan_nrw.android.scroid.Wallpaper;


/**
 * @author Daniel Czerwonk
 *
 */
final class XmlWallpaperParser implements IWallpaperParser {

	/* (non-Javadoc)
	 * @see de.dan_nrw.boobleftboobright.IWallpaperParser#parse(java.lang.String)
	 */
	@Override
	public List<Wallpaper> parse(String data) throws ParseException {
        try {
        	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        	
        	Document document = builder.parse(new InputSource(new StringReader(data)));
        	
        	Element element = document.getDocumentElement();
    		
    		NodeList list = element.getElementsByTagName("wallpaper");
    		
    		List<Wallpaper> wallpapers = new ArrayList<Wallpaper>();
    		
    		for (int i = 0; i < list.getLength(); i++) {
    			Wallpaper wallpaper = this.getWallpaperByElement(list.item(i));
    			
    			if (wallpaper != null) {
    				wallpapers.add(wallpaper);	
    			}
    		}
    		
    		return wallpapers;
        }
        catch (SAXException ex) {
        	throw new ParseException(ex.getMessage(), 0);
        }
        catch (IOException ex) {
        	throw new ParseException(ex.getMessage(), 0);
        }
        catch (ParserConfigurationException ex) {
        	throw new ParseException(ex.getMessage(), 0);
        }
        catch (FactoryConfigurationError ex) {
	        throw new ParseException(ex.getMessage(), 0);
        }
	}

	private Wallpaper getWallpaperByElement(Node node) {
		NodeList subNodes = node.getChildNodes();
		
		String id = null;
		String title = null;
		URI thumbUrl = null;
		URI previewUrl = null;
		URI wallpaperUrl = null;
		String text = null;
		
		for (int i = 0; i < subNodes.getLength(); i++) {
			Node subNode = subNodes.item(i);
			
			if (subNode.getNodeName().equals("id")) {
				id = this.getTextValueByNode(subNode);
			}
			else if (subNode.getNodeName().equals("title")) {
				title = this.getTextValueByNode(subNode);
			}
			else if (subNode.getNodeName().equals("wallpaperUrl")) {
				wallpaperUrl = URI.create(this.getTextValueByNode(subNode));
			}
			else if (subNode.getNodeName().equals("previewUrl")) {
				previewUrl = URI.create(this.getTextValueByNode(subNode));
			}
			else if (subNode.getNodeName().equals("thumbUrl")) {
				thumbUrl = URI.create(this.getTextValueByNode(subNode));
			}
			else if (subNode.getNodeName().equals("text")) {
				text = this.getTextValueByNode(subNode);
			}
		}
		
		return new Wallpaper(id, title, thumbUrl, previewUrl, wallpaperUrl, text);
	}
	
	private String getTextValueByNode(Node node) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node subNote = node.getChildNodes().item(0);
			
			if (subNote.getNodeType() == Node.TEXT_NODE) {
				return subNote.getNodeValue();
			}
		}
		
		return null;
	}
}