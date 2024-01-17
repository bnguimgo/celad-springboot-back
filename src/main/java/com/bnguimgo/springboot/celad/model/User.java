package com.bnguimgo.springboot.celad.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {

    //set initialValue = 4 since there are 3 users initialized
    //Note: GenerationType.IDENTITY doesn't work with if there is initialization from data.sql --> Conflict with primary_key
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSequenceParam")
    @SequenceGenerator(name = "UserSequenceParam", sequenceName = "UserSequenceName", initialValue = 4, allocationSize = 1)
    @Column(name = "USER_ID", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

}
