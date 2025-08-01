package tg.fitnessbot.services;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tg.fitnessbot.dto.UserForm;
import tg.fitnessbot.models.User;
import tg.fitnessbot.repositories.UserRepository;

import java.sql.ResultSet;

@Component
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
