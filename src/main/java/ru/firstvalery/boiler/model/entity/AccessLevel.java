package ru.firstvalery.boiler.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "access_level")
@Getter
@Setter
@NoArgsConstructor
public class AccessLevel {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "access_level_generator", sequenceName = "access_level_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_level_generator")
    private Integer id;

    @Lob
    @Column(name = "code", nullable = false)
    private String code;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;
}