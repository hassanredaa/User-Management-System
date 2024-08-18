package com.login.app.loginregapi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserInfo {

    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String nid;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    private String phoneNumber;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}
