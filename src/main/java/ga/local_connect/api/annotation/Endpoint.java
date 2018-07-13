package ga.local_connect.api.annotation;

import ga.local_connect.api.enumeration.EndpointCategory;
import ga.local_connect.api.enumeration.HttpMethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
    HttpMethodType method();
    EndpointCategory category();
    String name();
}
