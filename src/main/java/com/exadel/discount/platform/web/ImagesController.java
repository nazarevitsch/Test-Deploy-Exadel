package com.exadel.discount.platform.web;

import com.exadel.discount.platform.domain.Message;
import com.exadel.discount.platform.service.AmazonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class ImagesController {
    @Autowired
    private AmazonService amazonService;

    @PostMapping(value = "/upload_image",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Message> upload(@RequestParam MultipartFile file){
        try {
            String link = amazonService.uploadFile(file);
            log.info(link);
            return new ResponseEntity<Message>(new Message(link), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Message>(new Message("Image Storage is temporarily unavailable!"), HttpStatus.NOT_FOUND);
        }
    }
}
