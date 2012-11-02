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
    
    Resource resource
    = new XMIResourceImpl(URI.createFileURI("/home/daniela/sw/IBM/git/UML-MARTE/exportTest/Default.sbs.xmi"));
//         = new XMIResourceImpl(URI.createFileURI("/home/daniela/sw/IBM/git/UML-MARTE/exportTest/TestProject.rpy.xmi"));
    resource.load(null);
      System.out.println( resource.getContents().get(0) );
// ResourceSet resSet = new ResourceSetImpl();
// Resource resource = resSet.getResource(URI
//         .createURI("website/My.website"), true);
  }
}