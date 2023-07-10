package com.example.jwtauthentication.Controllers;

import com.example.jwtauthentication.model.JwtRequest;
import com.example.jwtauthentication.model.JwtResponse;
import com.example.jwtauthentication.security.JwtHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller

@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final UserDetailsService userDetailsService;

    @Autowired
    private final AuthenticationManager manager;


    private final JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserDetailsService userDetailsService, AuthenticationManager manager, JwtHelper helper) {
        this.userDetailsService = userDetailsService;
        this.manager = manager;
        this.helper = helper;
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        this.logger.warn("This is working message");
        return "This is testing";
    }



//   ----------------------------- PostMapping for JSON response--------------------------------------
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
//
//        this.doAuthenticate(request.getEmail(), request.getPassword());
//
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//        String token = this.helper.generateToken(userDetails);
//
//        JwtResponse response = JwtResponse.builder()
//                .jwtToken(token)
//                .username(userDetails.getUsername()).build();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    private void doAuthenticate(String email, String password) {
//
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
//        try {
//            manager.authenticate(authentication);
//
//
//        } catch (BadCredentialsException e) {
//            throw new BadCredentialsException(" Invalid Username or Password  !!");
//        }
//
//    }

// -------------------------------------GetMapping for form based login-----------------------------------
@GetMapping("/login")
public String getLogin(Model model){

        model.addAttribute("user",new JwtRequest());

        return "login";
}

@PostMapping("/login")
public ResponseEntity<JwtResponse> login(@ModelAttribute JwtRequest request, HttpServletResponse res) {

    this.doAuthenticate(request.getEmail(), request.getPassword());

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
    String token = this.helper.generateToken(userDetails);

    Cookie cookie=new Cookie("Authorization",token);
    res.addCookie(cookie);

//      Header header=new Header("Authorization",token);
//      HttpHeaders httpHeaders=new HttpHeaders();
//      httpHeaders.add("Authorization","Bearer "+token);
//      httpHeaders.setBearerAuth(token);
//      MDC.put("Authorization","Bearer "+token);
//      res.getHeader("Authorization");
//      res.addHeader("Authorization","Bearer "+token);

    JwtResponse response = JwtResponse.builder()
            .jwtToken(token)
            .username(userDetails.getUsername()).build();

//    return ResponseEntity.ok().headers(httpHeaders).body(response);
    return new ResponseEntity<>(response, HttpStatus.OK);
}

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
