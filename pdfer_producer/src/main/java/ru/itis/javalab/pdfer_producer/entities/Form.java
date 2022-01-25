package ru.itis.javalab.pdfer_producer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Form {

    private DocType docType;
    private String firstName;
    private String lastName;

    public enum DocType {
        DIPLOMA, DEGREE, CERTIFICATE
    }
}
