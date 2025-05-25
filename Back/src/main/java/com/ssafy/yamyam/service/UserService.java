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
    //아이디/비번찾기
    String findUserId(String name, String email);
    String resetPassword(String userId, String name, String email);


    boolean isUserIdExists(String userId);
}
