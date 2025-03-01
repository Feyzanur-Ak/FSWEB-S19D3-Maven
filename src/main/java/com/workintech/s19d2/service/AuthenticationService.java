package com.workintech.s19d2.service;

import com.workintech.s19d2.entity.Member;
import com.workintech.s19d2.entity.Role;
import com.workintech.s19d2.repository.MemberRepository;
import com.workintech.s19d2.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private MemberRepository memberRepository;
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Member register(String email, String password) {
        // Email kontrolü
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            throw new RuntimeException("User with given email already exists! Email: " + email);
        }

        // Şifreyi encode et
        String encodedPassword = passwordEncoder.encode(password);

        // Rolleri al
        Role roleAdmin = addRoleAdmin();
        Role roleUser = addRoleUser();

        // Yeni bir Member oluştur ve bilgileri ata
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);

        // Rolleri Set olarak ekle
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        roles.add(roleAdmin);
        member.setRoles(Arrays.asList(roleUser, roleAdmin));

        // Member'ı kaydet ve döndür
        return memberRepository.save(member);
    }

    private Role addRoleAdmin() {
        // ADMIN rolünü kontrol et
        Optional<Role> roleAdmin = roleRepository.findByAuthority("ADMIN");

        if (roleAdmin.isEmpty()) { // Eğer ADMIN rolü yoksa, oluştur ve kaydet
            Role roleAdminEntity = new Role();
            roleAdminEntity.setAuthority("ADMIN");
            return roleRepository.save(roleAdminEntity);
        } else { // Eğer ADMIN rolü varsa, döndür
            return roleAdmin.get();
        }
    }

    private Role addRoleUser() {
        // USER rolünü kontrol et
        Optional<Role> roleUser = roleRepository.findByAuthority("USER");

        if (roleUser.isEmpty()) { // Eğer USER rolü yoksa, oluştur ve kaydet
            Role roleUserEntity = new Role();
            roleUserEntity.setAuthority("USER");
            return roleRepository.save(roleUserEntity);
        } else { // Eğer USER rolü varsa, döndür
            return roleUser.get();
        }
    }
}
