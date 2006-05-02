package com.sun.wts.tools.mri;

import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * @author Kohsuke Kawaguchi
 */
public class Artifact {
    private File jar;
    private File pom;
    /**
     * Source zip. May be null.
     */
    private File sourceZip;

    public File getJar() {
        return jar;
    }

    public void setJar(File jar) {
        this.jar = jar;
    }

    public File getPom() {
        return pom;
    }

    public void setPom(File pom) {
        this.pom = pom;
    }

    public void setSrczip(File file) {
        this.sourceZip = file;
    }

    public File getSrczip() {
        return sourceZip;
    }

    /**
     * Makes sure that the configuration is valid.
     */
    public void validate() throws BuildException {
        check(pom, "POM");
        check(jar, "jar");
        if(sourceZip!=null)
            check(sourceZip,"source zip");
    }

    private void check(File f, String caption) {
        if(f==null)
            throw new BuildException("No "+caption +" is specified");
        if(!f.exists())
            throw new BuildException(f+" doesn't exist");
    }
}
