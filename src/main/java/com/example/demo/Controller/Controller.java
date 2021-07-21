package com.example.demo.Controller;

import com.example.demo.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
    public String UploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("time") String time, Model model) throws IOException {
        List<String> errorMsg = new ArrayList<>();
//        System.out.println("測試時間-----------------------------"+time);
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String extension = fileName.substring(fileName.lastIndexOf(".")+1);
            if (extension.equals("csv")) {
                service.init(time);
                service.UploadFile(multipartFile,time);
                service.csvXml(service.UploadFile(multipartFile,time),time);
                service.csvJson(service.UploadFile(multipartFile,time),time);
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

        model.addAttribute("time",time);

        return "redirect:/ListFile/"+time;
    }

    @RequestMapping(value = "/DownloadFile/{time}/{filename}", method = RequestMethod.GET)
    public ResponseEntity<StreamingResponseBody> DownloadFile(@PathVariable String filename,@PathVariable("time") String time) throws FileNotFoundException, UnsupportedEncodingException {
        StreamingResponseBody body = service.DownloadFile(filename,time);
        filename = new String(filename.getBytes(StandardCharsets.UTF_8));
        filename = URLEncoder.encode(filename,"UTF-8");
        String noExtension = filename.substring(0, filename.lastIndexOf("."));
        String extension = filename.substring(filename.lastIndexOf(".")+1);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename="+noExtension+"."+extension)
                .body(body);
    }

    @RequestMapping(value = "/ListFile/{time}", method = RequestMethod.GET)
    public String ListFile(@PathVariable("time") String time, Model model) {
        model.addAttribute("time", time);
        List<String>  list = service.listFile(time);
        model.addAttribute("list", list);
        return "ListFile";
    }

}