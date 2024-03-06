package com.tpe.domain;

import com.tpe.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role
{

    /*
        Burada oluşturulan user larımızın rolleri için entity oluşturduk.
        Rollerimiz sabit oldukları için enum tipinde tanımladık.
        Enum olan rollerimizi de enums package'ında tanımladık.
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING) // enum sabitlerinin db de string olarak kaydedilmesini sağlar.
    @Column(nullable = false)
    private RoleType type;

}
