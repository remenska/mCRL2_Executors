
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
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl;
import org.eclipse.uml2.uml.internal.impl.ModelImpl;
import  org.eclipse.uml2.uml.*;
import org.eclipse.emf.ecore.impl.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.uml2.uml.resource.*;
import org.eclipse.uml2.uml.internal.impl.*;

/*class Process {
  String id;
  String className;
  String objInstanceName;
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

public class Test1 {

public static LinkedList<String> ClassType = new LinkedList<String>();
public static LinkedList<String> ClassObject = new LinkedList<String>();
public static LinkedList<String> OperationSignatures = new LinkedList<String>();

  public static void createSorts(org.eclipse.uml2.uml.Package rootPackage){
    Collection<org.eclipse.uml2.uml.internal.impl.ClassImpl> classes = getClasses(rootPackage);
//     System.out.println(classes);
    Iterator classes_iterator = classes.iterator();
    while(classes_iterator.hasNext()){
      ClassImpl classFromCollection = (ClassImpl)classes_iterator.next();
      System.out.println("Class:"+classFromCollection.getName());
      
      ClassType.add(classFromCollection.getName());
      
      Collection<OperationImpl> classOperations = getOperations(classFromCollection);
      Iterator operations_iterator = classOperations.iterator();
      System.out.println("---OPERATIONS----");
      while(operations_iterator.hasNext()){
	OperationImpl operationFromCollection = (OperationImpl)operations_iterator.next();
	System.out.println(operationFromCollection.getName());
	
// 	MethodSignatures.add(operationFromCollection.getName());
	StringBuffer operationSignature = new StringBuffer(operationFromCollection.getName());
	StringBuffer operationSignature_return = new StringBuffer(operationFromCollection.getName()+"_return");

// 	System.out.print(((org.eclipse.uml2.uml.Element)operationFromCollection).getOwner());
	//HOW TO GET PARENT(OWNER) OF A A TAGGED ELEMENT
	Element ownerClass = ((org.eclipse.uml2.uml.Element)operationFromCollection).getOwner();
	System.out.println(((ClassImpl)ownerClass).getName());
	
	Collection<ParameterImpl> operationParameters = getParameters(operationFromCollection);
	Iterator parameters_iterator = operationParameters.iterator();
	    StringBuffer operationParametersIn = new StringBuffer();
	    StringBuffer operationParametersReturn = new StringBuffer();
	System.out.println("---PARAMETERS----");
	  while(parameters_iterator.hasNext()){

	    ParameterImpl parameterFromCollection = (ParameterImpl) parameters_iterator.next();
// 	    System.out.print(parameterFromCollection.getName());
// 	    System.out.println("; direction="+parameterFromCollection.getDirection().toString());
	    
	    if(parameterFromCollection.getDirection().toString().equals("return")){
		operationParametersReturn.append(parameterFromCollection.getName()+":");
		if(parameterFromCollection.getType().getClass().equals(PrimitiveTypeImpl.class))
		  operationParametersReturn.append(determinePrimitiveType((PrimitiveTypeImpl)parameterFromCollection.getType())+",");
		else
		  operationParametersReturn.append(parameterFromCollection.getType().getName());
	    }
	     
	    else {
		operationParametersIn.append(parameterFromCollection.getName()+":");
	    	if(parameterFromCollection.getType().getClass().equals(PrimitiveTypeImpl.class))
		  operationParametersIn.append(determinePrimitiveType((PrimitiveTypeImpl)parameterFromCollection.getType())+",");
// 		  operationSignature.append();
		else
		  operationParametersIn.append(parameterFromCollection.getType().getName()+",");
	    }
	    
	   
	  }
	  System.out.println("---END_PARAMETERS----");
	   if(operationParametersIn.length()!=0){	   				
	      OperationSignatures.add(
		operationSignature.toString()+"("+operationParametersIn.toString().substring(0,operationParametersIn.length()-1)+")"
		);
	    }
	   else
	      OperationSignatures.add(operationSignature.toString());
	     
	    if(operationParametersReturn.length()!=0){	 
	    OperationSignatures.add(
		operationSignature_return.toString()+"("+operationParametersReturn.toString().substring(0,operationParametersReturn.length()-1)+")");
	    }
	      else
	      OperationSignatures.add(operationSignature_return.toString());

	}
	
      System.out.println("--END_OPERATIONS-----");
    }
    System.out.println("--END_CLASSES-----");
    
    // FOR PRINTOUT IN mCRL2 file!
    // sort ClassType =...
    ListIterator<String> ClassType_st = ClassType.listIterator(0);
    System.out.println("sort ClassType = struct ");
    while(ClassType_st.hasNext())  {
      System.out.print("\t\t\t"+ClassType_st.next());
      if(ClassType_st.hasNext()) 
	    System.out.println(" | ");
      else
	    System.out.println(" ; ");
    }
    //sort Method = ...
    System.out.println("sort Method = struct ");
    ListIterator<String> OperationSignatures_st = OperationSignatures.listIterator(0);
    while(OperationSignatures_st.hasNext())  {
      System.out.print("\t\t\t"+OperationSignatures_st.next());
      if(OperationSignatures_st.hasNext()) 
	    System.out.println(" | ");
      else
	    System.out.println(" ; ");
      }
    //objects left ClassObject = ... <- to be taken from lifelines
    
    // FOR PRINTOUT IN mCRL2 file!
  }	
  
  public static void createClassObjectSort(org.eclipse.uml2.uml.Package rootPackage){
    
    TreeIterator<EObject> treeIt =	rootPackage.eAllContents(); 
    LinkedList<EObject> allElements = new LinkedList<EObject>(); 
    System.out.println("sort ClassObject = struct");
    while(treeIt.hasNext()){
	EObject el = treeIt.next();
	if(el.getClass().equals(LifelineImpl.class)){
	    allElements.add(el);
	}
    }
    
    Iterator<EObject> lifelines_iterator = allElements.iterator();
     while(lifelines_iterator.hasNext()){
      System.out.print("\t\t\t"+((LifelineImpl)lifelines_iterator.next()).getRepresents().getName());
      //just temp, to know how to extract the class a message belongs to 
//       System.out.print("\t\t\t"+((LifelineImpl)lifelines_iterator.next()).getRepresents().getType().getName());
	  if(lifelines_iterator.hasNext())
	    System.out.println(" |");
	  else
	    System.out.println(" ;");
    
     }
  
  }
  
  public static LinkedList<CollaborationImpl> getAllCollaborations(org.eclipse.uml2.uml.Package rootPackage){
    TreeIterator<EObject> treeColab =	rootPackage.eAllContents(); 
    LinkedList<CollaborationImpl> allColaborations = new LinkedList<CollaborationImpl>(); 
    while(treeColab.hasNext()){
	EObject el = treeColab.next();
	if(el.getClass().equals(CollaborationImpl.class)){
	    allColaborations.add((CollaborationImpl)el);
	    System.out.println("Collaboration:"+((CollaborationImpl)el).getName());
	    getAllInteractionsForCollaboration((CollaborationImpl)el);
	}
    }
    return allColaborations;
  }
  
  public static LinkedList<InteractionImpl> getAllInteractionsForCollaboration(CollaborationImpl collaboration){
    TreeIterator<EObject> treeInteractions =	collaboration.eAllContents(); 
    LinkedList<InteractionImpl> allInteractions = new LinkedList<InteractionImpl>(); 
    while(treeInteractions.hasNext()){
	EObject el = treeInteractions.next();
	if(el.getClass().equals(InteractionImpl.class)){
	    allInteractions.add((InteractionImpl)el);
	    System.out.println("Interaction:"+((InteractionImpl)el).getName());
	    getLifeLinesForInteraction((InteractionImpl)el);
	    getFragmentsForInteraction((InteractionImpl)el);
	}
    }
    return allInteractions;
  }
  
  public static LinkedList<LifelineImpl> getLifeLinesForInteraction(InteractionImpl interaction){
    TreeIterator<EObject> treeLifelines =	interaction.eAllContents(); 
    LinkedList<LifelineImpl> allLifelines = new LinkedList<LifelineImpl>(); 
    while(treeLifelines.hasNext()){
	EObject el = treeLifelines.next();
	if(el.getClass().equals(LifelineImpl.class)){
	    allLifelines.add((LifelineImpl)el);
	    System.out.println("Lifeline:"+((LifelineImpl)el).getRepresents().getName());
	}
    }
    return allLifelines;
  }
  
  public static Collection<InteractionFragmentImpl> getFragmentsForInteraction(InteractionImpl interaction){
//	    EList<EObject> fragments =	interaction.eContents(); 
	  Collection<InteractionFragmentImpl> fragments =  EcoreUtil.getObjectsByType(interaction.eContents(), UMLPackage.Literals.INTERACTION_FRAGMENT);
	
	  Iterator fragments_iterator = fragments.iterator();
	    
	    while(fragments_iterator.hasNext()){
	    InteractionFragmentImpl el = (InteractionFragmentImpl)fragments_iterator.next();
	      if(el.getClass().equals(MessageOccurrenceSpecificationImpl.class)){
	    	  System.out.print("MessageOccurence:"+((InteractionFragmentImpl)el).getCovered(null).getRepresents().getName());
	    	  System.out.print("; represents:"+((InteractionFragmentImpl)el).getCovered(null).getRepresents().getType().getName());
	    	  System.out.print("; Message:"+((MessageOccurrenceSpecificationImpl)el).getMessage().getName());
	    	  
	    	  //let's see if it's originating from the lifeline, or received by it; for processes, after the
	    	  //first receive we don't care about another one before we return from the received;
	    	  //we just walk down the line and we'll see the next received one eventually
	    	  MessageOccurrenceSpecificationImpl occurence = (MessageOccurrenceSpecificationImpl)el;
	    	  if(occurence.getMessage().getSendEvent()==el){
	    		 System.out.println("; sending...");
	    	  }
	    	  else if(occurence.getMessage().getReceiveEvent()==el){
	    		  System.out.println("; receiving...");
	    	  }
//	    	  System.out.println("; Message event:"+((MessageOccurrenceSpecificationImpl)el).getMessage().getSendEvent());

	      }
	      else if(el.getClass().equals(CombinedFragmentImpl.class)){
	    	  System.out.print("CombinedFragment:"+((CombinedFragmentImpl)el).getCovered(null).getName());
	    	  System.out.println("interactionOperator:"+((CombinedFragmentImpl)el).getInteractionOperator());
	    	  getOperandsForCombinedFragment((CombinedFragmentImpl)el);
	      }
		}
	
	    return fragments;
	  }
  
  public static EList<InteractionOperand> getOperandsForCombinedFragment(CombinedFragment fragment){
	  EList<InteractionOperand> operands = fragment.getOperands();
//	  System.out.println(operands);
	  Iterator operands_iterator = operands.iterator();
	  while(operands_iterator.hasNext()){
		  InteractionOperand operand = (InteractionOperand)operands_iterator.next();
//		  System.out.println("InteractionOperand guard:"+(operand.getGuard().getSpecification()));
		  OpaqueExpressionImpl opaqueExpression = (OpaqueExpressionImpl)operand.getGuard().getSpecification();
		  System.out.println("InteractionOperand guard:"+opaqueExpression.getBodies().get(0));
		  getFragmentsInsideOperand(operand);
	  }
	  return operands;
  }
  
  public static EList<InteractionFragment> getFragmentsInsideOperand(InteractionOperand operand){
	  EList<InteractionFragment> fragments = operand.getFragments();
	  Iterator fragments_iterator = fragments.iterator();
	    
	    while(fragments_iterator.hasNext()){
	    InteractionFragmentImpl el = (InteractionFragmentImpl)fragments_iterator.next();
	      if(el.getClass().equals(MessageOccurrenceSpecificationImpl.class)){
	    	  System.out.print("\t MessageOccurence:"+((InteractionFragmentImpl)el).getCovered(null).getRepresents().getName());
	    	  System.out.print("; represents:"+((InteractionFragmentImpl)el).getCovered(null).getRepresents().getType().getName());
	    	  System.out.println("; Message:"+((MessageOccurrenceSpecificationImpl)el).getMessage().getName());
	    	  //let's see if it's originating from the lifeline, or received by it; for processes, after the
	    	  //first receive we don't care about another one before we return from the received;
	    	  //we just walk down the line and we'll see the next received one eventually
	    	  MessageOccurrenceSpecificationImpl occurence = (MessageOccurrenceSpecificationImpl)el;
	    	  if(occurence.getMessage().getSendEvent()==el){
	    		 System.out.println("; sending...");
	    	  }
	    	  else if(occurence.getMessage().getReceiveEvent()==el){
	    		  System.out.println("; receiving...");
	    	  }
	      }
	      else if(el.getClass().equals(CombinedFragmentImpl.class)){
	    	  System.out.print("\t CombinedFragment:"+((CombinedFragmentImpl)el).getCovered(null).getRepresents().getName());
	    	  System.out.println("interactionOperator:"+((CombinedFragmentImpl)el).getInteractionOperator());
	    	  getOperandsForCombinedFragment((CombinedFragmentImpl)el);
	      }
		}
	  return fragments;
  }
  
  public static void createActionDefinitions(){
  System.out.println("\n\nact method_call_begin,method_var_begin:Nat;");
  }
  
  
  public static String determinePrimitiveType(PrimitiveTypeImpl typearg){
  if(typearg.eProxyURI().toString().contains("Integer")) return "Int";
  else if(typearg.eProxyURI().toString().contains("Boolean")) return "Bool";
  else if(typearg.eProxyURI().toString().contains("Natural")) return "Nat";
  else if(typearg.eProxyURI().toString().contains("String")) return "SortString";
  else return "None";
  }
  
  
  public static Collection<ClassImpl> getClasses(org.eclipse.uml2.uml.Package rootPackage){
     return EcoreUtil.getObjectsByType(rootPackage.eContents(), UMLPackage.Literals.CLASS);

  }
  
  public static Collection<OperationImpl> getOperations(ClassImpl classarg){
    return EcoreUtil.getObjectsByType(classarg.eContents(), UMLPackage.Literals.OPERATION);
  }
  
  public static Collection<ParameterImpl> getParameters(OperationImpl operationarg){
    return EcoreUtil.getObjectsByType(operationarg.eContents(), UMLPackage.Literals.PARAMETER);
  }
  
  
  public static void main(String args[]) throws Exception{
  
      EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
      resourceSet.getPackageRegistry().put("http://schema.omg.org/spec/UML/2.1", UMLPackage.eINSTANCE);
      resourceSet.getPackageRegistry().put("http://schema.omg.org/spec/UML/2.2", UMLPackage.eINSTANCE);

      Resource resource = null;
// 	File f = new File("ExportedProject.uml"); 
	File f = new File("/home/daniela/Documents/mCRL2_new_models/September2012/mCRL2_Executors/ParsingProject/ExportedModel.uml"); 

      URI uri = URI.createFileURI(f.getAbsolutePath());
      resource = (UMLResource)resourceSet.getResource(uri, true);

      resource.load(null);
      System.out.println("resource:"+resource);
      System.out.println("resource.isLoaded():"+resource.isLoaded());

      org.eclipse.uml2.uml.Package rootPackage =  (org.eclipse.uml2.uml.Package)EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
      
      System.out.println("rootPackage:"+rootPackage.getName());
      //---------------------------
      createSorts(rootPackage);
      createClassObjectSort(rootPackage);
      createActionDefinitions();
      getAllCollaborations(rootPackage);
      //--------------------------
//       Iterator package_iterator = rootPackage.iterator();
//       while(package_iterator.hasNext()){
// 	org.eclipse.uml2.uml.Package pkg = (org.eclipse.uml2.uml.Package)package_iterator.next();
// 	System.out.println("name:"+pkg.getName());
//       }
  /*      
      while(model_iterator.hasNext()){
	Model model1 = (Model)model_iterator.next();
	  if (model1.getName().equals("SmallProject")){
	    java.util.Collection<org.eclipse.uml2.uml.internal.impl.PackageImpl> packages =  EcoreUtil.getObjectsByType(model1.eContents(), UMLPackage.Literals.PACKAGE);
	    System.out.println("e aj:"+packages);
	    //iterate through packages
	    Iterator package_itr = packages.iterator();
	    while(package_itr.hasNext()){
		  org.eclipse.uml2.uml.Package package1 = (org.eclipse.uml2.uml.Package)package_itr.next();
		 
		 java.util.Collection<org.eclipse.uml2.uml.internal.impl.ClassImpl> classes = EcoreUtil.getObjectsByType(package1.eContents(), UMLPackage.Literals.CLASS);
		 Iterator classes_iterator = classes.iterator();
		 
		    while(classes_iterator.hasNext()){
		      org.eclipse.uml2.uml.internal.impl.ClassImpl class1 = (org.eclipse.uml2.uml.internal.impl.ClassImpl)classes_iterator.next();
		       //BEGIN_getting the classes and their info
		      if(class1.getClass().equals(org.eclipse.uml2.uml.internal.impl.ClassImpl.class)){
		      System.out.println("CLASS==============");
		      System.out.println("class name:"+class1.getName());
		      EList<Operation> operations = ((org.eclipse.uml2.uml.internal.impl.ClassImpl)class1).getOwnedOperations(); 
		      Iterator operations_iter = operations.iterator();
		      while (operations_iter.hasNext()) {
			Operation operation1 = (Operation) operations_iter.next();
			System.out.println("Name operation:"+operation1.getName() + " ");
			Parameter returnParam = operation1.getReturnResult();
			if (returnParam!=null) System.out.println("Return:" + returnParam.getName()+":"+returnParam.getType().getName());
			org.eclipse.emf.common.util.EList<Parameter> parameters = operation1.getOwnedParameters();
			Iterator it_parameters = parameters.iterator();
			  while(it_parameters.hasNext()){
			    Parameter param = (Parameter) it_parameters.next();
			    System.out.println("Name parameter:"+param.getName() + ":" + param.getType().getName());
			  }
		      }
		      System.out.println("==============");
		      } //END_getting the classes and their info
		      
		       //BEGIN_getting the interactions and their info
		      else if(class1.getClass().equals(org.eclipse.uml2.uml.internal.impl.InteractionImpl.class)){
		      System.out.println("INTERACTION==============");
			  System.out.println("interaction name:"+class1.getName());
			  //BEGIN_getting the interaction owned_attributes and their info
			  System.out.println("ATTRIBUTE==============");
			  org.eclipse.emf.common.util.EList<Property> owned_attributes = class1.getOwnedAttributes();
			  Iterator owned_attributes_iterator = owned_attributes.iterator();
			    while(owned_attributes_iterator.hasNext()){
			      org.eclipse.uml2.uml.Property attrib1 = (org.eclipse.uml2.uml.Property)owned_attributes_iterator.next();
			      System.out.println("name:"+attrib1.getName()+" type:"+attrib1.getType().getName());
			      
			    }
			    //END_getting the interaction owned_attributes and their info
			    
			    //BEGIN_getting the interaction lifelines and their info
			      org.eclipse.emf.common.util.EList<Lifeline> lifelines = ((org.eclipse.uml2.uml.internal.impl.InteractionImpl)class1).getLifelines();
			      Iterator lifelines_iterator = lifelines.iterator();
			    while(lifelines_iterator.hasNext()){
			      org.eclipse.uml2.uml.Lifeline lifeline = (org.eclipse.uml2.uml.Lifeline)lifelines_iterator.next();
			      System.out.println("LIFELINE==============");
			      System.out.println("name:"+lifeline.getName()+" type:"+lifeline.getRepresents().getName());
			      System.out.println("covered by:"+lifeline.getCoveredBys());
			    }
			    //END_getting the interaction lifelines and their info
			    System.out.println("==============");
			    
			    //BEGIN_getting the interaction fragments and their info
			    org.eclipse.emf.common.util.EList<InteractionFragment> fragments = ((org.eclipse.uml2.uml.internal.impl.InteractionImpl)class1).getFragments(); 
			    Iterator fragments_iterator = fragments.iterator();
			    while(fragments_iterator.hasNext()){
			      org.eclipse.uml2.uml.InteractionFragment fragment = (org.eclipse.uml2.uml.InteractionFragment)fragments_iterator.next();
			      System.out.println("INTERACTION FRAGMENT========");
// 			      System.out.println("name:"+fragment.getName());
			       org.eclipse.emf.common.util.EList<Lifeline> covereds =  fragment.getCovereds();			      
			       Iterator covereds_iterator = covereds.iterator();
			       while(covereds_iterator.hasNext()){
				  org.eclipse.uml2.uml.Lifeline lifeline1 = (org.eclipse.uml2.uml.Lifeline)covereds_iterator.next();
// 				  System.out.println("FRAGMENT COVERS LIFELINE==============");
				  System.out.println("Fragment covers lifeline: name:"+lifeline1.getName()+" type:"+lifeline1.getRepresents().getName());
// 				  System.out.println("covered by:"+lifeline1.getCoveredBys());
			       }
			       if(fragment.getClass().equals(org.eclipse.uml2.uml.internal.impl.BehaviorExecutionSpecificationImpl.class)){
				System.out.println("BehaviorExecutionSpecification");
				System.out.println("start:"+((org.eclipse.uml2.uml.internal.impl.BehaviorExecutionSpecificationImpl)fragment).getStart());
				System.out.println("finish:"+((org.eclipse.uml2.uml.internal.impl.BehaviorExecutionSpecificationImpl)fragment).getFinish());
			       } else if(fragment.getClass().equals(org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl.class)){
				  System.out.println("MessageOccurrenceSpecificationImpl");
				  System.out.println("message:"+((org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl)fragment).getMessage().getName());
				  System.out.println("message sort:"+ ((org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl)fragment).getMessage().getMessageSort());
// 				  System.out.println("event:"+((org.eclipse.uml2.uml.internal.impl.MessageOccurrenceSpecificationImpl)fragment).getEvent());
// 				  System.out.println("event:"+((org.eclipse.uml2.uml.OccurrenceSpecification)fragment).getEvent().toString());
				} else if(fragment.getClass().equals(org.eclipse.uml2.uml.internal.impl.ExecutionOccurrenceSpecificationImpl.class)) {
				  System.out.println("ExecutionOccurrenceSpecificationImpl");
				  System.out.println("execution:"+((org.eclipse.uml2.uml.internal.impl.ExecutionOccurrenceSpecificationImpl)fragment).getExecution());
				  System.out.println("enclosing interaction:"+((org.eclipse.uml2.uml.internal.impl.ExecutionOccurrenceSpecificationImpl)fragment).getEnclosingInteraction().getName());
// 				  getEnclosingInteraction()
				} else if(fragment.getClass().equals(org.eclipse.uml2.uml.internal.impl.CombinedFragmentImpl.class)) {
				    System.out.println("CombinedFragment");
				   InteractionOperatorKind operator = ((org.eclipse.uml2.uml.internal.impl.CombinedFragmentImpl)fragment).getInteractionOperator(); 
				   System.out.println("operator:"+ operator.getLiteral());
				   org.eclipse.emf.common.util.EList<InteractionOperand> operands = ((org.eclipse.uml2.uml.internal.impl.CombinedFragmentImpl)fragment).getOperands();
// 				   InteractionConstraint guard = operandsgetGuard()
				   Iterator operands_iterator = operands.iterator();
				   while(operands_iterator.hasNext()){
				    InteractionOperand operand = (org.eclipse.uml2.uml.InteractionOperand)operands_iterator.next();
				    System.out.println("operand guard:"+ operand.getGuard().getSpecification().stringValue()); 
				    
				    org.eclipse.emf.common.util.EList<InteractionFragment> innerFragments = operand.getFragments();
				    
				    
				   }
				   
				   
				   
				   
				}
				 else if(fragment.getClass().equals(org.eclipse.uml2.uml.internal.impl.InteractionUseImpl.class)) {
				    System.out.println("InteractionUse");
				   Interaction refers_to = ((org.eclipse.uml2.uml.internal.impl.InteractionUseImpl)fragment).getRefersTo(); 
				   System.out.println("refers to:"+ refers_to.getName());
				}
				
				
				System.out.println("==============");
			       
			    }
			    //END_getting the interaction fragments and their info
			   
			   org.eclipse.emf.common.util.EList<Message>	interactionMessages = ((org.eclipse.uml2.uml.internal.impl.InteractionImpl)class1).getMessages(); 
			   Iterator interactionMessages_iterator = interactionMessages.iterator();
			   //BEGIN_getting the iteraction messages and their info
			   while(interactionMessages_iterator.hasNext()){
			    org.eclipse.uml2.uml.Message message = (org.eclipse.uml2.uml.Message) interactionMessages_iterator.next();
			    System.out.println("interaction message:"+ message.getName()+" sendEvent="+message.getSendEvent()+" receiveEvent="+message.getReceiveEvent()+" sort:"+message.getMessageSort());
			   }
		      }//END_getting the interactions and their info
		      

// 		      InteractionUse?
		    }
		    
		 
	 
	    }
	    
	  }
      }*/
      
    
}
}