package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.Hotel;
import com.ooad.bookinghotel.HotelDb.HotelDbApplication;
import com.ooad.bookinghotel.HotelDb.User;
import com.ooad.bookinghotel.HotelDb.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/User") // This means URL's start with /demo (after Application path)
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

    @Autowired
    private UserRepository userRepository;
//
//    @PostMapping("/add")
//    public User newUser(@RequestBody User newUser) {
//        log.info("newUser:"+newUser.getEmail());
//        return userRepository.save(newUser);
//    }

//    @CrossOrigin(origins = "http://localhost:8081")
//    @PostMapping(path="/add")
//    public @ResponseBody User newUser(@RequestBody User newUser) {
//        log.info("newUser:"+newUser.getEmail());
//        userRepository.save(newUser);
//        return newUser;
//    }

//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    public String submit(@Valid @ModelAttribute("user")User user,
//                         BindingResult result, ModelMap model) {
//        if (result.hasErrors()) {
//            return "error";
//        }
//        model.addAttribute("name", user.getName());
//        model.addAttribute("contactNumber", user.getEmail());
//        model.addAttribute("id", user.getId());
//        return "ok";
//    }

}
