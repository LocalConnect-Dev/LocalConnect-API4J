package ga.local_connect.api.http;

import ga.local_connect.api.annotation.Endpoint;
import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.enumeration.EndpointCategory;
import ga.local_connect.api.enumeration.HttpMethodType;
import ga.local_connect.api.exception.LocalConnectException;
import org.eclipse.jetty.server.Request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

class EndpointManager {
    private static HashMap<Endpoint, Method> endpoints = new HashMap<>();

    static void register(Class c) {
        for (var method : c.getMethods()) {
            var annotations = method.getAnnotationsByType(Endpoint.class);
            if (annotations.length < 1) continue;

            endpoints.put(annotations[0], method);
        }
    }

    static Object call(HttpMethodType method, EndpointCategory category, String name, Request req) throws Throwable {
        for (var endpoint : endpoints.keySet()) {
            if (endpoint.category() != category ||
                !endpoint.name().equalsIgnoreCase(name)) continue;

            if (endpoint.method() != method) {
                throw new LocalConnectException(
                    HttpStatuses.METHOD_NOT_ALLOWED,
                    APIErrorType.INVALID_METHOD
                );
            }

            try {
                return endpoints.get(endpoint).invoke(null, req);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

        throw new LocalConnectException(
            HttpStatuses.NOT_FOUND,
            APIErrorType.ENDPOINT_NOT_FOUND
        );
    }
}
