package com.hugodutra.study.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "usuario_usr", schema = "study")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id_int")
    private Long id;
    @Column(name = "usr_login_txt", unique = true, nullable = false)
    private String login;
    @Column(name = "usr_password_txt", nullable = false)
    private  String password;
}
