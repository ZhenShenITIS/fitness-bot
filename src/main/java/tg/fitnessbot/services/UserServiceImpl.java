package tg.fitnessbot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.models.User;
import tg.fitnessbot.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void updateUser(UserForm userForm) {
        User user = User
                .builder()
                .id(userForm.getId())
                .birthday(userForm.getBirthday())
                .weight(userForm.getWeight())
                .height(userForm.getHeight())
                .gender(userForm.getGender())
                .firstName(userForm.getFirstName())
                .lastName(userForm.getLastName())
                .username(userForm.getUsername())
                .lifeActivity(userForm.getLifeActivity())
                .build();
        userRepository.save(user);
    }

    @Override
    public UserForm getUserByID(Long id) {
        User user = userRepository.findById(id).orElse(null);
        UserForm userForm = null;
        if (user != null) {
            userForm = UserForm
                    .builder()
                    .id(user.getId())
                    .birthday(user.getBirthday())
                    .gender(user.getGender())
                    .height(user.getHeight())
                    .weight(user.getWeight())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .lifeActivity(user.getLifeActivity())
                    .build();
        }
        return userForm;
    }
}
