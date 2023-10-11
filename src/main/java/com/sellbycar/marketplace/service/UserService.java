package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.model.user.User;

public interface UserService {

    public User getUser(long id);

    public User updateUser(User user);

    public User createUser(User user);

    public boolean deleteUser(long id);
}
