package com.email.controller;

import com.email.model.EmailRequest;
import com.email.service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin("*")
public class EmailGeneratorController {
    @Autowired
    private EmailGeneratorService emailGeneratorService;

    @PostMapping("/generate")
   public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){

       String response = emailGeneratorService.generateEmailReply(emailRequest);
       return new ResponseEntity<>(response, HttpStatus.OK);

   }
}
