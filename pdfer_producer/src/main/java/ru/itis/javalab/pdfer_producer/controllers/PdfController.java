package ru.itis.javalab.pdfer_producer.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.javalab.pdfer_producer.entities.Form;

import java.io.File;

@RequiredArgsConstructor
@RestController
@RequestMapping("/form")
public class PdfController {

    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    private static final String KEY3 = "key3";
    private static final String DEFKEY = "def";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<File> sendForm(@RequestBody Form form) {
        String currentRouting = form.getDocType().toString().toLowerCase();
        switch (currentRouting) {
            case "diploma":
                currentRouting = KEY1;
                break;
            case "degree":
                currentRouting = KEY2;
                break;
            case "certificate":
                currentRouting = KEY3;
                break;
            default:
                currentRouting = DEFKEY;
                break;
        }
        String pathToFile = String.valueOf(rabbitTemplate.sendAndReceive("exchange", currentRouting, new Message(gson.toJson(form).getBytes())));
        return ResponseEntity.ok(new File(pathToFile));
    }
}
