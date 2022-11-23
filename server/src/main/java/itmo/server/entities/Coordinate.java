package itmo.server.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString(of = {"id", "x", "y"})
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "coordinates")
public class Coordinate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "x")
    private double x;

    @Column(name = "y", nullable = false)
    private Float y;
}