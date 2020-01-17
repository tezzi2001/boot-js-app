package com.bondarenko.apps.boot_js_app.domain.entities;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
abstract class BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;
}