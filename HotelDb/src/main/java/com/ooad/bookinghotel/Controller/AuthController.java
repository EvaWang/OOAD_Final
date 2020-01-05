package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.User;
import com.ooad.bookinghotel.HotelDb.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path="/Auth") // This means URL's start with /demo (after Application path)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        String account = authenticationRequest.getUsername();
        Optional<User> user = userRepository.findByAccount(account);
        if (user.isPresent() == false) {
            throw new UsernameNotFoundException("User not found with username: " + account);
        }

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.get().getId());
        claims.put("name", user.get().getName());

        final UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.get().getAccount(), user.get().getPassword(),
                new ArrayList<>());
        final String token = jwtToken.generateToken(userDetails, claims);
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        return ResponseEntity.ok(new JwtResponse(token, user.get().getName()));
    }

    private void authenticate(String username, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }

}