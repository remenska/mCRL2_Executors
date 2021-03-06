
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
import org.eclipse.uml2.uml.internal.impl.ModelImpl;
import  org.eclipse.uml2.uml.*;
import org.eclipse.emf.ecore.impl.*;

class Process {
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


public class Test {
  public static void main(String args[]) throws Exception{
  
      EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
      resourceSet.getPackageRegistry().put("http://schema.omg.org/spec/UML/2.1", UMLPackage.eINSTANCE);

      Resource resource = null;
// 	File f = new File("ExportedProject.uml"); 
	File f = new File("/home/daniela/sw/IBM/git/UML-MARTE/OKThanksBye/ExportedtSmall1.uml"); 

      URI uri = URI.createFileURI(f.getAbsolutePath());
      resource = resourceSet.getResource(uri, true);

      resource.load(null);

      java.util.Collection<Model> m =  EcoreUtil.getObjectsByType(resource.getContents(), UMLPackage.Literals.MODEL);
      
      Iterator model_iterator = m.iterator();
      
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
      }
      
    
}
}