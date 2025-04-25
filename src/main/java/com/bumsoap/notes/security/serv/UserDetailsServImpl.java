package com.bumsoap.notes.security.serv;

import com.bumsoap.notes.models.User;
import com.bumsoap.notes.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServImpl implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(
                        "존재하지 않는 유저의 username: " + username));
        return UserDetailsImpl.build(user);
    }
}
