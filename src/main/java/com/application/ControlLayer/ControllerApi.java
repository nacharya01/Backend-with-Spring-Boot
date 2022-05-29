package com.application.ControlLayer;
import com.application.DaoLayer.DataAccessServiceLayer;
import com.application.DaoLayer.Photo;
import com.application.DaoLayer.PhotoAccess;
import com.application.DaoLayer.Student;
import com.application.ServiceLayer.Login;
import com.application.ServiceLayer.customUserDetailsService;
import com.application.webSecurity.Ticket;
import com.application.webSecurity.jwtToken;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@ControllerAdvice
@Controller
@CrossOrigin
public class ControllerApi {
    @Autowired
    DataAccessServiceLayer dbOperation;
    @Autowired
    PhotoAccess photoAccess;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    customUserDetailsService customService;

    @Autowired
    jwtToken tokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;
    //calling constructor to create an object
    public ControllerApi(){}

    //API: post mapping api
    @GetMapping("/show/{id}")
    @ResponseBody
    public ResponseEntity<Student> getData(@PathVariable("id") Integer id){
        try{
            return new ResponseEntity<>(dbOperation.findById(id).get(),HttpStatus.OK);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }
    @ModelAttribute
    public void assignModel(Model model){
        model.addAttribute("student",new Student());
    }

    //API: showing the homepage
    @GetMapping("/registration")
    public String showRegistration(Model model){
        return "Registration";
    }

    //API: post mapping to upload the file to our projet repositories
    @PostMapping("/upload-file")
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file){
        //Providing the full path where file to be copied
        String path="/Users/nirajanacharya/Desktop/demo/src/main/resources/static";
        String fileName= file.getOriginalFilename();
        String fullPath=path+ File.separator+fileName;
        try {
            //now copying the file from the post request into out directory
            Files.copy(file.getInputStream(), Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
            return new ResponseEntity<>("Success",HttpStatus.CREATED);
        }
        catch(IOException e){
            return new ResponseEntity<>("Some exception occured",HttpStatus.BAD_REQUEST);
        }
    }

    //API: Now uploading the photos to database
    @PostMapping("/uploadPhoto")
    @ResponseBody
    public ResponseEntity<Photo> uploadFileToDb(@RequestParam("file")MultipartFile file) throws IOException {
        Photo photo= new Photo(StringUtils.cleanPath(file.getOriginalFilename()),file.getContentType(),file.getBytes());
        Photo savedPhoto = photoAccess.save(photo);
        return new ResponseEntity<>(savedPhoto, HttpStatus.OK);
    }

    //validating the registration process and determining which way to go
    @PostMapping("/account")
    public String accountCreated(@Valid @ModelAttribute("student") Student student,BindingResult results){

        if(results.hasErrors()){
            return "Registration";
        }
        String password=student.getPassword();
        student.setPassword(encoder.encode(password));
        Student save = dbOperation.save(student);
        return "Login";
    }
    //Creating an account from client side (React Js)
    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@Valid @RequestBody Student student,BindingResult results){
        if(results.hasErrors()){
            return new ResponseEntity<>("Registration",HttpStatus.BAD_REQUEST);
        }
        String password=student.getPassword();
        student.setPassword(encoder.encode(password));
        Student save = dbOperation.save(student);
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }

    //API: to retrieve the login page
    @GetMapping("/signIn")
    public String getLoginPage(){
        return "Login";
    }

    //API: Postmapping for verifying the login credentials and giving them access to the account
    @PostMapping("/process/{Email}/{Password}")
    @ResponseBody
    public ModelAndView verifyingLogin(ModelAndView view,@PathVariable("Email") String email, @PathVariable("Password")String password){
        Optional<Student>student=dbOperation.findByEmail(email);
        if(student.get().getPassword().equals(password)){
            view.setViewName("process");
            return view;
        }
        else {
            view.addObject("error","Either username or password didn't match.");
            view.setViewName("Login");
            return view;
        }
    }

    //API: this is the api after the login has been successfull
    @GetMapping("/myAccount")
    public String loginSuccessfull(Model model) throws ResourceNotFoundException {
        Object principle= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email=((UserDetails) principle).getUsername();
        Optional<Student> student=Optional.ofNullable(dbOperation.findByEmail(email)).orElseThrow(ResourceNotFoundException::new);
        return "process";
    }
    //API: to get the token
    @PostMapping("/getToken")
    public ResponseEntity<?> getToken(@RequestBody Login login) throws Exception{
        UsernamePasswordAuthenticationToken token= new UsernamePasswordAuthenticationToken(login.getUserName(),login.getPassword());
        try {
            this.authenticationManager.authenticate(token);
        }
        catch (BadCredentialsException e){
            e.printStackTrace();
            throw new BadCredentialsException("Bad credential Inserted");
        }
       //If the code comes to this level, which means that authentication has been done
        UserDetails userDetails= customService.loadUserByUsername(login.getUserName());
        String s = tokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new Ticket(s));
    }

}
