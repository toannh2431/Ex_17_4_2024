import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {
	public static void main(String[] args) {
		final String INPUT_FILE = "student.xml";
	    final String OUTPUT_FILE = "kq.xml";

	        ExecutorService executor = Executors.newFixedThreadPool(3);
	        List<Student> students = readStudentsFromFile(INPUT_FILE);

	        for (Student student : students) {
	            AgeCalculator ageCalculator = new AgeCalculator(student);
	            executor.execute((Runnable) ageCalculator);

	            try {
	                executor.shutdown();
	                while (!executor.isTerminated()) {
	                }
	                String encodedAge = ((AgeCalculator) ageCalculator).getEncodedAge();

	                PrimeChecker primeChecker = new PrimeChecker(encodedAge);
	                primeChecker.run();

	                createResultXML(student, encodedAge, primeChecker.isPrime());
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    private static List<Student> readStudentsFromFile(String fileName) {
	        List<Student> students = new ArrayList<>();
	        try {
	            File file = new File(fileName);
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document doc = builder.parse(file);
	            doc.getDocumentElement().normalize();

	            NodeList nodeList = doc.getElementsByTagName("Student");
	            for (int i = 0; i < nodeList.getLength(); i++) {
	                Element element = (Element) nodeList.item(i);
	                String id = element.getAttribute("id");
	                String name = element.getElementsByTagName("name").item(0).getTextContent();
	                String address = element.getElementsByTagName("address").item(0).getTextContent();
	                String dateOfBirth = element.getElementsByTagName("dateOfBirth").item(0).getTextContent();
	                students.add(new Student(id, name, address, dateOfBirth));
	            }
	        } catch (ParserConfigurationException | SAXException | IOException e) {
	            e.printStackTrace();
	        }
	        return students;
	    }

	    private static void createResultXML(Student student, String encodedAge, boolean isPrime) {
	        try {
	            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	            Document doc = docBuilder.newDocument();

	            Element rootElement = doc.createElement("Students");
	            doc.appendChild(rootElement);

	            Element studentElement = doc.createElement("Student");
	            rootElement.appendChild(studentElement);

	            studentElement.setAttribute("id", student.getId());

	            Element nameElement = doc.createElement("name");
	            nameElement.appendChild(doc.createTextNode(student.getName()));
	            studentElement.appendChild(nameElement);

	            Element addressElement = doc.createElement("address");
	            addressElement.appendChild(doc.createTextNode(student.getAddress()));
	            studentElement.appendChild(addressElement);

	            Element dobElement = doc.createElement("dateOfBirth");
	            dobElement.appendChild(doc.createTextNode(student.getDateOfBirth()));
	            studentElement.appendChild(dobElement);

	            Element encodedAgeElement = doc.createElement("encodedAge");
	            encodedAgeElement.appendChild(doc.createTextNode(encodedAge));
	            studentElement.appendChild(encodedAgeElement);

	            Element isPrimeElement = doc.createElement("isPrime");
	            isPrimeElement.appendChild(doc.createTextNode(String.valueOf(isPrime)));
	            studentElement.appendChild(isPrimeElement);

	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource source = new DOMSource(doc);
	            StreamResult result = new StreamResult(new File(encodedAge));
	            transformer.transform(source, result);

	            System.out.println("Result XML file saved!");
	        } catch (ParserConfigurationException | TransformerException e) {
	            e.printStackTrace();
	        }
	    
	}
}
