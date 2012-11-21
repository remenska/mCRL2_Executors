import org.eclipse.emf.ecore.xmi.*;
import org.eclipse.emf.ecore.xmi.impl.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.*;

import java.io.*;

import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import java.util.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl;
import org.eclipse.uml2.uml.*;
import org.eclipse.emf.ecore.impl.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.uml2.uml.resource.*;
import org.eclipse.uml2.uml.internal.impl.*;

/*class Process {
 String id;
 String className;
 String methodName;
 String[] methodArguments;
 String[] argumentTypes;
 String methodName_return;
 String[] methodName_returnArguments;
 String[] methodName_returnTypes;

 public void setId(String id){ this.id = id; }
 public void setClassName(String className){this.className = className;}
 public void setObjInstanceName(String objInstanceName){this.objInstanceName = objInstanceName;}
 public void setMethodName(String methodName) { this.methodName = methodName; }  
 public String getId() { return this.id;}
 public String getClassName() { return className;}
 public String getObjInstanceName() { return objInstanceName;}
 public String getMethodName() { return methodName; }
 public String[] getMethodArguments() { return methodArguments; }
 public String[] getArgumentTypes() { return argumentTypes; }
 public String getMethodName_return() { return methodName_return; }
 public String[] getMethodName_returnArguments() { return methodName_returnArguments; }
 public String[] getMethodName_returnTypes() { return methodName_returnTypes; }
 }
 */

class LoopProcess extends Process {
	String processSignature;
	String condition;
	String callSignature;
	// LinkedList<String> invocations = new LinkedList<String>();

	public void addLoopSignature(String signature) {
		this.processSignature = signature;
	}

	public void addCondition(String condition) {
		this.condition = condition;
	}

	public void setCallSignature(String callSignature){
		this.callSignature = callSignature;
	}
	
	public String getCallSignature(){
		return this.callSignature;
	}
	
	public String toString() {
		return super.toString() + "\n" + "condition:" + this.condition + "\n"
				+ "loop signature:" + this.processSignature + "\n";
	}

	public StringBuffer prepareForMCRL2() {
		// StringBuffer buffer = new StringBuffer();
		// buffer.append("% Loops not handled for now \n");
		// return buffer;

		StringBuffer buffer = new StringBuffer();
		buffer.append("proc " + this.processSignature + "= \n");
		// sum stuff missing!!
		buffer.append("\t(" + this.condition + ") -> ( \n");

		Iterator it_invocations = invocations.iterator();
		int counter = 0;
		while (it_invocations.hasNext()) {
			counter++;
			buffer.append("\t");
			String step = (String) it_invocations.next();
			if (step.contains("method_") || step.contains("DISET_")
					|| step.contains("loop") || step.endsWith("internal")) {
				buffer.append(step);
				if (counter == invocations.size())
					buffer.append("\n");
				else
					buffer.append(".\n");
			} else {

				buffer.append(step + "\n");

			}
		}
		
		buffer.append("\t ."
				+ this.getCallSignature() +
				") \n");
		buffer.append("\t) <> " + "\n \t\t internal");

		buffer.append(";\n\n");
		return buffer;

	}
}

class Process {
	String id = "1"; // fixed for now, needs to be inspected from Activity
	// diagrams
	ClassImpl classImpl;
	OperationImpl operationImpl;
	String methodSignature;
	String methodReturnSignature;
	LinkedList<String> invocations = new LinkedList<String>();
	HashMap<String, String> opParametersIn = new HashMap<String, String>();
	HashMap<String, String> opParametersReturn = new HashMap<String, String>();
	HashMap<String, String> sumParameters = new HashMap<String, String>();
	boolean isProcessed = false;
	int loopCounter = 0;

	public Process() {
	}

	public ClassImpl getClassImpl() {
		return this.classImpl;
	}

	public void addSumParameter(String key, String value) {
		sumParameters.put(key, value);
	}

	public HashMap<String, String> getSumParameters() {
		return this.sumParameters;
	}

	public OperationImpl getOperationImpl() {
		return this.operationImpl;
	}

	public Collection<ParameterImpl> getParameters(OperationImpl operationarg) {
		return EcoreUtil.getObjectsByType(operationarg.eContents(),
				UMLPackage.Literals.PARAMETER);
	}

	public HashMap<String, String> getOpParametersIn() {
		fillOperationParameters();
		return this.opParametersIn;
	}

	public HashMap<String, String> getOpParametersReturn() {
		fillOperationParameters();
		return this.opParametersReturn;
	}

	private void fillOperationParameters() {

		Collection<ParameterImpl> operationParameters = getParameters(this.operationImpl);
		Iterator parameters_iterator = operationParameters.iterator();

		while (parameters_iterator.hasNext()) {

			ParameterImpl parameterFromCollection = (ParameterImpl) parameters_iterator
					.next();
			// System.out.print(parameterFromCollection.getName());
			// System.out.println("; direction="+parameterFromCollection.getDirection().toString());

			// return parameters
			if (parameterFromCollection.getDirection().toString()
					.equals("return")) {
				if (parameterFromCollection.getType().getClass()
						.equals(PrimitiveTypeImpl.class)) {
					this.opParametersReturn
							.put(parameterFromCollection.getName(),
									Test1.determinePrimitiveType((PrimitiveTypeImpl) parameterFromCollection
											.getType()));
				}

				else {
					this.opParametersReturn.put(
							parameterFromCollection.getName(), "ClassObject");
				}

			}

			// input parameters
			else {

				if (parameterFromCollection.getType().getClass()
						.equals(PrimitiveTypeImpl.class)) {
					this.opParametersIn
							.put(parameterFromCollection.getName(),
									Test1.determinePrimitiveType((PrimitiveTypeImpl) parameterFromCollection
											.getType()));
				}

				else {
					this.opParametersIn.put(parameterFromCollection.getName(),
							"ClassObject");
				}

			}

		}
	}

	public LinkedList<String> getInvocations() {
		return this.invocations;
	}

	public int getLoopCounter() {
		return this.loopCounter;
	}

	public Process(ClassImpl classImpl, OperationImpl operationImpl) {
		this.classImpl = classImpl;
		this.operationImpl = operationImpl;
	}

