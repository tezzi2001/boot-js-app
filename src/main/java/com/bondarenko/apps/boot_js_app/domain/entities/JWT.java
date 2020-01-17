package com.bondarenko.apps.boot_js_app.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tokens")
@Data
@EqualsAndHashCode(callSuper = true)
public class JWT extends BaseEntity{
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

    public JWT(String login, String refreshToken, String fingerprint, Date expiresAt, Date createdAt, Date updatedAt) {
        this.login = login;
        this.refreshToken = refreshToken;
        this.fingerprint = fingerprint;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public JWT() {
    }
}
