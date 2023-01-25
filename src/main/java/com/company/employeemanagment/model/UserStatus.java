package com.company.employeemanagment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@SuppressWarnings("ALL")
@Data
@Entity
public class UserStatus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long statusId;
    private String name;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
//    @OneToMany(mappedBy = "status")
//    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @OneToMany(cascade = CascadeType.ALL, mappedBy="status", orphanRemoval = true)
    private Set<User> users;
}
