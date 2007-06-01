package com.sun.wts.tools.mri;

import org.apache.tools.ant.Project;

import java.io.File;

/**
 * CLI.
 * 
 * @author Kohsuke Kawaguchi
 */
public class Main {
    public static void main(String[] args) {
        TaskImpl task = new TaskImpl();
        task.setProject(new Project());
        task.setDestdir(new File(args[0]));
        task.setVersion(args[1]);

        Artifact a = new Artifact();
        a.setPom(new File(args[2]));
        a.setJar(new File(args[3]));
        if(args.length==5)
            a.setSrczip(new File(args[4]));
        task.addConfiguredArtifact(a);

        task.execute();
    }
}
