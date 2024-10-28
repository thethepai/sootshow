package org.example;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.PatchingChain;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.SootField;
import soot.Unit;
import soot.jimple.Stmt;
import soot.util.Chain;

public class APIPrintTransformer extends SceneTransformer {

	private static final String ANDROID_SENSOR_PREFIX = "android.hardware.Sensor";

	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) {
		// 获取所有应用类
		Chain<SootClass> sootClasses = Scene.v().getApplicationClasses();

		// 处理每个类的方法和字段
		for (SootClass sc : sootClasses) {
			processMethods(sc);
			processFields(sc);
		}
	}

	private void processMethods(SootClass sc) {
		// 获取类中的所有方法
		List<SootMethod> sootMethods = sc.getMethods();

		for (SootMethod sm : sootMethods) {
			try {
				// 获取方法体
				Body body = sm.retrieveActiveBody();
				PatchingChain<Unit> units = body.getUnits();

				// 遍历方法体中的每个语句
				for (Unit unit : units) {
					Stmt stmt = (Stmt) unit;
					System.out.println(stmt.toString());

					// 如果语句包含方法调用
					if (stmt.containsInvokeExpr()) {
						SootMethod callee = stmt.getInvokeExpr().getMethod();
						SootClass calleeClass = callee.getDeclaringClass();
						String calleeClassName = calleeClass.getName();

						// 如果调用的方法属于Android传感器类
						if (calleeClassName.startsWith(ANDROID_SENSOR_PREFIX)) {
							// 需要时进行额外处理
						}
					}
				}
			} catch (Exception ex) {
				// 适当处理异常
			}
		}
	}

	private void processFields(SootClass sc) {
		// 获取类中的所有字段
		Chain<SootField> sootFields = sc.getFields();

		for (SootField sf : sootFields) {
			SootClass calleeClass = sf.getDeclaringClass();
			String calleeClassName = calleeClass.getName();
			// 需要时进行额外处理
		}
	}
}