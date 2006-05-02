package com.sun.wts.tools.mri;

import org.apache.tools.ant.taskdefs.Copy;

import java.util.Vector;

/**
 * @author Kohsuke Kawaguchi
 */
final class CopyEx extends Copy {
    public Vector getFilterSets() {
        return super.getFilterSets();
    }
}
