package com.edstem.taxibookingandbillingsystem.security;

//import com.edstem.taxibookingandbillingsystem.exception.EntityNotFoundException;

import com.edstem.taxibookingandbillingsystem.exception.EntityNotFoundException;
import com.edstem.taxibookingandbillingsystem.model.User;
import com.edstem.taxibookingandbillingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserRepository userRepository;

//    @Bean
//    public User userDetailsService() {
//        return email ->
//                userRepository.findByEmail(email)
//                        .orElseThrow(() -> new EntityNotFoundException("User"));
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider =
//                new DaoAuthenticationProvider() {
//                    public void additionalAuthenticationChecks(
//                            UserDetails userDetails,
//                            UsernamePasswordAuthenticationToken authentication) {
//                        if (!passwordEncoder()
//                                .matches(
//                                        authentication.getCredentials().toString(),
//                                        userDetails.getPassword())) {
//                            throw new EntityNotFoundException("User");
//                        }
//                    }
//                };
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
}
