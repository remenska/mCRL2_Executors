// import com.sdmetrics.util.XMLParser;
// import org.xml.sax.ContentHandler;
// import org.xml.sax.*;
// import org.xml.sax.helpers.DefaultHandler;
// import com.sdmetrics.util.SAXHandler;
// import com.sdmetrics.model.XMIReader;
// import com.sdmetrics.model.Model;
// import com.sdmetrics.model.MetaModel;
// import com.sdmetrics.model.XMITransformations;
// import com.sdmetrics.*;
import org.eclipse.emf.ecore.xmi.*;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
// import java.net.URI;
// import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.URI;
// import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
// imoprt org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.*;
// import org.eclipse.uml2.uml.UMLPackage;
import java.io.*;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
class Test {
  public static void main(String args[]) throws Exception{
    System.out.println("Success!!!");
 
//     MetaModel mmodel = new MetaModel();
//     Model model = new Model(mmodel);
//     XMITransformations transf = new XMITransformations(mmodel);
//     XMIReader reader = new XMIReader(transf,model);
//     org.xml.sax.helpers.DefaultHandler handler = transf.getSAXParserHandler(); 
//    XMLParser parser =  new XMLParser();
//     parser.parse("/home/daniela/sw/IBM/git/UML-MARTE/exportTest/Default.sbs.xmi", handler);
//     System.out.println("Num:" + reader.getNumberOfElements());
    
EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
  ResourceSet resourceSet = new ResourceSetImpl();
  resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
// resourceSet.getPackageRegistry().put("http://www.eclipse.org/uml2/2.0.0/UML", UMLPackage.eINSTANCE);
  resourceSet.getPackageRegistry().put("http://schema.omg.org/spec/UML/2.1", UMLPackage.eINSTANCE);
  resourceSet.getPackageRegistry().put("http://www.eclipse.org/uml2/2.0.0/UML", UMLPackage.eINSTANCE);

  Resource resource = null;
//       File f = new File("/home/daniela/Documents/mCRL2_new_models/September2012/mCRL2_Executors/ExportHere.xmi"); 
      File f = new File("/home/daniela/sw/IBM/git/exportNew/Default.sbs.xmi.copy.xmi"); 


    URI uri = URI.createFileURI(f.getAbsolutePath());
        resource = resourceSet.getResource(uri, true);

//     Resource resource
//     = new XMIResourceImpl(URI.createFileURI("/home/daniela/Documents/mCRL2_new_models/September2012/mCRL2_Executors/ExportHere.xmi"));
//         = new XMIResourceImpl(URI.createFileURI("/home/daniela/sw/IBM/git/UML-MARTE/exportTest/TestProject.rpy.xmi"));
    resource.load(null);
      System.out.println( resource.getContents().get(0) );
// ResourceSet resSet = new ResourceSetImpl();
// Resource resource = resSet.getResource(URI
//         .createURI("website/My.website"), true);
  }
}