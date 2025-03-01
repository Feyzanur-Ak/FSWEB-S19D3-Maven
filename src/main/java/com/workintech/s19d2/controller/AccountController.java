package com.workintech.s19d2.controller;


import com.workintech.s19d2.entity.Account;
import com.workintech.s19d2.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping()
    public List<Account> findAll(){
        return  accountService.findAll();
    }

    @PostMapping
    public  Account save(@RequestBody Account account) {
        return  accountService.save(account);
    }
}
