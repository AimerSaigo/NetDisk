package com.example.netdisk.entity;

import com.example.netdisk.common.entity.LogicEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted = 0")
@DiscriminatorValue("1")
public class Admin extends User {
    @Column
    protected String adminname;

    @Column
    protected String password;
}
