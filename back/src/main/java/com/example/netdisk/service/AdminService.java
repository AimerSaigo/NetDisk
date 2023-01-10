package com.example.netdisk.service;

import com.example.netdisk.common.service.LogicService;
import com.example.netdisk.dao.AdminDao;
import com.example.netdisk.dao.CommenUserDao;
import com.example.netdisk.entity.Admin;
import com.example.netdisk.entity.CommenUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends LogicService<AdminDao, Admin,Long> {
    public AdminService(@Autowired AdminDao dao) {
        super(dao);
    }
}
