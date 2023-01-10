package com.example.netdisk.dao;

import com.example.netdisk.common.dao.LogicDAO;
import com.example.netdisk.entity.CommenUser;
import org.springframework.stereotype.Service;


public interface CommenUserDao extends LogicDAO<CommenUser,Long> {

    CommenUser findByUsernameAndPassword(String username,String password);
}
