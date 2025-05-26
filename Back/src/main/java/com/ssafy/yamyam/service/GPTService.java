package com.ssafy.yamyam.service;

import java.time.LocalDate;

public interface GPTService {
    String getFeedback(String userId, LocalDate date);
}
