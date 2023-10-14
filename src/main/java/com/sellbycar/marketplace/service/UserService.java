package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.repository.model.User;

public interface UserService {

    public User getUser(long id);

    public User updateUser(User user);

    public User createUser(User user);

    public boolean isEmailAlreadyExists(String email);

    public boolean deleteUser(long id);
}
