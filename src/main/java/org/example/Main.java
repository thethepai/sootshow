package org.example;

import soot.*;
import soot.jimple.JimpleBody;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String APP_PATH = "E:/documents/idea-project/sootshow/sootshow/apks/a.apk";
    private static final String ANDROID_JARS = "E:/documents/idea-project/android-platforms/";
    private static final String CLASS_DIR = "E:/documents/idea-project/sootshow/sootshow/javaclass";
    private static final String CLASS_NAME = "HelloWorld";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select a function to run: ");
        System.out.println("1. analyzeApk");
        System.out.println("2. analyzeClass");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                analyzeApk();
                break;
            case 2:
                analyzeClass();
                break;
            default:
                System.out.println("Unknown choice: " + choice);
        }
    }

    private static void analyzeApk() {

        String[] arguments = {
                "-process-dir", APP_PATH,
                "-android-jars", ANDROID_JARS,
                "-ire",
                "-pp",
                "-allow-phantom-refs",
                "-w",
                "-p", "cg", "enabled:false"
        };

        G.reset();

        APIPrintTransformer transformer = new APIPrintTransformer();

        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_output_format(Options.output_format_none);
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.MethodFeatureTransformer", transformer));

        soot.Main.main(arguments);
    }

    private static void analyzeClass() {
        G.reset();

        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_jimple);

        Options.v().set_soot_classpath(CLASS_DIR);

        Options.v().set_process_dir(java.util.Collections.singletonList(CLASS_DIR));
        Scene.v().loadNecessaryClasses();

        String className = CLASS_NAME;
        SootClass sootClass = Scene.v().loadClassAndSupport(className);
        sootClass.setApplicationClass();

        if (sootClass.hasSuperclass()) {
            SootClass superClass = sootClass.getSuperclass();
            System.out.println(className + " extends Superclass: " + superClass.getName());
        } else {
            System.out.println(className + " has no superclass");
        }

        PackManager.v().getPack("jtp").add(new Transform("jtp.myTransform", new BodyTransformer() {
            @Override
            protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
                if (body instanceof JimpleBody) {
                    System.out.println("Analyzing method: " + body.getMethod().getName());
                    System.out.println(body);
                }
            }
        }));

        PackManager.v().runPacks();
    }
}