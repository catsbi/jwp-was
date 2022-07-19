package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import webserver.domain.HttpRequest;
import webserver.domain.RequestLine;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void request_resttemplate() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("HTTP GET 요청에 대한 파싱 결과를 확인할 수 있다.")
    @Test
    void getRequestLine() {
        ResponseEntity<HttpRequest> response = restTemplate.getForEntity("http://localhost:8080/users", HttpRequest.class);
        HttpRequest httpRequest = response.getBody();
        RequestLine requestLine = Objects.requireNonNull(httpRequest).getRequestLine();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/users");
        assertThat(requestLine.getProtocol()).isEqualTo("HTTP");
        assertThat(requestLine.getVersion()).isEqualTo("1.1");
    }

    @DisplayName("HTTP POST 요청에 대한 파싱 결과를 확인할 수 있다.")
    @Test
    void postRequestLine() {
        ResponseEntity<HttpRequest> response = restTemplate.postForEntity("http://localhost:8080/users", null, HttpRequest.class);
        HttpRequest httpRequest = response.getBody();
        RequestLine requestLine = Objects.requireNonNull(httpRequest).getRequestLine();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(requestLine.getPath()).isEqualTo("/users");
        assertThat(requestLine.getProtocol()).isEqualTo("HTTP");
        assertThat(requestLine.getVersion()).isEqualTo("1.1");
    }
}
