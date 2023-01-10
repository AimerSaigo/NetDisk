package com.example.netdisk.controller;

import com.example.netdisk.common.controller.LogicController;
import com.example.netdisk.dao.AdminDao;
import com.example.netdisk.entity.Admin;
import com.example.netdisk.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@Api(tags = "管理员实体的控制器")
@RestController
@RequestMapping("/admin")
public class AdminController extends LogicController<AdminService, AdminDao, Admin, Long> {
    public AdminController ( @Autowired AdminService ss ) {
        super ( ss );
    }

    @ApiOperation("测试用方法")
    @GetMapping("/test")
    public String test () {
        return "OK";
    }
}
