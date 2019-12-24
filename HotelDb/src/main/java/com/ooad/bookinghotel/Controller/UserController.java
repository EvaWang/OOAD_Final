package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.HotelDbApplication;
import com.ooad.bookinghotel.HotelDb.User;
import com.ooad.bookinghotel.HotelDb.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController    // This means that this class is a Controller
@RequestMapping(path="/User") // This means URL's start with /demo (after Application path)
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

    @Autowired
    private UserRepository userRepository;

//    debug use
//    @GetMapping("/all")
//    Iterable<User> all() {
//        return userRepository.findAll();
//    }

    @PostMapping("/add")
    User newUser(@RequestBody Map<String, String> userObj) {
        User newUser = new User();
        newUser.setLastName(userObj.get("lastname"));
        newUser.setFirstName(userObj.get("firstname"));
        newUser.setAccount(userObj.get("account"));
        newUser.setPassword(userObj.get("password"));
        newUser.setEmail(userObj.get("email"));

        return userRepository.save(newUser);
    }

    // Single item

    @GetMapping("/findOne/{id}")
    User one(@PathVariable int id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @PutMapping("/updateOne/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable int id) {

        return userRepository.findById(id)
                .map(updateUser -> {
                    updateUser.setEmail(newUser.getEmail());
                    return userRepository.save(updateUser);
                }).orElseThrow(() -> new NotFoundException(id));
    }

}
