package webserver.domain;

import java.util.Objects;

public class ResponseEntity<T> extends HttpEntity<T> {

    private final HttpStatus httpStatus;

    public ResponseEntity(HttpStatus httpStatus) {
        this(null, null, httpStatus);
    }

    public ResponseEntity(T body, HttpStatus httpStatus) {
        this(null, body, httpStatus);
    }

    public ResponseEntity(HttpHeaders headers, T body, HttpStatus httpStatus) {
        super(headers, body);
        this.httpStatus = httpStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponseEntity)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ResponseEntity<?> that = (ResponseEntity<?>) o;
        return httpStatus == that.httpStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), httpStatus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        sb.append(this.httpStatus).append(lineSeparator);

        sb.append(getHeaders()).append(lineSeparator);
        if (hasBody()) {
            sb.append(lineSeparator).append(getBody()).append(lineSeparator);
        }

        return sb.toString();
    }

    public static <T> ResponseBuilder<T> ok() {
        return status(HttpStatus.OK);
    }

    public static ResponseBuilder created(String locationValue) {
        HttpHeaders httpHeaders = HttpHeaders.defaultResponseHeader();
        httpHeaders.add(HttpHeaders.LOCATION, locationValue);

        return status(HttpStatus.CREATED).headers(httpHeaders);
    }

    public static ResponseBuilder badRequest() {
        return status(HttpStatus.BAD_REQUEST);
    }

    public static ResponseBuilder notFound() {
        return status(HttpStatus.NOT_FOUND);
    }

    public static ResponseBuilder internalServerError() {
        return status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseBuilder found(String locationValue) {
        HttpHeaders httpHeaders = HttpHeaders.defaultResponseHeader();
        httpHeaders.add(HttpHeaders.LOCATION, locationValue);

        return status(HttpStatus.FOUND).headers(httpHeaders);
    }

    public static ResponseBuilder status(HttpStatus status) {
        return new ResponseBuilder<>(status);
    }


    public static class ResponseBuilder<T> {
        private HttpStatus status;
        private HttpHeaders headers = new HttpHeaders();

        public ResponseBuilder(HttpStatus status) {
            this.status = status;
        }

        public ResponseBuilder<T> headers(HttpHeaders headers) {
            this.headers = headers;

            return this;
        }

        public ResponseBuilder<T> jsonHeader() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, ContentType.JSON.getContentType());

            return headers(httpHeaders);
        }

        public ResponseBuilder<T> htmlHeader() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, ContentType.HTML.getContentType());

            return headers(httpHeaders);
        }

        public ResponseEntity body(T body) {
            return new ResponseEntity<>(this.headers, body, this.status);
        }

        public ResponseEntity<T> build() {
            return body(null);
        }

        public ResponseBuilder<T> cookie(Cookie cookie) {
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            return this;
        }

        public ResponseBuilder<T> headers(String key, String value) {
            headers.add(key, value);
            return this;
        }
    }
}
