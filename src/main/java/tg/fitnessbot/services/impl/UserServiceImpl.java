package tg.fitnessbot.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tg.fitnessbot.constants.Gender;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.models.User;
import tg.fitnessbot.repositories.UserRepository;
import tg.fitnessbot.services.UserService;
import tg.fitnessbot.utils.DateUtil;

@Service
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

    @Override
    public Double calculateTdee(UserForm user) {
        Double bmr;
        if (user.getGender().equals(Gender.MALE)) {
            // BMR = 10 × вес (кг) + 6.25 × рост (см) – 5 × возраст (г) + 5
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * DateUtil.getAge(user.getBirthday()) + 5;
        } else {
            // BMR = 10 × вес (кг) + 6.25 × рост (см) – 5 × возраст (г) – 161
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * DateUtil.getAge(user.getBirthday()) - 161;
        }
        return bmr * user.getLifeActivity().getRatio();
    }
}
