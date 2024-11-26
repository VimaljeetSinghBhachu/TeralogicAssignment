package com.terralogic.assignment.model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.Data;

@Data
public class User {
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private boolean passwordResetRequired;
    private String email;
    private String contact;
    private String expirationDate;
    private String description;
    private int timeout;
    private String dateTimeFormat;
    private String roleName;
    private String scopeName;
    private String primaryGroupName;
    private String secondaryGroupName;
    private String language;
    private String organization;
    private String timezone;
    private String memo;
}
