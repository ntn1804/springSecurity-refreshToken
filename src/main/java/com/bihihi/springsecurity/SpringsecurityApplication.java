package com.bihihi.springsecurity;

import com.bihihi.springsecurity.model.Role;
import com.bihihi.springsecurity.model.User;
import com.bihihi.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringsecurityApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityApplication.class, args);
    }

    @Override
    public void run(String... args) {
        User adminAccount = userRepository.findByRole(Role.ADMIN);

        if (null == adminAccount) {
            User user = new User();

            user.setFirstname("admin");
            user.setLastname("admin");
            user.setEmail("admin@gmail.com");
            user.setRole(Role.ADMIN);
            user.setPassword(passwordEncoder.encode("admin"));

            userRepository.save(user);
        }

    }
}
