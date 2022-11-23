package itmo.server.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@ToString(of = {"id", "x", "y", "z"})
@EqualsAndHashCode(of = {"id"})
@Table(name = "location")
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "x")
    private float x;

    @Column(name = "y")
    private Double y;

    @Column(name = "z", nullable = false)
    private Float z;
}