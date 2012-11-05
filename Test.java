
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
class Test {
  public static void main(String args[]) throws Exception{
    System.out.println("Success!!!");
 
    System.out.println("AMAAAN");
EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
  ResourceSet resourceSet = new ResourceSetImpl();
  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());

		
  resourceSet.getPackageRegistry().put("http://schema.omg.org/spec/UML/2.1", UMLPackage.eINSTANCE);

  Resource resource = null;
      File f = new File("ExportedProject.uml"); 


    URI uri = URI.createFileURI(f.getAbsolutePath());
        resource = resourceSet.getResource(uri, true);

    resource.load(null);

      java.util.Collection<Model> m =  EcoreUtil.getObjectsByType(resource.getContents(), UMLPackage.Literals.MODEL);
      Iterator model_iterator = m.iterator();
      while(model_iterator.hasNext()){
	Model model1 = (Model)model_iterator.next();
	  if (model1.getName().equals("TestProject")){
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
			  //BEGIN_getting the owned_attributes and their info
			  System.out.println("ATTRIBUTE==============");
			  org.eclipse.emf.common.util.EList<Property> owned_attributes = class1.getOwnedAttributes();
			  Iterator owned_attributes_iterator = owned_attributes.iterator();
			    while(owned_attributes_iterator.hasNext()){
			      org.eclipse.uml2.uml.Property attrib1 = (org.eclipse.uml2.uml.Property)owned_attributes_iterator.next();
			      System.out.println("name:"+attrib1.getName()+" type:"+attrib1.getType().getName());
			      
			    }
			    //END_getting the owned_attributes and their info
			    
			    //BEGIN_getting the lifelines and their info
			      org.eclipse.emf.common.util.EList<Lifeline> lifelines = ((org.eclipse.uml2.uml.internal.impl.InteractionImpl)class1).getLifelines();
			      Iterator lifelines_iterator = lifelines.iterator();
			    while(lifelines_iterator.hasNext()){
			      org.eclipse.uml2.uml.Lifeline lifeline = (org.eclipse.uml2.uml.Lifeline)lifelines_iterator.next();
			      System.out.println("LIFELINE==============");
			      System.out.println("name:"+lifeline.getName()+" type:"+lifeline.getRepresents().getName());
			      System.out.println("covered by:"+lifeline.getCoveredBys());
			    }
			    //END_getting the lifelines and their info
			    System.out.println("==============");
		      }//END_getting the interactions and their info
		      

		    }
		    
		 
	 
	    }
	    
	  }
      }
      
    
//        java.util.Collection<org.eclipse.uml2.uml.internal.impl.ClassImpl> classes =  EcoreUtil.getObjectsByType(resource.getContents(), UMLPackage.Literals.CLASS);
//     System.out.println("E aj:"+classes);
}
}