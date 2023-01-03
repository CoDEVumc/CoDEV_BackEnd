package com.codevumc.codev_backend.errorhandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class CoDevResponse {
    private int code = HttpStatus.OK.value();
    private Object result;

    public CoDevResponse() { }

    public void setResult(Object result) {
        this.result = result;
    }

    public static class ResponseMap extends CoDevResponse {

        private Map responseData = new HashMap();

        public ResponseMap() {
            setResult(responseData);
        }

        public void setResponseData(String key, Object value) {
            this.responseData.put(key, value);
        }
        public void clear() {
            this.responseData.clear();
        }

    }
}
