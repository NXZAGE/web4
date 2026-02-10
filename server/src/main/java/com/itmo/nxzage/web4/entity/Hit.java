package com.itmo.nxzage.web4.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column
    @NotNull
    private Double x;
    @Column
    @NotNull
    private Double y;
    @Column
    @NotNull
    @Positive
    private Double r;
    @Column
    @NotNull
    private Boolean hit;
    @Column(name = "exec_time")
    @NotNull
    @Positive
    private Long execTime;
    @Column(name = "registred_at")
    @NotNull
    private OffsetDateTime registredAt;
}
