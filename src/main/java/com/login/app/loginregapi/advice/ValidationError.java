package com.login.app.loginregapi.advice;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationError {
    private List<String> errors = new ArrayList<>();
    private String uri;

    public void addError(String error) {
        this.errors.add(error);
    }

}
