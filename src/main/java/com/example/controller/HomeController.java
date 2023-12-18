package com.example.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/csv")
public class HomeController {

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadCsv() {
        // Create sample data
        List<String> header = Arrays.asList("Name", "Age", "City");
        List<List<String>> data = Arrays.asList(
                Arrays.asList("John Doe", "25", "New York"),
                Arrays.asList("Jane Smith", "30", "San Francisco")
        );

        // Generate CSV content
        StringWriter writer = new StringWriter();
        writer.append(String.join(",", header)).append("\n");
        for (List<String> row : data) {
            writer.append(String.join(",", row)).append("\n");
        }

        // Convert to byte array
        byte[] csvBytes = writer.toString().getBytes();

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "data.csv");

        // Return the CSV file as a ResponseEntity
        return new ResponseEntity<>(csvBytes, headers, 200);
    }
    @GetMapping("/hello")
    public String heolloWorld() {
        return "hello-world";
    }

}
