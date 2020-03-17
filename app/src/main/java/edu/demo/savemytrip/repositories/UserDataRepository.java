package edu.demo.savemytrip.repositories;

import androidx.lifecycle.LiveData;

import edu.demo.savemytrip.database.dao.UserDao;
import edu.demo.savemytrip.models.User;

public class UserDataRepository {

    private final UserDao userDao;

    public UserDataRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    // --- GET USER ---
    public LiveData<User> getUser(long userId){
        return userDao.getUser(userId);
    }
}
