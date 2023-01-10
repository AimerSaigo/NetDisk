package com.example.netdisk.entity;

import com.example.netdisk.common.entity.LogicEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.util.DigestUtils;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;

@Entity
@Getter
@Setter
@Inheritance
@Where(clause = "is_deleted = 0")
@DiscriminatorColumn(name = "type")
public abstract class User extends LogicEntity {
    @Column
    protected String username;

    @Column
    protected String password;

    /**
     * 创建所有User时，密码经过md5加密
     * @param psd
     */
    public void setPassword(String psd){
        this.password = DigestUtils.md5DigestAsHex(psd.getBytes());
    }

    static public String getMD5password(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    @Column(updatable = false, insertable = false)
    protected Integer type;
}
