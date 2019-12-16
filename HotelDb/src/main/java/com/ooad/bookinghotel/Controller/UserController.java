package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.HotelDbApplication;
import com.ooad.bookinghotel.HotelDb.User;
import com.ooad.bookinghotel.HotelDb.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    // Single item

    @GetMapping("/findOne/{id}")
    User one(@PathVariable int id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @PutMapping("/updateOne/{id}")
    User upddateUser(@RequestBody User newUser, @PathVariable int id) {
        Optional<User> user = userRepository.findById(id);


        return userRepository.findById(id)
                .map(employee -> {
                    employee.setName(newUser.getName());
                    employee.setEmail(newUser.getEmail());
                    return userRepository.save(employee);
                }).orElseThrow(() -> new NotFoundException(id));
    }

}
