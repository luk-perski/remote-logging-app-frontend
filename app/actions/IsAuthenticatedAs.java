package actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;

/**
 * Necessary annotation for control action AuthenticatedAsAction
 * 
 * @author alsl
 * 
 */
@With(AuthenticatedAsAction.class)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsAuthenticatedAs {
	String[] value() default { "" };
}