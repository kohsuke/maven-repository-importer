package com.sun.wts.tools.mri;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Kohsuke Kawaguchi
 */
public class DOMUtil {
    /**
     * Returns the first element that has the specified local name.
     *
     * The namespace URI is not checked, to support the Maven semantics
     * of "allowing either '' or 'http://maven.apache.org/POM/4.0.0' as the namespace".
     */
    public static Element getFirstChild(Element e, String local) {
        for( Node n=e.getFirstChild(); n!=null; n=n.getNextSibling() ) {
            if(n.getNodeType()==Node.ELEMENT_NODE) {
                Element c = (Element)n;
                if(c.getLocalName().equals(local))
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
