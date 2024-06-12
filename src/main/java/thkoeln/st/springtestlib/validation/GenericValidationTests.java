package thkoeln.st.springtestlib.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Check whether object fields contain certain constraints to ensure valid objects
 */
@Slf4j
public class GenericValidationTests extends GenericTests {

    private Validator validator;

    public GenericValidationTests(WebApplicationContext appContext) {
        super(appContext);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Check whether an invalid object results in the right amount of violations
     * @param objectDescription description of the object which should be checked
     * @param expectedViolations expected number of violations
     * @throws Exception
     */
    public void checkValidation(ObjectDescription objectDescription, int expectedViolations) throws Exception {
        Object object = objectBuilder.buildInvalidObject(objectDescription);

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

        log.info("These violations were found:");
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            log.info(constraintViolation.toString());
        }

        assertEquals(expectedViolations, constraintViolations.size());
    }
}