	public void addMethodSignature(String signature) {
		this.methodSignature = signature;
	}

	public void addMethodReturnSignature(String returnSignature) {
		this.methodReturnSignature = returnSignature;
	}

	public void setClassImpl(ClassImpl classImpl) {
		this.classImpl = classImpl;
	}

	public void setOperationImpl(OperationImpl operationImpl) {
		this.operationImpl = operationImpl;
	}

	public void addInvocation(String invocation) {
		invocations.add(invocation);
	}

	public void addAltFragment(String guard) {
		invocations.add("(" + guard + ")->(");
	}

	public void addOptFragment(String guard) {
		invocations.add("((" + guard + ")->(");
	}

	public void closeOptFragment() {
		invocations.add(")<>internal)");
	}

	public void addEndAltFragment(boolean lastStep) {
		if (lastStep)
			invocations.add(")");
		else
			invocations.add(" ) <> ");
	}

	public void addCloseFragment(boolean withInternal) {
		if (withInternal)
			invocations.add("<> internal)");
		else
			invocations.add(")");
	}

	public void addBeginAltFragment() {
		invocations.add("(");
	}

	public void addCallLoopFragment(String operation) {
		loopCounter++;
		invocations.add(operation);

	}

	public boolean equals(Process anotherProc) {
		return (this.classImpl == anotherProc.classImpl && this.operationImpl == anotherProc.operationImpl);
	}

	public String toString() {
		if (classImpl == null && operationImpl == null)
			return "No class && operation signature" + "\n" // starter process
					+ invocations.toString() + "\n";
		else if (classImpl == null) // this case happens only in loop processes
			return "No classImpl but YES operationImpl \n"
					+ this.methodSignature + "\n" + this.methodReturnSignature
					+ "\n+" + " " + invocations.toString() + "\n"
					+ "operation:" + this.operationImpl.getName() + "\n";
		// else if (operationImpl == null) //no such case??
		// return "No operationImpl \n"
		// +"Class:" + this.classImpl.getName() + "\n"
		// + this.methodSignature + "\n" + this.methodReturnSignature
		// + "\n+" + " " + invocations.toString() + "\n;";
		else
			return "Everything in place \n" // regular process
					+ "Class:" + this.classImpl.getName()
					+ "\n"
					+ this.methodSignature + "\n"
					+ this.methodReturnSignature
					+ "\n+" + " " + invocations.toString()
					+ "\n"
					+ "operation:" + this.operationImpl.getName() + "\n";
	}

	public void setProcessed() {
		this.isProcessed = true;
	}

	public boolean isProcessed() {
		return this.isProcessed;
	}

	public boolean equalsClassNameANDOperationName(String className,
			String operationName) {
		return (this.classImpl.getName().equals(className) && this.operationImpl
				.getName().equals(operationName));
	}

	public StringBuffer prepareForMCRL2() {
		StringBuffer buffer = new StringBuffer();
		if (classImpl == null && operationImpl == null)
			buffer.append("proc starter(id:Nat) = \n");
		else {
			buffer.append("proc " + this.classImpl.getName() + "_"
					+ this.operationImpl.getName() + "(id:Nat) = \n");
			// sum stuff missing!!
		}
		Iterator it_invocations = invocations.iterator();
		int counter = 0;
		while (it_invocations.hasNext()) {
			counter++;
			buffer.append("\t");
			String step = (String) it_invocations.next();
			if (step.contains("method_") || step.contains("DISET_")
					|| step.contains("loop") || step.contains("internal")) {
				buffer.append(step);
				if (counter == invocations.size())
					buffer.append("\n");
				else
					buffer.append(".\n");
			} else {

				buffer.append(step + "\n");

			}
		}

		buffer.append(";\n\n");
		return buffer;
	}
}

public class Test1 {

	public static LinkedList<Process> processes = new LinkedList<Process>();
	public static LinkedList<LoopProcess> loop_processes = new LinkedList<LoopProcess>();
	public static LinkedList<String> ClassType = new LinkedList<String>();
	public static LinkedList<String> ClassObject = new LinkedList<String>();
	public static LinkedList<String> OperationSignatures = new LinkedList<String>();
	public static HashMap<String, Stack<Process>> readyProcessesPerLifeline = new HashMap<String, Stack<Process>>();
	public static HashMap<String, Stack<Process>> busyProcessesPerLifeline = new HashMap<String, Stack<Process>>();
	public static LinkedList<String> SortString = new LinkedList<String>();

	public static int loopCounter = 1;

	public static FileWriter fstream;
	public static BufferedWriter outfile;

