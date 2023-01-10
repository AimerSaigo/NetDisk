package com.example.netdisk.service;

import com.example.netdisk.common.service.LogicService;
import com.example.netdisk.dao.UserDao;
import com.example.netdisk.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService extends LogicService<UserDao,User,Long> {

    public UserService(@Autowired UserDao dao) {
        super(dao);
    }


}
