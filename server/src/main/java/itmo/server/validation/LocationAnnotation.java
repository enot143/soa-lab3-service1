package itmo.server.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LocationDtoValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocationAnnotation {
    String message() default "{validation.ValidLocation.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