	public static void createSorts(org.eclipse.uml2.uml.Package rootPackage)
			throws IOException {
		fstream = new FileWriter(
				"/home/daniela/remenska/Documents/LHCb/Adrian/workspace/FormalMethods/src/model.mcrl2");
		outfile = new BufferedWriter(fstream);
		Collection<org.eclipse.uml2.uml.internal.impl.ClassImpl> classes = getClasses(rootPackage);
		// System.out.println(classes);
		Iterator classes_iterator = classes.iterator();
		while (classes_iterator.hasNext()) {
			ClassImpl classFromCollection = (ClassImpl) classes_iterator.next();

			System.out.println("Class:" + classFromCollection.getName());

			ClassType.add(classFromCollection.getName());

			Collection<OperationImpl> classOperations = getOperations(classFromCollection);
			Iterator operations_iterator = classOperations.iterator();
			System.out.println("---OPERATIONS----");
			while (operations_iterator.hasNext()) {
				// each operation becomes a process
				Process newProcess = new Process();
				newProcess.setClassImpl(classFromCollection);
				OperationImpl operationFromCollection = (OperationImpl) operations_iterator
						.next();
				System.out.println(operationFromCollection.getName());
				newProcess.setOperationImpl(operationFromCollection);
				// MethodSignatures.add(operationFromCollection.getName());
				StringBuffer operationSignature = new StringBuffer(
						operationFromCollection.getName());
				StringBuffer operationSignature_return = new StringBuffer(
						operationFromCollection.getName() + "_return");

				// System.out.print(((org.eclipse.uml2.uml.Element)operationFromCollection).getOwner());
				// HOW TO GET PARENT(OWNER) OF A A TAGGED ELEMENT
				Element ownerClass = ((org.eclipse.uml2.uml.Element) operationFromCollection)
						.getOwner();
				System.out.println(((ClassImpl) ownerClass).getName());

				Collection<ParameterImpl> operationParameters = getParameters(operationFromCollection);
				Iterator parameters_iterator = operationParameters.iterator();
				StringBuffer operationParametersIn = new StringBuffer();
				StringBuffer operationParametersReturn = new StringBuffer();
				System.out.println("---PARAMETERS----");
				while (parameters_iterator.hasNext()) {

					ParameterImpl parameterFromCollection = (ParameterImpl) parameters_iterator
							.next();
					// System.out.print(parameterFromCollection.getName());
					// System.out.println("; direction="+parameterFromCollection.getDirection().toString());

					// return parameters
					if (parameterFromCollection.getDirection().toString()
							.equals("return")) {
						operationParametersReturn
								.append(parameterFromCollection.getName() + ":");
						if (parameterFromCollection.getType().getClass()
								.equals(PrimitiveTypeImpl.class))
							operationParametersReturn
									.append(determinePrimitiveType((PrimitiveTypeImpl) parameterFromCollection
											.getType()) + ",");
						else
							operationParametersReturn
									.append(parameterFromCollection.getType()
											.getName());
					}

					// input parameters
					else {
						operationParametersIn.append(parameterFromCollection
								.getName() + ":");
						if (parameterFromCollection.getType().getClass()
								.equals(PrimitiveTypeImpl.class))
							operationParametersIn
									.append(determinePrimitiveType((PrimitiveTypeImpl) parameterFromCollection
											.getType()) + ",");
						// operationSignature.append();
						else
							operationParametersIn
									.append(parameterFromCollection.getType()
											.getName() + ",");
					}

				}
				System.out.println("---END_PARAMETERS----");
				if (operationParametersIn.length() != 0) {
					OperationSignatures.add(operationSignature.toString()
							+ "("
							+ operationParametersIn.toString().substring(0,
									operationParametersIn.length() - 1) + ")");

					newProcess.addMethodSignature(operationSignature.toString()
							+ "("
							+ operationParametersIn.toString().substring(0,
									operationParametersIn.length() - 1) + ")");
				} else {
					OperationSignatures.add(operationSignature.toString());
					newProcess
							.addMethodSignature(operationSignature.toString());
				}

				if (operationParametersReturn.length() != 0) {
					OperationSignatures.add(operationSignature_return
							.toString()
							+ "("
							+ operationParametersReturn.toString().substring(0,
									operationParametersReturn.length() - 1)
							+ ")");
					// copied from above, probably should be adapted for
					// sum:type blabla in mcrl2
					newProcess
							.addMethodReturnSignature(operationSignature_return
									.toString()
									+ "("
									+ operationParametersReturn.toString()
											.substring(
													0,
													operationParametersReturn
															.length() - 1)
									+ ")");

				} else {
					OperationSignatures.add(operationSignature_return
							.toString());
					newProcess
							.addMethodReturnSignature(operationSignature_return
									.toString());
				}
				processes.add(newProcess);

			}
			System.out.println("--END_OPERATIONS-----");
		}
		System.out.println("--END_CLASSES-----");
		printSortsMCRL2();
	}

	public static void printSortsMCRL2() throws IOException {
		// FOR PRINTOUT IN mCRL2 file!
		// sort ClassType =...
		ListIterator<String> ClassType_st = ClassType.listIterator(0);
		// System.out.println("sort ClassType = struct ");
		// outfile.newLine();
		outfile.write("%comment");
		outfile.newLine();
		outfile.write("%-------sorts---------");
		outfile.newLine();
		outfile.write("sort ClassType = struct ");
		outfile.newLine();
		while (ClassType_st.hasNext()) {
			outfile.write("\t\t\t" + ClassType_st.next());

			if (ClassType_st.hasNext()) {
				// System.out.println(" | ");
				outfile.write(" | ");
				outfile.newLine();
			} else {
				outfile.write(" ; ");
				outfile.newLine();
				// System.out.println(" ; ");

			}
		}
		// sort Method = ...
		// System.out.println("sort Method = struct ");
		outfile.write("sort Method = struct ");
		outfile.newLine();
		ListIterator<String> OperationSignatures_st = OperationSignatures
				.listIterator(0);
		while (OperationSignatures_st.hasNext()) {
			// System.out.print("\t\t\t" + OperationSignatures_st.next());
			outfile.write("\t\t\t" + OperationSignatures_st.next());
			if (OperationSignatures_st.hasNext()) {
				// System.out.println(" | ");
				outfile.write(" | ");
				outfile.newLine();
			} else {
				// System.out.println(" ; ");
				outfile.write(" ; ");
				outfile.newLine();
			}
		}
		// objects left ClassObject = ... <- to be taken from lifelines

		// FOR PRINTOUT IN mCRL2 file!
	}

	public static void createClassObjectSort(
			org.eclipse.uml2.uml.Package rootPackage) throws IOException {

		TreeIterator<EObject> treeIt = rootPackage.eAllContents();
		LinkedList<EObject> allElements = new LinkedList<EObject>();
		// System.out.println("sort ClassObject = struct");
		outfile.newLine();
		outfile.write("sort ClassObject = struct");
		outfile.newLine();
		while (treeIt.hasNext()) {
			EObject el = treeIt.next();
			if (el.getClass().equals(LifelineImpl.class)) {
				allElements.add(el);
			}
		}

		Iterator<EObject> lifelines_iterator = allElements.iterator();
		LinkedList<String> knownObjects = new LinkedList<String>();
		while (lifelines_iterator.hasNext()) {
			// System.out.print("\t\t\t"
			// + ((LifelineImpl) lifelines_iterator.next())
			// .getRepresents().getName());
			String nameObject = ((LifelineImpl) lifelines_iterator.next())
					.getRepresents().getName();
			if (!knownObjects.contains(nameObject))
				knownObjects.add(nameObject);
		}

		Iterator it_knownObjects = knownObjects.iterator();
		while (it_knownObjects.hasNext()) {
			outfile.write("\t\t\t" + it_knownObjects.next());

			// just temp, to know how to extract the class a message belongs to
			// System.out.print("\t\t\t"+((LifelineImpl)lifelines_iterator.next()).getRepresents().getType().getName());
			if (it_knownObjects.hasNext()) {
				// System.out.println(" |");
				outfile.write(" |");
				outfile.newLine();

			} else {
				// System.out.println(" ;");
				outfile.write(" ;");
				outfile.newLine();

			}
		}

		outfile.newLine();
		outfile.write("%-------end sorts---------");
	}

