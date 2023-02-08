package learn.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class VehicleSignal {

    @NotNull
    @DecimalMax("90")
    @DecimalMin("-90")
    BigDecimal latitude;

    @NotNull
    @DecimalMax("180")
    @DecimalMin("-180")
    BigDecimal longitude;
}
