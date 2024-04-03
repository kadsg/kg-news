package kg.news.service.impl;

import kg.news.entity.User;
import kg.news.repository.UserRepository;
import kg.news.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User queryUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
