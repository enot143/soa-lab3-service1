package itmo.server.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@ToString(of = {"id", "name", "coordinates", "creationDate", "from", "to", "distance"})
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "route")
public class Route implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinate coordinates;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "\"from\"", nullable = false)
    private Location from;

    @ManyToOne
    @JoinColumn(name = "\"to\"", nullable = false)
    private Location to;

    @Column(name = "distance")
    private double distance;
}