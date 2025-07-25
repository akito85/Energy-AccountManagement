package com.dbs.module.account;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dbs/api/system-master")
@Api(tags = "HealthMaster")
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<Integer> checkHealth(){
        System.out.println("tes health account service is running");
        return ResponseEntity.ok(200);
    }
}
