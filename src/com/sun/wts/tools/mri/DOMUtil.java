package com.sun.wts.tools.mri;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Kohsuke Kawaguchi
 */
public class DOMUtil {
    public static Element getFirstChild(Element e, String local) {
        for( Node n=e.getFirstChild(); n!=null; n=n.getNextSibling() ) {
            if(n.getNodeType()==Node.ELEMENT_NODE) {
                Element c = (Element)n;
                if(c.getLocalName().equals(local) && c.getNamespaceURI()==null)
                    return c;
            }
        }
        return null;
    }

    public static String getElementText(Element e) {
        StringBuffer buf = new StringBuffer();
        for( Node n=e.getFirstChild(); n!=null; n=n.getNextSibling() ) {
            if(n.getNodeType()==Node.TEXT_NODE
            || n.getNodeType()==Node.CDATA_SECTION_NODE) {
                buf.append(n.getNodeValue());
            }
        }
        return buf.toString();
    }
}
