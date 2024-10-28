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
        // 设置分析APK的参数
        String[] arguments = {
                "-process-dir", APP_PATH, // 指定APK文件路径
                "-android-jars", ANDROID_JARS, // 指定Android平台JAR文件路径
                "-ire", // 启用中间表示扩展
                "-pp", // 启用精确指针分析
                "-allow-phantom-refs", // 允许幻影引用
                "-w", // 启用整个程序分析
                "-p", "cg", "enabled:false" // 禁用调用图生成
        };

        // 重置Soot的全局状态
        G.reset();

        // 创建自定义的SceneTransformer
        APIPrintTransformer transformer = new APIPrintTransformer();

        // 设置Soot的选项
        Options.v().set_src_prec(Options.src_prec_apk); // 设置输入源为APK
        Options.v().set_output_format(Options.output_format_none); // 不生成输出文件
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.MethodFeatureTransformer", transformer)); // 添加自定义的转换器

        // 启动Soot主程序
        soot.Main.main(arguments);
    }

    private static void analyzeClass() {
        // 重置Soot的全局状态
        G.reset();

        // 设置Soot的选项
        Options.v().set_prepend_classpath(true); // 在类路径前添加用户指定的类路径
        Options.v().set_allow_phantom_refs(true); // 允许幻影引用
        Options.v().set_output_format(Options.output_format_jimple); // 设置输出格式为Jimple

        Options.v().set_soot_classpath(CLASS_DIR); // 设置Soot的类路径

        Options.v().set_process_dir(java.util.Collections.singletonList(CLASS_DIR)); // 设置要处理的目录
        Scene.v().loadNecessaryClasses(); // 加载必要的类

        String className = CLASS_NAME;
        SootClass sootClass = Scene.v().loadClassAndSupport(className); // 加载并支持指定的类
        sootClass.setApplicationClass(); // 设置为应用类

        // 检查类是否有超类
        if (sootClass.hasSuperclass()) {
            SootClass superClass = sootClass.getSuperclass();
            System.out.println(className + " extends Superclass: " + superClass.getName());
        } else {
            System.out.println(className + " has no superclass");
        }

        // 添加自定义的BodyTransformer
        PackManager.v().getPack("jtp").add(new Transform("jtp.myTransform", new BodyTransformer() {
            @Override
            protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
                if (body instanceof JimpleBody) {
                    System.out.println("Analyzing method: " + body.getMethod().getName());
                    System.out.println(body);
                }
            }
        }));

        // 运行所有的转换包
        PackManager.v().runPacks();
    }
}