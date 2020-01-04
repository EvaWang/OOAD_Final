package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.HotelDbApplication;
import com.ooad.bookinghotel.HotelDb.User;
import com.ooad.bookinghotel.HotelDb.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;


@RestController    // This means that this class is a Controller
@RequestMapping(path="/User") // This means URL's start with /demo (after Application path)
public class UserController {
//    private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);
    private static final String SECRET_KEY = "OOAD_FINAL_PROJECT";

    @Autowired
    private UserRepository userRepository;

//    debug use
//    @GetMapping("/all")
//    Iterable<User> all() {
//        return userRepository.findAll();
//    }

    @PostMapping("/add")
    Boolean newUser(@RequestBody Map<String, String> body) {
        String account = body.get("account");
        if (userRepository.existsByAccount(account)){
            throw new ValidationException("account already existed");
        }

        String password = body.get("password");
        String email = body.get("email");
        String name = body.get("name");

        String encodedPassword = new BCryptPasswordEncoder().encode(password);
//        String hashedPassword = hashData.get_SHA_512_SecurePassword(password);
        User newUser = new User();
        newUser.setAccount(account);
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword);
        newUser.setName(name);
        userRepository.save(newUser);
//        userRepository.save(new User(account, encodedPassword, fullname));
        return true;
//        return userRepository.save(newUser);
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
                    updateUser.setName(newUser.getName());
                    updateUser.setEmail(newUser.getEmail());
                    return userRepository.save(updateUser);
                }).orElseThrow(() -> new NotFoundException(id));
    }

    @PostMapping("/login")
    void login(@RequestBody User newUser){
        userRepository.findByAccount(newUser.getAccount());

    }

//    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {
//
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//        //Let's set the JWT Claims
//        JwtBuilder builder = Jwts.builder().setId(id)
//                .setIssuedAt(now)
//                .setSubject(subject)
//                .setIssuer(issuer)
//                .signWith(key);
//
//        //if it has been specified, let's add the expiration
//        if (ttlMillis > 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }
//
//        //Builds the JWT and serializes it to a compact, URL-safe string
//        return builder.compact();
//    }
//
//    public static Claims decodeJWT(String jwt) {
//        //This line will throw an exception if it is not a signed JWS (as expected)
//        Claims claims = Jwts.parser()
//                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
//                .parseClaimsJws(jwt).getBody();
//        return claims;
//    }

}
