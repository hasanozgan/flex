package com.hasanozgan.flex.core.utils;

import java.util.Map;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class URLData {
    private final ActionData actionData;
    private final String urlPattern;
    private final String realUrl;
    private final Map<String, String> parameters;

    public URLData(String urlPattern, String realUrl, Map<String, String> parameters, ActionData actionData) {
        this.urlPattern = urlPattern;
        this.realUrl = realUrl;
        this.parameters = parameters;
        this.actionData = actionData;
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

    public ActionData getActionData() {
        return actionData;
    }
}