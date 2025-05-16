package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.UserDao;
import com.ssafy.yamyam.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return userDao.insert(user) > 0;
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

    @Override
    public double calculateRecommendedCalories(User user) {
        double bmr;
        if (user.getGender().equals("M")) {
            bmr = 66 + (13.7 * user.getWeight()) + (5 * user.getHeight()) - (6.8 * user.getAge());
        } else {
            bmr = 655 + (9.6 * user.getWeight()) + (1.8 * user.getHeight()) - (4.7 * user.getAge());
        }

        //bmr -> 기초대사량

        double multiplier = switch (user.getActivityLevel()) {
            case "L" -> 1.2;
            case "M" -> 1.55;
            case "H" -> 1.9;
            default -> 1.0; // 어차피 쓰일일 없긴한데 일단.
        }; // 활동지수

        return bmr * multiplier; // 일일 권장 칼로리
    }
}
