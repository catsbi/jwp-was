package webserver.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import webserver.domain.HttpRequest;
import webserver.domain.Path;
import webserver.domain.RequestLine;
import webserver.domain.RequestMapping;
import webserver.domain.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public interface Controller {

    default ResponseEntity<?> execute(HttpRequest httpRequest) {
        try {
            Method method = getExecutableMethod(httpRequest.getRequestLine());

            return (ResponseEntity<?>) method.invoke(this, httpRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().body(e.getMessage());
        }
    }

    default Method getExecutableMethod(RequestLine requestLine) throws NoSuchMethodException {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> {
                    RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
                    return Objects.nonNull(annotation)
                            && supportMethod(method.getDeclaredAnnotation(RequestMapping.class), requestLine);
                }).findFirst()
                .orElseThrow(NoSuchMethodException::new);
    }

    default boolean support(RequestLine requestLine) {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getDeclaredAnnotation(RequestMapping.class) != null)
                .anyMatch(method -> supportMethod(method.getDeclaredAnnotation(RequestMapping.class), requestLine));
    }

    private boolean supportMethod(RequestMapping annotation, RequestLine requestLine) {
        boolean matchMethod = Arrays.stream(annotation.method())
                .anyMatch(method -> method.equals(requestLine.getMethod()));

        boolean matchPath = new Path(annotation.value()).containsPath(requestLine.getPath());

        return matchPath && matchMethod;
    }
}
