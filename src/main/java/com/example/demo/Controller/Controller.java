package com.example.demo.Controller;

import com.example.demo.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller

public class Controller {

    @Autowired
    private Service service;

    @GetMapping("/HomePage")
    public String index() {
        return "HomePage";
    }

    @PostMapping("/UploadFile")
    public String UploadFile(@RequestParam("file") MultipartFile multipartFile, Model model) throws IOException {
        List<String> errorMsg = new ArrayList<>();
        if(!multipartFile.isEmpty()) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        if (extension.equals("csv")) {

            service.UploadFile(multipartFile);
            service.transferFile(service.UploadFile(multipartFile));
            service.csvJson(service.UploadFile(multipartFile));
            service.DeleteFile(multipartFile);
        }
        else{
            errorMsg.add("請上傳.csv檔");
            model.addAttribute("errorMsg", errorMsg);
            return "HomePage";
        }

        }else{
            errorMsg.add("請選擇檔案");
            model.addAttribute("errorMsg", errorMsg);
            return "HomePage";
        }

        return "redirect:/listFile";
    }

    @GetMapping("/DownloadFile/{filename}")
    public ResponseEntity<StreamingResponseBody> DownloadFile(@PathVariable String filename) throws FileNotFoundException, UnsupportedEncodingException {
        StreamingResponseBody body = service.DownloadFile(filename);
        filename = new String(filename.getBytes(StandardCharsets.UTF_8));
        filename = URLEncoder.encode(filename,"UTF-8");
        String noExtension = filename.substring(0, filename.lastIndexOf("."));
        String extension = filename.substring(filename.lastIndexOf(".")+1);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename="+noExtension+"."+extension)
                .body(body);
    }

    @GetMapping("/listFile")
    public String ListFile(Model model) {
        List<String>  list = service.listFile();
        model.addAttribute("list", list);
        return "listFile";
    }


//    public static void main(String[] args) throws IOException {
//        File input = new File("D:\\桌面\\local\\addresses.csv");
//        File output = new File("D:\\桌面\\local\\output.xml");
//
//        CsvMapper csvMapper = new CsvMapper();
//        CsvSchema csvSchema = csvMapper
//                .typedSchemaFor(Vo.class)
//                .withHeader()
//                .withComments();
//        MappingIterator<Vo> mappingIterator= csvMapper
//                .readerWithSchemaFor(Vo.class)
//                .with(csvSchema)
//                .readValues(input);
//
//        List<Vo> data= mappingIterator.readAll();
//        XmlMapper mapper = new XmlMapper();
//        mapper.writerWithDefaultPrettyPrinter().writeValue(output, data);
//        data.forEach(System.out::println);
//    }

//    public static void main(String[] args) throws IOException {
//        Path filepath = Paths.get("D:\\democsv\\upload");
//        Resource resource = new UrlResource(filepath.toUri());
//        System.out.println(filepath);
//        System.out.println(resource);
//
//        Stream<Path> pathStream = Files.walk(filepath).map(filepath::relativize);
//        pathStream.map(Path::toString).filter(f->f.endsWith(".xml")).forEach(System.out::println);
//    }

    public static void main(String[]args){
        List<String> filenames = new ArrayList<>();
        File file = new File("D:\\democsv\\upload\\");
        File[] files = file.listFiles();
        assert files != null;
        for (File f:files) {
            System.out.println(f.getName());
            filenames.add(f.getName());
        }
    }

}


