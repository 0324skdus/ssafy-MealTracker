package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.UserDao;
import com.ssafy.yamyam.dto.User;
import com.ssafy.yamyam.dto.UserProfileUpdateRequest;
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

    //임시비번 발급전 리셋
    @Override
    public String resetPassword(String userId, String name, String email) {
        User user = userDao.findUserByAllInfo(userId, name, email);
        if (user == null) return null;

        String tempPw = generateTempPassword(10);
        userDao.updatePassword(userId, tempPw);
        return tempPw;
    }
//임시비번 발급
    private String generateTempPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
    //회원정보 수정
    @Override
    public void updateProfile(UserProfileUpdateRequest req) {
        String userId = req.getUserId();
        if (req.getName() != null) {
            userDao.updateName(userId, req.getName());
        }
        if (req.getEmail() != null) {
            userDao.updateEmail(userId, req.getEmail());
        }
        if (req.getPassword() != null) {
            userDao.updatePassword(userId, req.getPassword());
        }
        if (req.getNewUserId() != null) {
            userDao.updateUserId(userId, req.getNewUserId());
        }
    }

    @Override
    public boolean isUserIdExists(String userId) {
        return userDao.countByUserId(userId) > 0;
    }


}
