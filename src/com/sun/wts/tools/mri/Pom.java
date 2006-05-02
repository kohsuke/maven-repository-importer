package com.sun.wts.tools.mri;

/**
 * @author Kohsuke Kawaguchi
 */
public class Pom {
    final String groupId;
    final String version;

    public Pom(String groupId, String version) {
        this.groupId = groupId;
        this.version = version;
    }
}