	public static LinkedList<CollaborationImpl> getAllCollaborations(
			org.eclipse.uml2.uml.Package rootPackage) {
		TreeIterator<EObject> treeColab = rootPackage.eAllContents();
		LinkedList<CollaborationImpl> allColaborations = new LinkedList<CollaborationImpl>();
		while (treeColab.hasNext()) {
			EObject el = treeColab.next();
			if (el.getClass().equals(CollaborationImpl.class)) {
				allColaborations.add((CollaborationImpl) el);
				System.out.println("Collaboration:"
						+ ((CollaborationImpl) el).getName());
				getAllInteractionsForCollaboration((CollaborationImpl) el);
			}
		}
		return allColaborations;
	}

	public static LinkedList<InteractionImpl> getAllInteractionsForCollaboration(
			CollaborationImpl collaboration) {
		TreeIterator<EObject> treeInteractions = collaboration.eAllContents();
		LinkedList<InteractionImpl> allInteractions = new LinkedList<InteractionImpl>();
		while (treeInteractions.hasNext()) {
			EObject el = treeInteractions.next();
			if (el.getClass().equals(InteractionImpl.class)) {
				allInteractions.add((InteractionImpl) el);
				System.out.println("Interaction:"
						+ ((InteractionImpl) el).getName());
				getLifeLinesForInteraction((InteractionImpl) el);
				getFragmentsForInteraction((InteractionImpl) el);
			}
		}
		return allInteractions;
	}

	public static LinkedList<LifelineImpl> getLifeLinesForInteraction(
			InteractionImpl interaction) {
		TreeIterator<EObject> treeLifelines = interaction.eAllContents();
		LinkedList<LifelineImpl> allLifelines = new LinkedList<LifelineImpl>();
		readyProcessesPerLifeline = new HashMap<String, Stack<Process>>();
		busyProcessesPerLifeline = new HashMap<String, Stack<Process>>();
		// System.out.println("ENTIRE STACK:"+stackPerLifeline.entrySet().toString());
		while (treeLifelines.hasNext()) {
			EObject el = treeLifelines.next();
			if (el.getClass().equals(LifelineImpl.class)) {
				allLifelines.add((LifelineImpl) el);
				System.out.print("Lifeline:"
						+ ((LifelineImpl) el).getRepresents().getName());
				System.out.println("; type:"
						+ ((LifelineImpl) el).getRepresents().getType()
								.getName());
				Process initProcess = new Process();
				Stack<Process> initStack = new Stack<Process>();
				initStack.push(initProcess);
				processes.add(initProcess);
				System.out.println("Want to put:");
				System.out.println("Key:"
						+ ((LifelineImpl) el).getRepresents().getName()
								.toString());
				System.out.println("Value:" + initStack.toString());
				readyProcessesPerLifeline.put(((LifelineImpl) el)
						.getRepresents().getName().toString(), initStack);
				busyProcessesPerLifeline.put(((LifelineImpl) el)
						.getRepresents().getName().toString(), new Stack());
				// System.out.println("ENTIRE STACK:"+stackPerLifeline.entrySet().toString());
			}
		}
		return allLifelines;
	}

	public static String getMessageArguments(Message message) {

		EList arguments = message.getArguments();
		Iterator arguments_iterator = arguments.iterator();
		StringBuffer args = new StringBuffer();
		boolean isFirst = true;
		int count = 0;
		while (arguments_iterator.hasNext()) {
			OpaqueExpression argument = (OpaqueExpression) arguments_iterator
					.next();
			// System.out.print("; arg="+argument.getBodies().get(0));
			if (isFirst)
				args.append("(");
			count++;
			if (count == arguments.size())
				args.append(argument.getBodies().get(0).replace("\"", "") + ")");
			// strip the quotes of a string: .replace("\"","")
			else
				args.append(argument.getBodies().get(0).replace("\"", "") + ",");
			isFirst = false;
			if (argument.getBodies().get(0).startsWith("\"")
					&& !SortString.contains(argument.getBodies().get(0)))
				SortString.add(argument.getBodies().get(0).replace("\"", ""));
		}

		return args.toString();
	}

	// in method_call_begin and method_call_end we have (id,
	// Class,obj,methodName..) so we need
	// the class and object to pass

	// method_call_begin
	public static String getClassAndObjectForMCB(Message message) {
		StringBuffer args = new StringBuffer();
		// if we're dealing with a gate
		if (message.getReceiveEvent().getClass()
				.equals(org.eclipse.uml2.uml.internal.impl.GateImpl.class)) {
			GateImpl actualGateCalling = (GateImpl) message.getReceiveEvent();
			// we neeed the owner fragment InteractionUse that will tell us
			// which lifeline it covers
			Element ownerClass = ((org.eclipse.uml2.uml.Element) actualGateCalling)
					.getOwner();
			InteractionFragmentImpl ownerFragment = (InteractionFragmentImpl) ownerClass;
			args.append(ownerFragment.getCovered(null).getRepresents()
					.getType().getName()
					+ ","
					+ ownerFragment.getCovered(null).getRepresents().getName()
					+ ",");

			// System.out.println(((ClassImpl) ownerClass).getName());

		} else if (message
				.getReceiveEvent()
				.getClass()
				.equals(org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl.class)) {
			InteractionFragmentImpl event = (MessageOccurrenceSpecificationImpl) message
					.getReceiveEvent();

			args.append(event.getCovered(null).getRepresents().getType()
					.getName()
					+ ","
					+ event.getCovered(null).getRepresents().getName()
					+ ",");
		}
		return args.toString();
	}

