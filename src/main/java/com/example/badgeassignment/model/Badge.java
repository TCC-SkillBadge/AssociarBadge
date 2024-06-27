package com.example.badgeassignment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "badge")
@IdClass(BadgeId.class)
public class Badge {

    @Id
    @Column(name = "email_com", nullable = false)
    private String emailCom;

    @Id
    @Column(name = "email_empr", nullable = false)
    private String emailEmpr;

    @Id
    @Column(name = "id_badge", nullable = false)
    private Integer idBadge;

    @Column(name = "dt_emissao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEmissao;

    @Column(name = "dt_vencimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtVencimento;

    @Column(name = "imagem_b", nullable = false)
    private String imagemB;
}
