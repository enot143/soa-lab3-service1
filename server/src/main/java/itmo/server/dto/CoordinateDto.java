package itmo.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@NoArgsConstructor
public class CoordinateDto implements Serializable {
    private double x;
    @NotNull(message = "coordinate y can't be null")
    @Max(value = 271, message = "coordinate y must be <= 271")
    private Float y;
}
