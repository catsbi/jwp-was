package webserver.processor;

import controller.LoginController;
import controller.UserController;
import controller.UserListController;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Processors {
    private static final NotFoundProcessor NOT_FOUND_PROCESSOR = new NotFoundProcessor();
    private static final List<Processor> processors = Arrays.asList(
            new ControllerProcessor(
                    Arrays.asList(new UserController(), new LoginController(), new UserListController())
            ),
            new TemplateProcessor(),
            new ResourceProcessor()
    );

    public void process(final HttpRequest httpRequest, final HttpResponse httpResponse) throws IOException {
        processors.stream()
                .filter(processor -> processor.isMatch(httpRequest))
                .findFirst()
                .orElse(NOT_FOUND_PROCESSOR)
                .process(httpRequest, httpResponse);
    }
}