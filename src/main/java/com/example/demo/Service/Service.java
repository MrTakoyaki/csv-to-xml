package com.example.demo.Service;

import com.example.demo.Model.Document;
import com.example.demo.Model.DataSet;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    Path uploadPath = Paths.get("upload");
    Path downloadPath = Paths.get("download");

    Logger logger = Logger.getLogger(Service.class);

    public File UploadFile(MultipartFile multipartFile, String time) throws IOException {
        Path uploadPath = Paths.get(this.uploadPath + "//" + time);
        try {
            Files.copy(multipartFile.getInputStream(), uploadPath.resolve(multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        }
        return new File(uploadPath + "/" + multipartFile.getOriginalFilename());

    }

    public JsonMapper csvJson(File input, String time) throws IOException {
        Path downloadPath = Paths.get(this.downloadPath + "/" + time);
        String fileName = input.getName();
        String noExtension = fileName.substring(0, fileName.lastIndexOf("."));
        File output = new File(downloadPath + "/" + noExtension + ".json");
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper
                .typedSchemaFor(DataSet.class)
                .withHeader()
                .withComments();
        logger.info(csvMapper
                .typedSchemaFor(DataSet.class)
                .withHeader()
                .withComments());
        MappingIterator<DataSet> mappingIterator = csvMapper
                .readerWithSchemaFor(DataSet.class)
                .with(csvSchema)
                .readValues(input);

        List<DataSet> data = mappingIterator.readAll();

        JsonMapper mapper = new JsonMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(output, data);
        return mapper;
    }

    public XmlMapper csvXml(File input, String time) throws IOException {
        Path downloadPath = Paths.get(this.downloadPath + "/" + time);
        String fileName = input.getName();
        String noExtension = fileName.substring(0, fileName.lastIndexOf("."));
        File output = new File(downloadPath + "/" + noExtension + ".xml");
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper
                .typedSchemaFor(DataSet.class)
                .withHeader()
                .withComments();
        logger.info(csvMapper
                .typedSchemaFor(DataSet.class)
                .withHeader()
                .withComments());
        MappingIterator<DataSet> mappingIterator = csvMapper
                .readerWithSchemaFor(DataSet.class)
                .with(csvSchema)
                .readValues(input);

        List<DataSet> data = mappingIterator.readAll();

        Document document = new Document();
        document.sethOne("Document");
        document.setDescriptions("XML Format");
        document.setDefinition(noExtension);
        document.setDataSet(data);

        XmlMapper mapper = new XmlMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(output, document);
        return mapper;
    }

    public void init(String time) {


        Path uploadTimeDir = Paths.get(this.uploadPath + "/" + time);
        Path downloadTimeDir = Paths.get(this.downloadPath + "/" + time);
        if (!uploadPath.toFile().exists()) {
            uploadPath.toFile().mkdir();
        }
        if (!downloadPath.toFile().exists()) {
            downloadPath.toFile().mkdir();
        }

        try {
            Files.createDirectory(uploadTimeDir);
            Files.createDirectory(downloadTimeDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage");
        }
    }


    public StreamingResponseBody DownloadFile(String filename, String time) throws FileNotFoundException {
        Path downloadPath = Paths.get(this.downloadPath + "/" + time);
        InputStream inputStream = new FileInputStream(downloadPath + "//" + filename);
        return outputStream -> FileCopyUtils.copy(inputStream, outputStream);
    }

    public List<String> listFile(String time) {
        Path downloadPath = Paths.get(this.downloadPath + "/" + time);
        List<String> filenames = new ArrayList<>();
        File file = new File(downloadPath.toString());
        File[] files = file.listFiles();
        assert files != null;
        for (File f : files) {
            filenames.add(f.getName());
        }
        return filenames;
    }
}