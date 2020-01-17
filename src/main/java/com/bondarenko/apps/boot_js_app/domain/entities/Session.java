package com.bondarenko.apps.boot_js_app.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tokens")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Session extends BaseEntity{
    @Column(name = "login", length = 60, nullable = false)
    private String login;
    @Column(name = "refreshToken", length = 1300, nullable = false)
    private String refreshToken;
    @Column(name = "fingerprint", length = 1300, nullable = false)
    private String fingerprint;
    @Column(name = "expiresAt", nullable = false)
    private Date expiresAt;
    @Column(name = "createdAt", nullable = false)
    private Date createdAt;
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;
}