	// method_call_end
	public static String getClassAndObjectForMCE(Message message) {
		StringBuffer args = new StringBuffer();
		// if we're dealing with a gate
		if (message.getSendEvent().getClass()
				.equals(org.eclipse.uml2.uml.internal.impl.GateImpl.class)) {
			GateImpl actualGateReceiving = (GateImpl) message.getSendEvent();
			Element ownerClass = ((org.eclipse.uml2.uml.Element) actualGateReceiving)
					.getOwner();
			InteractionFragmentImpl ownerFragment = (InteractionFragmentImpl) ownerClass;
			args.append(ownerFragment.getCovered(null).getRepresents()
					.getType().getName()
					+ ","
					+ ownerFragment.getCovered(null).getRepresents().getName()
					+ ",");

		} else if (message
				.getSendEvent()
				.getClass()
				.equals(org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl.class)) {

			InteractionFragmentImpl event = (MessageOccurrenceSpecificationImpl) message
					.getSendEvent();

			args.append(event.getCovered(null).getRepresents().getType()
					.getName()
					+ ","
					+ event.getCovered(null).getRepresents().getName()
					+ ",");
		}

		return args.toString();
	}

	public static void extractFragments(
			Collection<InteractionFragmentImpl> fragments,
			InteractionOperand operand, String operator, boolean firstOperand,
			boolean lastOperand, String guard, boolean insideOperand) {

		Iterator fragments_iterator = fragments.iterator();

		boolean firstSendFound = false;
		Process responsibleProcess = null;
		LoopProcess loopProcess = null;
		Stack theReadyStack = null;
		Stack theBusyStack = null;
		while (fragments_iterator.hasNext()) {
			InteractionFragmentImpl el = (InteractionFragmentImpl) fragments_iterator
					.next();
			if (el.getClass().equals(MessageOccurrenceSpecificationImpl.class)) {
				System.out.println("========");
				System.out.print("MessageOccurence:"
						+ ((InteractionFragmentImpl) el).getCovered(null)
								.getRepresents().getName());
				System.out.print("; represents:"
						+ ((InteractionFragmentImpl) el).getCovered(null)
								.getRepresents().getType().getName());
				System.out.println("; Message:"
						+ ((MessageOccurrenceSpecificationImpl) el)
								.getMessage().getName());

				String arguments = getMessageArguments(((MessageOccurrenceSpecificationImpl) el)
						.getMessage());

				MessageOccurrenceSpecificationImpl occurence = (MessageOccurrenceSpecificationImpl) el;
				Event event = occurence.getEvent();
				// 4 cases:
				// 1. SendOccurence synchCall
				// 2. SendOccurence reply
				// 3. ReceiveOccurence synchCall
				// 4. ReceiveOccurence reply
				System.out.println("sort:"
						+ occurence.getMessage().getMessageSort());

				theReadyStack = readyProcessesPerLifeline
						.get(((InteractionFragmentImpl) el).getCovered(null)
								.getRepresents().getName());
				theBusyStack = busyProcessesPerLifeline
						.get(((InteractionFragmentImpl) el).getCovered(null)
								.getRepresents().getName());
				System.out.println("Ready stack BEFORE message occurence:"
						+ theReadyStack.toString());
				System.out.println("Busy stack BEFORE message occurence:"
						+ theBusyStack.toString());

				if (occurence.getMessage().getSendEvent() == el) {
					System.out.print("DIRECTION; sending; ");
					SendOperationEvent sendEvent = (SendOperationEvent) event;
					System.out.println("Original operation:"
							+ sendEvent.getOperation().getName());
					if (occurence.getMessage().getMessageSort().toString()
							.equals("synchCall")) {
						// Case 1:

						Process theProcess = (Process) theReadyStack.pop();

						if (!firstSendFound && insideOperand) {
							responsibleProcess = theProcess;
							if (operator.equals("alt")) {
								if (firstOperand)
									responsibleProcess.addBeginAltFragment();
								if (!guard.equals("else"))
									responsibleProcess.addAltFragment(guard);
								else
									responsibleProcess.addBeginAltFragment();
							}

							else if (operator.equals("opt")) {
								responsibleProcess.addOptFragment(guard);
							} else if (operator.equals("loop")) {
								StringBuffer paramsToPass = new StringBuffer();
								StringBuffer signatureParams = new StringBuffer();
								for (Map.Entry<String, String> entry : responsibleProcess
										.getSumParameters().entrySet()) {
									// String key = entry.getKey();
									paramsToPass.append("," + entry.getKey());
									signatureParams.append("," + entry.getKey()
											+ ":" + entry.getValue());
								}

								responsibleProcess
										.addCallLoopFragment(responsibleProcess.operationImpl
												.getName()
												+ "_loop"
												+ loopCounter
												+ "(id,obj"
												+ paramsToPass.substring(0,
														paramsToPass.length())
												+ ")");
								theReadyStack.push(theProcess); // push it back
																// to ready
																// again, since
																// it's only a
																// call to
																// "_loop(id)."
								// no method_call_begin and method_call_end

								loopProcess = new LoopProcess();
								loopProcess.setCallSignature(responsibleProcess.operationImpl
										.getName()
										+ "_loop"
										+ loopCounter
										+ "(id,obj"
										+ paramsToPass.substring(0,
												paramsToPass.length())
										+ ")"); //call signature is the last thing inside the loop
												// .loop_repeatAgain(...) <> internal
								
								loopProcess //this is the process signature
										.addLoopSignature(responsibleProcess.operationImpl
												.getName()
												+ "_loop"
												+ loopCounter
												+ "(id:Nat,obj:ClassObject"
												+ signatureParams.substring(0,
														signatureParams
																.length())
												+ ")");
								loopCounter++;
								loopProcess
										.setOperationImpl(responsibleProcess.operationImpl);
								// loopProcess.setClassImpl(responsibleProcess.classImpl);
								loopProcess.addCondition(guard);
								processes.add(loopProcess);
								//
								// now, should set the _loop Process as the
								// active one on the lifeline
								// so that all the calls below will go there
								theProcess = loopProcess;

							}

							firstSendFound = true;
						}

						String classAndobject = getClassAndObjectForMCB(((MessageOccurrenceSpecificationImpl) el)
								.getMessage());

						theProcess.addInvocation("method_call_begin(id,"
								+ classAndobject
								+ ((MessageOccurrenceSpecificationImpl) el)
										.getMessage().getName() + arguments
								+ ")");
						// System.out.println("!!!!Stack:" +
						// theStack.toString());

						// now push it to busy
						theBusyStack = busyProcessesPerLifeline
								.get(((InteractionFragmentImpl) el)
										.getCovered(null).getRepresents()
										.getName());
						theBusyStack.push(theProcess);

					} else if (occurence.getMessage().getMessageSort()
							.toString().equals("reply")) {
						// Case 2:
						theReadyStack = readyProcessesPerLifeline
								.get(((InteractionFragmentImpl) el)
										.getCovered(null).getRepresents()
										.getName());
						Process theProcess = (Process) theReadyStack.pop();

						if (!theProcess.isProcessed) {
							theProcess.addInvocation("method_var_end(id,"
									+ ((InteractionFragmentImpl) el)
											.getCovered(null).getRepresents()
											.getType().getName()
									+ ",obj,"
									+ ((MessageOccurrenceSpecificationImpl) el)
											.getMessage().getName() + "_return"
									+ arguments + ")");
							theProcess.setProcessed();
						}

						// System.out.println("!!!!Stack:" +
						// theReadyStack.toString());
					} else if (occurence.getMessage().getMessageSort()
							.toString().equals("asynchCall")) {
						// Case 6: asynch send
						String classAndobject = getClassAndObjectForMCB(((MessageOccurrenceSpecificationImpl) el)
								.getMessage());
						Process theProcess = (Process) theReadyStack.peek();
						theProcess.addInvocation("DISET_call_send("
								+ classAndobject
								+ ((MessageOccurrenceSpecificationImpl) el)
										.getMessage().getName() + arguments
								+ ")");
					}

				} else if (occurence.getMessage().getReceiveEvent() == el) {
					System.out.println("DIRECTION; receiving...");
					ReceiveOperationEvent receiveEvent = (ReceiveOperationEvent) event;
					System.out.println("Original operation:"
							+ receiveEvent.getOperation().getName());

					// for now handle only creating new processes for receive
					// commands
					// CAREFUL, check sort:synchCall vs sort:reply, everything
					// inbetween you need!
					if (occurence.getMessage().getMessageSort().toString()
							.equals("synchCall")) {
						// Case 3:
						OperationImpl opCheck = (OperationImpl) receiveEvent
								.getOperation();
						ClassImpl classCheck = (ClassImpl) ((InteractionFragmentImpl) el)
								.getCovered(null).getRepresents().getType();

						Process findProcess = findProcess(classCheck, opCheck);

						// Process findProcess = (Process) theReadyStack.pop();
						if (findProcess == null) {
							findProcess = new Process(classCheck, opCheck);
							processes.add(findProcess);
						}
						if (!findProcess.isProcessed) {

							StringBuffer changedStep = new StringBuffer();
							StringBuffer changedParams = new StringBuffer();
							changedStep.append("sum ");
							for (Map.Entry<String, String> entry : findProcess
									.getOpParametersIn().entrySet()) {
								String key = entry.getKey();
								String value = entry.getValue();
								changedStep.append(key + ":" + value + ",");
								changedParams.append(key + ",");
								findProcess.addSumParameter(key, value);
							}
							changedStep.append("obj:ClassObject.");

							if (findProcess.getOpParametersIn().size() != 0) { // there
																				// are
																				// method
																				// arguments
								findProcess
										.addInvocation(changedStep
												+ "method_var_begin(id,"
												+ ((InteractionFragmentImpl) el)
														.getCovered(null)
														.getRepresents()
														.getType().getName()
												+ ",obj,"
												+ ((MessageOccurrenceSpecificationImpl) el)
														.getMessage().getName()
												+ "("
												+ changedParams
														.substring(
																0,
																changedParams
																		.length() - 1)
												+ ")" + ")");
							} else { // no method arguments
								findProcess
										.addInvocation(changedStep
												+ "method_var_begin(id,"
												+ ((InteractionFragmentImpl) el)
														.getCovered(null)
														.getRepresents()
														.getType().getName()
												+ ",obj,"
												+ ((MessageOccurrenceSpecificationImpl) el)
														.getMessage().getName()
												+ ")");
							}

						}
						theReadyStack = readyProcessesPerLifeline
								.get(((InteractionFragmentImpl) el)
										.getCovered(null).getRepresents()
										.getName());
						theReadyStack.push(findProcess);
						// System.out.println("!!!!Stack:" +
						// theStack.toString());

					} else if (occurence.getMessage().getMessageSort()
							.toString().equals("reply")) {
						// Case 4:
						// this is for replies from other messages, when the
						// process has called someone
						// now it gets fishy, how do I know to which process to
						// add to? how do I know inside which
						// method this one is called
						// OperationImpl opCheck =
						// (OperationImpl)receiveEvent.getOperation();
						// ClassImpl classCheck = (ClassImpl)
						// ((InteractionFragmentImpl)
						// el).getCovered(null).getRepresents().getType();
						// Process findProcess =
						// findProcess(classCheck,opCheck);

						// find last busy process, pop it
						theBusyStack = busyProcessesPerLifeline
								.get(((InteractionFragmentImpl) el)
										.getCovered(null).getRepresents()
										.getName());
						Process findProcess = (Process) theBusyStack.pop();
						// push it to ready now
						theReadyStack = readyProcessesPerLifeline
								.get(((InteractionFragmentImpl) el)
										.getCovered(null).getRepresents()
										.getName());
						theReadyStack.push(findProcess);

						String classAndobject = getClassAndObjectForMCE(((MessageOccurrenceSpecificationImpl) el)
								.getMessage());

						// for method_call_end we need the sum: ... parameters,
						// which
						// can be obtained only if we know the called process
						// OpParametersReturn dictionary
						Process calledProcess = findProcessViaClassANDOperationName(
								classAndobject.split(",")[0],
								((MessageOccurrenceSpecificationImpl) el)
										.getMessage().getName());
						// preparing for prepending "sum param:Type,..." and
						// appending arguments
						StringBuffer sumParameters = new StringBuffer();
						StringBuffer appendedParameters = new StringBuffer();
						sumParameters.append("sum ");

						for (Map.Entry<String, String> entry : calledProcess
								.getOpParametersReturn().entrySet()) {
							String key = entry.getKey();
							String value = entry.getValue();
							sumParameters.append(key + ":" + value + ",");
							appendedParameters.append(key + ",");
							findProcess.addSumParameter(key, value);
						}

						if (calledProcess.getOpParametersReturn().size() != 0) {
							// prepending the "sum param:Type.."
							// adding the parameter values in the call as well
							// at the end
							findProcess.addInvocation(sumParameters.substring(
									0, sumParameters.length() - 1)
									+ ".method_call_end(id,"
									+ classAndobject
									+ ((MessageOccurrenceSpecificationImpl) el)
											.getMessage().getName()
									+ "_return"
									+ "("
									+ appendedParameters.substring(0,
											appendedParameters.length() - 1)
									+ ")" + ")");

						} // no parameters to add
						else {
							findProcess.addInvocation("method_call_end(id,"
									+ classAndobject
									+ ((MessageOccurrenceSpecificationImpl) el)
											.getMessage().getName() + "_return"
									+ ")");

						}

						// System.out.println("!!!!Stack:" +
						// theStack.toString());
					} else if (occurence.getMessage().getMessageSort()
							.toString().equals("asynchCall")) {

						// Case 5: // asynchronous receive
						OperationImpl opCheck = (OperationImpl) receiveEvent
								.getOperation();
						ClassImpl classCheck = (ClassImpl) ((InteractionFragmentImpl) el)
								.getCovered(null).getRepresents().getType();

						Process findProcess = findProcess(classCheck, opCheck);

						// Process findProcess = (Process) theReadyStack.pop();
						if (findProcess == null) {
							findProcess = new Process(classCheck, opCheck);
							processes.add(findProcess);
						}
						if (!findProcess.isProcessed) {
							findProcess.addInvocation("DISET_call_receive("
									+ ((MessageOccurrenceSpecificationImpl) el)
											.getMessage().getName() + ")");
						}
						theReadyStack = readyProcessesPerLifeline
								.get(((InteractionFragmentImpl) el)
										.getCovered(null).getRepresents()
										.getName());
						theReadyStack.push(findProcess);
						// System.out.println("!!!!Stack:" +
						// theStack.toString());

					}

				}
				// System.out.println("; Message event:"+((MessageOccurrenceSpecificationImpl)el).getMessage().getSendEvent());

				System.out.println("Ready stack AFTER message occurence:"
						+ theReadyStack.toString());
				System.out.println("Busy stack AFTER message occurence:"
						+ theBusyStack.toString());

			} else if (el.getClass().equals(CombinedFragmentImpl.class)) {
				System.out.println("CombinedFragment:"
						+ ((CombinedFragmentImpl) el).getCovered(null)
								.getName());
				System.out.println("interactionOperator:"
						+ ((CombinedFragmentImpl) el).getInteractionOperator());
				getOperandsForCombinedFragment((CombinedFragmentImpl) el,
						((CombinedFragmentImpl) el).getInteractionOperator()
								.toString());
				System.out.println("EndCombinedFragment");
			}
		}
		if (insideOperand) {

			if (operator.equals("alt")) {

				responsibleProcess.addEndAltFragment(lastOperand);
				if (lastOperand)
					if (guard.equals("else"))
						responsibleProcess.addCloseFragment(false);
					else
						responsibleProcess.addCloseFragment(true);
			} else if (operator.equals("opt")) {
				responsibleProcess.closeOptFragment();
			} else if (operator.equals("loop")) {
				// remove the ready _loop process from the queue in order to
				// keep method_var_end appearing
				// in the right process
				theReadyStack.pop();
			}

		}

	}

