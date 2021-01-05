package com.shopify.common;

import lombok.Data;

@Data
public class RestApiData {
    private int id;
    private String url;
    private String method;
    private String requestHeaders;
    private String requestBody;
    private String status;
    private String responseHeaders;
    private String responseBody;
    private String exceptionMessage;
}
