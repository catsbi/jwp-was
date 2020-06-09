package http.request;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RequestHeader {
    private static final String COOKIE = "Cookie";
    private static final String COOKIE_KEY_VALUE_DELIMITER = "=";
    private static final String HEADER_DELIMITER = ": ";
    private static final String COOKIE_DELIMITER = ";";

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();
    private final StringBuilder origin = new StringBuilder();

    public RequestHeader() {}

    public void addHeader(final String token) {
        origin.append(token).append('\n');
        String[] tokens = token.split(HEADER_DELIMITER);
        String key = tokens[0];
        String value = tokens[1];

        if (COOKIE.equals(key)) {
            initCookies(value);
            return;
        }

        headers.put(key, value);
    }

    private void initCookies(final String cookies) {
        StringTokenizer tokens = new StringTokenizer(cookies, COOKIE_DELIMITER);

        while (tokens.hasMoreTokens()) {
            addCookie(tokens.nextToken());
        }
    }

    private void addCookie(final String token) {
        String[] tokens = token.trim()
                .split(COOKIE_KEY_VALUE_DELIMITER);

        if (tokens.length < 2) {
            return;
        }

        cookies.put(tokens[0].trim(), tokens[1].trim());
    }

    public String getHeader(final String headerName) {
        return headers.get(headerName);
    }

    public String getCookie(final String cookieName) {
        return cookies.get(cookieName);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return origin.toString();
    }
}