package com.email.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailRequest {

    private String emailContent;
    private String tone;
    private String platform;

}
