package itmo.server.dto;

import itmo.server.validation.LocationAnnotation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@LocationAnnotation(message = "Location is not valid (y and z can't be null if id is null)")
public class LocationDto implements Serializable {
    private Integer id;
    private float x;
    private Double y;
    private Float z;
}
