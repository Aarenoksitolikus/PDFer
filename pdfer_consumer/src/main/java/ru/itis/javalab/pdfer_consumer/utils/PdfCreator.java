package ru.itis.javalab.pdfer_consumer.utils;

import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.itis.javalab.pdfer_consumer.entities.Form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class PdfCreator {
    @RabbitListener(queues = "#{queue.name}", containerFactory = "containerFactory")
    public String createPdfOnMessage(Message message) throws IOException, DocumentException {
        Form form = new Gson().fromJson(new String(message.getBody()), Form.class);
        String type;
        String word;
        Form.DocType docType = form.getDocType();
        String firstName = form.getFirstName();
        String lastName = form.getLastName();
        switch (docType) {
            case DEGREE:
                type = "DEGREE";
                word = "is awarded to ";
                break;
            case DIPLOMA:
                type = "DIPLOMA";
                word = "is given to ";
                break;
            case CERTIFICATE:
                type = "CERTIFICATE";
                word = "confirms qualification of ";
                break;
            default:
                type = "DOC";
                word = "belongs to ";
                break;
        }
        File file = new File("C:/Users/Akvelon/IdeaProjects/education/Java/pdfer/docs/" + type + "-" + UUID.randomUUID() + ".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.COURIER, 18, BaseColor.BLACK);
        Paragraph header = new Paragraph(type, headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Font credentialsFont = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.RED);
        Paragraph credentials = new Paragraph(word + firstName + " " + lastName, credentialsFont);
        credentials.setAlignment(Element.ALIGN_CENTER);
        document.add(credentials);

        Image img = Image.getInstance("src/main/resources/static/images/logo.jpg");
        img.setAlignment(Element.ALIGN_CENTER);
        Paragraph image = new Paragraph();
        image.add(img);
        document.add(image);

        document.close();

        System.out.println("received");
        return file.getPath();
    }
}
