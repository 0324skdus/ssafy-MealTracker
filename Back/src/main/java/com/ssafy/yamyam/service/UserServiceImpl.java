package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.UserDao;
import com.ssafy.yamyam.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(String userId) {
        return userDao.selectById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.selectAll();
    }

    @Override
    public boolean register(User user) {
        try {
            System.out.println("[register] 입력된 User 객체: " + user);
            int result = userDao.insert(user);
            System.out.println("[register] insert 결과: " + result);
            return result > 0;
        } catch (Exception e) {
            System.out.println("[register] 예외 발생:");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modify(User user) {
        return userDao.update(user) > 0;
    }

    @Override
    public boolean remove(String userId) {
        return userDao.delete(userId) > 0;
    }

    @Override
    public User getUserInfo(String userId) {
        return userDao.selectById(userId);
    }

    @Override
    public User login(String userId, String userPassword) {
        return userDao.login(userId, userPassword);
    }
    //아이디/비번찾기
    @Override
    public String findUserId(String name, String email) {
        return userDao.findUserIdByNameAndEmail(name, email);
    }

    @Override
    public String resetPassword(String userId, String name, String email) {
        User user = userDao.findUserByAllInfo(userId, name, email);
        if (user == null) return null;

        String tempPw = generateTempPassword(10);
        userDao.updatePassword(userId, tempPw);
        return tempPw;
    }

    private String generateTempPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @Override
    public boolean isUserIdExists(String userId) {
        return userDao.countByUserId(userId) > 0;
    }


}