	public static Collection<InteractionFragmentImpl> getFragmentsForInteraction(
			InteractionImpl interaction) {
		// EList<EObject> fragments = interaction.eContents();
		Collection<InteractionFragmentImpl> fragments = EcoreUtil
				.getObjectsByType(interaction.eContents(),
						UMLPackage.Literals.INTERACTION_FRAGMENT);

		extractFragments(fragments, null, null, false, false, null, false);
		return fragments;
	}

	public static EList<InteractionOperand> getOperandsForCombinedFragment(
			CombinedFragment fragment, String operator) {
		EList<InteractionOperand> operands = fragment.getOperands();
		// System.out.println(operands);
		Iterator operands_iterator = operands.iterator();
		boolean firstOperand = true;
		boolean lastOperand = false;
		int countThem = 0;
		while (operands_iterator.hasNext()) {
			InteractionOperand operand = (InteractionOperand) operands_iterator
					.next();
			// System.out.println("InteractionOperand guard:"+(operand.getGuard().getSpecification()));
			OpaqueExpressionImpl opaqueExpression = (OpaqueExpressionImpl) operand
					.getGuard().getSpecification();
			System.out.println("InteractionOperand guard:"
					+ opaqueExpression.getBodies().get(0));
			// here I should pass the guard in order to insert it to the process
			// where needed
			countThem++;
			if (countThem == operands.size())
				lastOperand = true;
			getFragmentsInsideOperand(operand, operator, firstOperand,
					lastOperand, opaqueExpression.getBodies().get(0).toString());
			// operator=alt, guard=choice1, this>1 etc
			System.out.println("END_InteractionOperand guard:"
					+ opaqueExpression.getBodies().get(0));
			firstOperand = false;
		}
		return operands;
	}

