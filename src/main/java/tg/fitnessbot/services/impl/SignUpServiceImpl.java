package tg.fitnessbot.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.models.User;
import tg.fitnessbot.repositories.UserRepository;
import tg.fitnessbot.services.SignUpService;


@Service
public class SignUpServiceImpl implements SignUpService {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean signUp(UserForm form) {
        User exUser = userRepository.findById(form.getId()).orElse(null);
        if (exUser == null) {
            User user = User
                    .builder()
                    .firstName(form.getFirstName())
                    .lastName(form.getLastName())
                    .username(form.getUsername())
                    .id(form.getId())
                    .build();
            userRepository.save(user);
            return true;
        }
        return false;

    }
}
