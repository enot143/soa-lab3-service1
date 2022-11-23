package itmo.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@NoArgsConstructor
public class RouteDto implements Serializable {
    @NotBlank(message = "name can't be null or empty")
    private String name;

    @NotNull(message = "coordinates can't be null")
    private CoordinateDto coordinates;

    @NotNull(message = "from can't be null")
    private LocationDto from;

    @NotNull(message = "to can't be null")
    private LocationDto to;

    @DecimalMin(value = "1.0", inclusive = false, message = "distance must be > 1")
    private double distance;
}
