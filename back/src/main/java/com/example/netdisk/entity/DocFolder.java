package com.example.netdisk.entity;

import com.example.netdisk.common.entity.LogicEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted = 0")
public class DocFolder extends LogicEntity {
    @Column
    private String docFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"docFiles","documents"})
    private Path path;
}
