package webserver.domain;

import java.util.Objects;

public class HttpEntity<T> {
    public static final String STRING_EMPTY = "";

    private final HttpHeaders headers;

    private final T body;

    public HttpEntity() {
        this(null, null);
    }

    public HttpEntity(T body) {
        this(null, body);
    }

    public HttpEntity(HttpHeaders headers, T body) {
        if (Objects.isNull(headers)) {
            headers = HttpHeaders.defaultResponseHeader();
        }

        this.headers = headers;
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        if (hasBody()) {
            headers.add(HttpHeaders.CONTENT_LENGTH, getContentLength());
        }
        return headers;
    }

    public T getBody() {
        return body;
    }

    public boolean hasBody() {
        return Objects.nonNull(body);
    }

    protected String getContentLength() {
        return getBody().toString().length() + STRING_EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpEntity)) {
            return false;
        }
        HttpEntity<?> that = (HttpEntity<?>) o;
        return Objects.equals(headers, that.headers) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers, body);
    }
}