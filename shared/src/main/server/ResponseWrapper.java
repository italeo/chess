package server;

import java.util.List;
import java.util.Map;

public class ResponseWrapper<T> {
    private final T body;
    private final Map<String, List<String>> headers;

    public ResponseWrapper(T body, Map<String, List<String>> headers) {
        this.body = body;
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
