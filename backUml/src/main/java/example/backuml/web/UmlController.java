package example.backuml.web;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

@RestController
@RequestMapping("/api/uml")
public class UmlController {

    @PostMapping("/generate-svg")
    public ResponseEntity<String> generateSequenceDiagram(@RequestParam("file") MultipartFile file) {
        try {
            String xmlContent = new String(file.getBytes());
            if (!isValidXml(xmlContent)) {
                return ResponseEntity.badRequest().body("Fichier XML invalide.");
            }

            String svgContent = applyXsltTransformation(xmlContent);
            return ResponseEntity.ok(svgContent); // Retourne le SVG en tant que texte
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors du traitement du fichier.");
        }
    }

    private boolean isValidXml(String xmlContent) {
        // Charger et valider le fichier XML selon un XML Schema (XSD)
        // Exemple de validation basique avec XMLUnit
        try {
            InputSource xmlInputSource = new InputSource(new StringReader(xmlContent));
            Validator validator = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(new File("uml-to-svg.xsl"))
                    .newValidator();
            validator.validate(new StreamSource(String.valueOf(xmlInputSource)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private String applyXsltTransformation(String xmlContent) throws Exception {
        InputStream xsltInputStream = getClass().getResourceAsStream("uml-to-svg.xsl");
        InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        StreamSource xslt = new StreamSource(xsltInputStream);
        StreamSource xml = new StreamSource(xmlInputStream);
        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);

        transformerFactory.newTransformer(xslt).transform(xml, result);

        return stringWriter.toString(); // Le SVG généré
    }
}
