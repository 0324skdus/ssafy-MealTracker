package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.User;
import java.util.List;

public interface UserService {

    User getUserById(String userId);

    List<User> getAllUsers();

    boolean register(User user);

    boolean modify(User user);

    boolean remove(String userId);

    User getUserInfo(String userId);

    User login(String userId, String userPassword);

    public double calculateRecommendedCalories(User user);
}
