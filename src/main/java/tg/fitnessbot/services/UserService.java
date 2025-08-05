package tg.fitnessbot.services;

import tg.fitnessbot.dto.UserForm;

public interface UserService {
    void updateUser (UserForm userForm);

    UserForm getUserByID (Long id);
}
