package com.application.ServiceLayer;
import com.application.DaoLayer.DataAccessServiceLayer;
import com.application.DaoLayer.Student;
import com.application.ServiceLayer.customUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class customUserDetailsService implements UserDetailsService {
    @Autowired
    DataAccessServiceLayer jpa;
    Optional<Student> student;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        this.student=jpa.findByEmail(username);
        if(student.isPresent()){
            return new customUserDetails(student.get());
        }
        else {
            throw new UsernameNotFoundException("Could not found username");
        }
    }
}
