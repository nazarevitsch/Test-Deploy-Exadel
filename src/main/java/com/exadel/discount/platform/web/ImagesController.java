package com.exadel.discount.platform.web;

import com.exadel.discount.platform.domain.Message;
import com.exadel.discount.platform.service.AmazonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload_image")
@Slf4j
public class ImagesController {
    @Autowired
    private AmazonService amazonService;

    @PostMapping(value = "/{name}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Message> upload(@PathVariable String name, @RequestParam MultipartFile file) throws Exception{
        String[] parts = file.getOriginalFilename().split("\\.");
        String link = amazonService.uploadFile(file.getName(), parts[parts.length - 1], file.getInputStream());
        log.info(link);
        return new ResponseEntity<Message>(new Message(link), HttpStatus.OK);
    }
}
