package com.hasanozgan.flex.core.utils;

import java.util.List;
import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class URLData {
    private final ResourceData resourceData;
    private final String urlPattern;
    private final String realUrl;
    private final Map<String, String> parameters;

    public URLData(String urlPattern, String realUrl, Map<String, String> parameters, ResourceData resourceData) {
        this.urlPattern = urlPattern;
        this.realUrl = realUrl;
        this.parameters = parameters;
        this.resourceData = resourceData;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public ResourceData getResourceData() {
        return resourceData;
    }
}