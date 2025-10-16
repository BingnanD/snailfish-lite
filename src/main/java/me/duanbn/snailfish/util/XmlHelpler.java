package me.duanbn.snailfish.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;

/**
 * 解析xml的辅助工具.
 * 
 * @author bingnan.dbn
 */
public class XmlHelpler {

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private Document xmlDoc;

    /**
     * 构造方法. 读取指定的xml文件
     * 
     * @param xmlFile 指定的xml文件
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public XmlHelpler(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
        StringReader sr = null;
        try {
            sr = new StringReader(xmlContent);
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlDoc = builder.parse(new InputSource(sr));
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    public Node getRoot() {
        NodeList childs = xmlDoc.getChildNodes();
        if (childs == null || childs.getLength() == 0) {
            return null;
        }
        return xmlDoc.getChildNodes().item(0);
    }

    public Node getFirstChildByName(Node parent, String name) {
        NodeList childs = parent.getChildNodes();
        if (childs == null || childs.getLength() == 0) {
            return null;
        }

        Node node = null;
        for (int i = 0; i < childs.getLength(); i++) {
            node = childs.item(i);
            if (node.getNodeName().equals(name)) {
                return node;
            }
        }

        return null;
    }

    public List<Node> getChild(Node parent) {
        List<Node> list = new ArrayList<Node>();
        NodeList childs = parent.getChildNodes();
        if (childs == null || childs.getLength() == 0) {
            return list;
        }

        for (int i = 0; i < childs.getLength(); i++) {
            Node item = childs.item(i);
            if (item instanceof Element) {
                list.add(item);
            }
        }

        return list;
    }

    public List<Node> getChildByName(Node parent, String name) {
        List<Node> list = new ArrayList<Node>();
        NodeList childs = parent.getChildNodes();
        if (childs == null || childs.getLength() == 0) {
            return list;
        }

        Node node = null;
        for (int i = 0; i < childs.getLength(); i++) {
            node = childs.item(i);
            if (node.getNodeName().equals(name)) {
                list.add(node);
            }
        }

        return list;
    }

    public Map<String, String> getAttributeAsMap(Node node, String... ignores) {
        Map<String, String> propMap = new HashMap<String, String>();
        if (node == null) {
            return propMap;
        }

        NamedNodeMap attrMap = node.getAttributes();
        Node attr = null;
        for (int i = 0; i < attrMap.getLength(); i++) {
            attr = attrMap.item(i);
            propMap.put(attr.getNodeName(), attr.getNodeValue());
        }
        for (String ignore : ignores) {
            propMap.remove(ignore);
        }

        return propMap;
    }

    public String getAttributeValue(Node node, String attribute) {
        if (node == null) {
            return null;
        }

        NamedNodeMap namedNodeMap = node.getAttributes();
        if (namedNodeMap == null) {
            return null;
        }

        Node attrNode = namedNodeMap.getNamedItem(attribute);
        if (attrNode == null) {
            return null;
        }

        return attrNode.getNodeValue();
    }

    public Map<String, Object> parseProperties(Node properties) {
        Map<String, Object> result = Maps.newLinkedHashMap();

        if (properties == null) {
            return result;
        }

        List<Node> child = this.getChild(properties);
        for (Node node : child) {
            if (node.getNodeName().equals("property")) {
                String name = this.getAttributeValue(node, "name");
                String value = this.getAttributeValue(node, "value");
                result.put(name, value);
            }
            if (node.getNodeName().equals("list")) {
                String name = this.getAttributeValue(node, "name");
                List<Node> itemList = this.getChildByName(node, "item");
                List<String> itemValue = Lists.newArrayList();
                for (Node item : itemList) {
                    itemValue.add(item.getTextContent());
                }
                result.put(name, itemValue);
            }
        }

        return result;
    }

}
