package com.example.controller;

import com.example.service.CsvReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/csv")
public class HomeController {

    @Autowired
    private CsvReaderService csvService;

    /**
     * return the response as csv
     * @return
     */
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

    /**
     * return csv content as joson format
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
        }

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            // Process CSV data (replace this with your logic)
            List<String[]> csvData = csvService.readCsvFile(reader);
            // You can do something with the CSV data here
            csvData.stream().forEach(data -> {
                for(String e:data){
                    System.out.print(e);
                }
                System.out.println("---");
            });
            return new ResponseEntity<>("File uploaded and processed successfully.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return new ResponseEntity<>("Failed to process the uploaded file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hello")
    public String heolloWorld() {
        return "hello-world";
    }

}
