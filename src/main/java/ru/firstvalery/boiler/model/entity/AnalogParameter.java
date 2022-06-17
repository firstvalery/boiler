package ru.firstvalery.boiler.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "analog_parameter")
@Getter
@Setter
@NoArgsConstructor
public class AnalogParameter {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "analog_parameter_generator", sequenceName = "analog_parameter_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analog_parameter_generator")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "alarm_min", nullable = false)
    private Double alarmMin;

    @Column(name = "lim_min", nullable = false)
    private Double limMin;

    @Column(name = "lim_max", nullable = false)
    private Double limMax;

    @Column(name = "alarm_max", nullable = false)
    private Double alarmMax;

    @Column(name = "hysteresis", nullable = false)
    private Double hysteresis;

    @Column(name = "blur", nullable = false)
    private Double blur;

    @Column(name = "data_offset", nullable = false)
    private Integer dataOffset;

}