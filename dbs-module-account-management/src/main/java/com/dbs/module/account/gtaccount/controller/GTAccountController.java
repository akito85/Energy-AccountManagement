package com.dbs.module.account.gtaccount.controller;

import com.dbs.module.account.gtaccount.services.GTAccountServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dbs/api/gt-account")
@Api(tags = "globalTypeAccount")
public class GTAccountController {
    @Autowired
    private GTAccountServiceImpl gtAccountService;

//    @GetMapping("/get-RT")
//    public ResponseEntity<?> getRT() {
//        return gtAccountService.getRT();
//    }
//
//    @GetMapping("/get-PK")
//    public ResponseEntity<?> getPK() {
//        return gtAccountService.getPK();
//    }
//
//    @GetMapping("/get-KI")
//    public ResponseEntity<?> getKI() {
//        return gtAccountService.getKI();
//    }

    @GetMapping("/get-LT")
    public ResponseEntity<?> getLT() {
        return gtAccountService.getLT();
    }

    @GetMapping("/get-LR/{parentId}")
    public ResponseEntity<?> getLR(@PathVariable Integer parentId) {
        return gtAccountService.getLR(parentId);
    }
}
