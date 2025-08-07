package com.email.service;

import com.email.model.EmailRequest;

public interface EmailGeneratorService {

    String generateEmailReply(EmailRequest emailRequest);

}
