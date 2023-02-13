package com.codevumc.codev_backend.domain.firebase;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FCM {
    private String title;
    private String body;
    private List<String> co_emails;
}
