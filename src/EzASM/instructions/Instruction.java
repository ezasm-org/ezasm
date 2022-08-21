package EzASM.instructions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A runtime annotation to denote the methods which should be loaded as instructions.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Instruction {

}
