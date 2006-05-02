package com.sun.wts.tools.mri;

/**
 * Parsed POM info.
 * 
 * @author Kohsuke Kawaguchi
 */
public class Pom {
    final String groupId;
    final String version;
    final String artifactId;

    public Pom(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
}
