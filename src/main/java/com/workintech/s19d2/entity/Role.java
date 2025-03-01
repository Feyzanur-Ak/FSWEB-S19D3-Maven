package com.workintech.s19d2.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name="role",schema = "bank")
public class Role implements GrantedAuthority {
    //spring securitynin rol tablosu oldu


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "authority")
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private Set<Member> members;

}
