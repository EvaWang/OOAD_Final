package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.User;
import com.ooad.bookinghotel.HotelDb.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

        User user = userRepository.findByAccount(account).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + account);
        }
        return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getPassword(),
                new ArrayList<>());
    }

}
