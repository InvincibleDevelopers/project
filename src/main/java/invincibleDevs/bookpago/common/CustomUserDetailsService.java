package invincibleDevs.bookpago.common;

import invincibleDevs.bookpago.Users.UserEntity;
import invincibleDevs.bookpago.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByKakaoId(Long.parseLong(username));

        if (userData != null) {

            return new CustomUserDetails(userData);
        }

        return null;
    }
}
