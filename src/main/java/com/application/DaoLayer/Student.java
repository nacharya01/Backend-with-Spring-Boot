package com.application.DaoLayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student {

    //Id in database
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //fullName
    @NotBlank(message = "Name field can't be empty")
    @Size(min = 4,message = "Full Name should be atleast 4 letter long")
    private String name;
    //major
    @NotBlank(message = "Major field can't be empty")
    private String major;
    //phone Number
    @NotBlank(message = "Phone Number should be 10 digits")
    @Size(min = 10, max = 10,message = "Number should be the length of 10 ")
    private String phoneNumber;
    //email
    @NotBlank(message = "Email field can't be blank")
    @Email(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",message = "Invalid email")
    private String email;
    //password
    @NotBlank(message = "Password can't be blank")
    @Size(min = 8, message = "Password should be at least 8 letters long")
    @Column(length = 4000)
    private String password;

    @NotBlank
    private String role;
}