	/**
	 * @param operand
	 * @param operator
	 * @param firstOperand
	 * @param lastOperand
	 * @param guard
	 * @return
	 */
	public static Collection<InteractionFragmentImpl> getFragmentsInsideOperand(
			InteractionOperand operand, String operator, boolean firstOperand,
			boolean lastOperand, String guard) {
		// EList<InteractionFragment> fragments = operand.getFragments();

		Collection<InteractionFragmentImpl> fragments = EcoreUtil
				.getObjectsByType(operand.eContents(),
						UMLPackage.Literals.INTERACTION_FRAGMENT);

		extractFragments(fragments, operand, operator, firstOperand,
				lastOperand, guard, true); // true flag for insideOperand

		return fragments;
	}

	public static void processLifeline(LifelineImpl lifeline) {

	}

	public static boolean checkExists(Process process) {
		ListIterator iterator = processes.listIterator(0);
		boolean exists = false;
		while (iterator.hasNext()) {
			Process p = (Process) iterator;
			if (p.equals(process)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	public static Process findProcess(ClassImpl classImpl,
			OperationImpl operationImpl) {
		Process proc = null;
		ListIterator iterator = processes.listIterator(0);
		while (iterator.hasNext()) {
			Process p = (Process) iterator.next();
			if (p.classImpl == classImpl && p.operationImpl == operationImpl) {
				proc = p;
				break;
			}
		}
		return proc;

	}

	public static Process findProcessViaClassANDOperationName(String className,
			String operationName) {
		Process proc = null;
		ListIterator iterator = processes.listIterator(0);
		while (iterator.hasNext()) {
			Process p = (Process) iterator.next();
			if (p.equalsClassNameANDOperationName(className, operationName)) {
				proc = p;
				break;
			}
		}
		return proc;
	}

	public static void createActionDefinitions() throws IOException {
		outfile.newLine();
		System.out.println("\n\nact method_call_begin,method_var_begin:Nat;");
		outfile.write("%-------action definitions-------");
		outfile.newLine();
		outfile.write("act method_call_begin,method_var_begin:Nat#ClassType#ClassObject#Method;");
		outfile.newLine();
		outfile.write("act method_call_end,method_var_end:Nat#ClassType#ClassObject#Method;");
		outfile.newLine();
		outfile.newLine();
		outfile.write("act method_begin:Nat#ClassType#ClassObject#Method;");
		outfile.newLine();
		outfile.write("act method_end:Nat#ClassType#ClassObject#Method;");
		outfile.newLine();
		outfile.newLine();
		outfile.write("act DISET_call_send,DISET_call_receive:Nat#ClassType#ClassObject#Method;");
		outfile.newLine();
		outfile.write("act DISET_call:Nat#ClassType#ClassObject#Method;");
		outfile.newLine();
		outfile.newLine();
		outfile.write("act internal;");
		outfile.newLine();
		outfile.write("%-------end action definitions-------");
		outfile.newLine();
	}

	public static String determinePrimitiveType(PrimitiveTypeImpl typearg) {
		if (typearg.eProxyURI().toString().contains("Integer"))
			return "Int";
		else if (typearg.eProxyURI().toString().contains("Boolean"))
			return "Bool";
		else if (typearg.eProxyURI().toString().contains("Natural"))
			return "Nat";
		else if (typearg.eProxyURI().toString().contains("String"))
			return "SortString";
		else
			return "None";
	}

	public static Collection<ClassImpl> getClasses(
			org.eclipse.uml2.uml.Package rootPackage) {
		return EcoreUtil.getObjectsByType(rootPackage.eContents(),
				UMLPackage.Literals.CLASS);

	}

	public static Collection<OperationImpl> getOperations(ClassImpl classarg) {
		return EcoreUtil.getObjectsByType(classarg.eContents(),
				UMLPackage.Literals.OPERATION);
	}

	public static Collection<ParameterImpl> getParameters(
			OperationImpl operationarg) {
		return EcoreUtil.getObjectsByType(operationarg.eContents(),
				UMLPackage.Literals.PARAMETER);
	}

	public static void printProcessesMCRL2(Process p) {

	}

	public static void main(String args[]) throws Exception {
		int starter = 0;
		EPackage.Registry.INSTANCE
				.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xml", new XMLResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(
				"http://schema.omg.org/spec/UML/2.1", UMLPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(
				"http://schema.omg.org/spec/UML/2.2", UMLPackage.eINSTANCE);

		Resource resource = null;
		// File f = new File("ExportedProject.uml");
		File f = new File(
				"/home/daniela/Documents/mCRL2_new_models/September2012/mCRL2_Executors/ParsingProject/ExportedModel.uml");

		URI uri = URI.createFileURI(f.getAbsolutePath());
		resource = (UMLResource) resourceSet.getResource(uri, true);

		resource.load(null);
		System.out.println("resource:" + resource);
		System.out.println("resource.isLoaded():" + resource.isLoaded());

		org.eclipse.uml2.uml.Package rootPackage = (org.eclipse.uml2.uml.Package) EcoreUtil
				.getObjectByType(resource.getContents(),
						UMLPackage.Literals.PACKAGE);

		System.out.println("rootPackage:" + rootPackage.getName());
		// ---------------------------
		createSorts(rootPackage);
		createClassObjectSort(rootPackage);
		createActionDefinitions();
		getAllCollaborations(rootPackage);
		createSortStrings();

		ListIterator processes_iterator = processes.listIterator(0);
		System.out.println("PROCESSES:");
		while (processes_iterator.hasNext()) {
			Process process = (Process) processes_iterator.next();
			if (!process.getInvocations().isEmpty()) {// those with no
														// invocations are
														// surplus, the rest are
														// initiators
				StringBuffer processString = process.prepareForMCRL2();
				outfile.append(processString.toString());
				System.out.println("----process-----");
				System.out.println(process);
				if (process.getClassImpl() == null
						&& process.getOperationImpl() == null) {
					starter++;
					System.out.println("starter" + starter);
				} else {
					System.out.println("OpParametersIn:"
							+ process.getOpParametersIn());
					System.out.println("OpParametersReturn:"
							+ process.getOpParametersReturn());
				}
			}
		}
		System.out.println("SORTSTRING:" + SortString);
		outfile.close();
	}

	public static void createSortStrings() throws IOException {
		outfile.newLine();
		if (SortString.size() != 0) {
			outfile.write("sort SortString = struct ");
			outfile.newLine();
			Iterator it_sortString = SortString.iterator();
			while (it_sortString.hasNext()) {
				outfile.write("\t\t\t " + it_sortString.next() + "");
				if (it_sortString.hasNext()) {
					// System.out.println(" |");
					outfile.write(" |");
					outfile.newLine();

				} else {
					// System.out.println(" ;");
					outfile.write(" ;");
					outfile.newLine();

				}
			}
		}

	}
}