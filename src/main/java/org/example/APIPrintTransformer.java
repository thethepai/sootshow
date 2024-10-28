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
		Chain<SootClass> sootClasses = Scene.v().getApplicationClasses();

		for (SootClass sc : sootClasses) {
			processMethods(sc);
			processFields(sc);
		}
	}

	private void processMethods(SootClass sc) {
		List<SootMethod> sootMethods = sc.getMethods();

		for (SootMethod sm : sootMethods) {
			try {
				Body body = sm.retrieveActiveBody();
				PatchingChain<Unit> units = body.getUnits();

				for (Unit unit : units) {
					Stmt stmt = (Stmt) unit;
					System.out.println(stmt.toString());

					if (stmt.containsInvokeExpr()) {
						SootMethod callee = stmt.getInvokeExpr().getMethod();
						SootClass calleeClass = callee.getDeclaringClass();
						String calleeClassName = calleeClass.getName();

						if (calleeClassName.startsWith(ANDROID_SENSOR_PREFIX)) {
							// Additional processing if needed
						}
					}
				}
			} catch (Exception ex) {
				// Handle exception appropriately
			}
		}
	}

	private void processFields(SootClass sc) {
		Chain<SootField> sootFields = sc.getFields();

		for (SootField sf : sootFields) {
			SootClass calleeClass = sf.getDeclaringClass();
			String calleeClassName = calleeClass.getName();
			// Additional processing if needed
		}
	}
}