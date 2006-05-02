package com.sun.wts.tools.mri;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Checksum;
import org.apache.tools.ant.types.FilterSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ant task that imports a jar file into a maven repository
 * by generating a POM and checksum.
 *
 * @author Kohsuke Kawaguchi
 */
public class TaskImpl extends Task {
    private String version;
    private File destdir;
    private final List filterSets = new ArrayList();
    private final List artifacts = new ArrayList();

    public void setDestdir(File destdir) {
        this.destdir = destdir;
    }

    public void setVersion(String version) {
        if(version.startsWith("-"))
            version = version.substring(1);
        this.version = version;
    }

    public FilterSet createFilterSet() {
        FilterSet filterSet = new FilterSet();
        filterSets.add(filterSet);
        return filterSet;
    }

    public void addConfiguredArtifact(Artifact a) {
        artifacts.add(a);
    }

    public void execute() throws BuildException {
        if(artifacts.isEmpty())
            throw new BuildException("No <artifact> is given");
        if(version==null)
            throw new BuildException("No @version is given");
        if(destdir==null)
            throw new BuildException("No @destdir is given");

        for( int i=0; i<artifacts.size(); i++ ) {
            Artifact a = (Artifact) artifacts.get(i);

            log("Importing "+a.getJar(), Project.MSG_INFO);

            // create directories
            Pom p = parsePom(a);
            File groupDir = new File(destdir,p.groupId);
            File jars = new File(groupDir,"jars");
            File poms = new File(groupDir,"poms");
            File srcs = new File(groupDir,"java-sources");
            jars.mkdirs();
            poms.mkdirs();
            srcs.mkdirs();

            // copy the files over
            File outJar = getMavenizedName(jars, p, ".jar");
            copy(outJar, a.getJar(), false);
            calcChecksum(outJar);

            File outPom = getMavenizedName(poms, p, ".pom");
            copy(outPom, a.getPom(), true);
            calcChecksum(outPom);

            if(a.getSrczip()!=null) {
                File outSrc = getMavenizedName(srcs, p, "-sources.jar");
                copy(outSrc, a.getSrczip(), true);
                calcChecksum(outSrc);
            }
        }
    }

    /**
     * Parses the POM.
     */
    private Pom parsePom(Artifact a) {
        try {
            // do the macro substitution
            File tmp = File.createTempFile("mri",null);
            tmp.delete();
            copy(tmp,a.getPom(),true);

            // parse the generated POM.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document dom = dbf.newDocumentBuilder().parse(tmp);

            Pom pom = new Pom(
                getElementText(dom, a, "groupId"),
                getElementText(dom, a, "artifactId"),
                getElementText(dom, a, "version"));

            tmp.delete();

            return pom;
        } catch (IOException e) {
            throw new BuildException(e);
        } catch (SAXException e) {
            throw new BuildException(e);
        } catch (ParserConfigurationException e) {
            throw new BuildException(e);
        }
    }

    private String getElementText(Document dom, Artifact a, String elementName) {
        Element e = DOMUtil.getFirstChild(dom.getDocumentElement(), elementName);
        if(e ==null)
            throw new BuildException(a.getPom()+" doesn't contain "+elementName);
        return DOMUtil.getElementText(e);
    }

    private void copy(File dst, File src, boolean filter) {
        CopyEx cp = new CopyEx();
        cp.setTaskName(getTaskName());
        cp.setProject(getProject());
        cp.setTofile(dst);
        cp.setFile(src);
        if(filter) {
            cp.getFilterSets().addAll(filterSets);
            FilterSet fs = new FilterSet();
            fs.addFilter("VERSION",version);
            cp.getFilterSets().add(fs);
        }
        cp.execute();
    }

    private void calcChecksum(File f) {
        Checksum md5 = new Checksum();
        md5.setTaskName(getTaskName());
        md5.setProject(getProject());
        md5.setAlgorithm("md5");
        md5.setFile(f);
        md5.execute();
    }

    /**
     * Converts a name like "foo.jar" into "foo-VERSION.jar".
     *
     * <p>
     * Note that the input might already be converted into the right form.
     */
    private File getMavenizedName(File dstDir, Pom pom, String extension) {
        return new File(dstDir,pom.artifactId+'-'+pom.version+extension);
    }
}
