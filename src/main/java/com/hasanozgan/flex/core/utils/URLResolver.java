package com.hasanozgan.flex.core.utils;

import com.hasanozgan.flex.core.HttpMethod;
import com.hasanozgan.flex.core.annotations.Path;
import com.hasanozgan.flex.core.annotations.Secured;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class URLResolver {

    private final Set<ResourceData> routes;

    public URLResolver(String resourcePackageScan) {
        this.routes = getResourceContext(resourcePackageScan);
    }

    public Set<ResourceData> getRoutes() {
        return this.routes;
    }

    public URLData getUrlDataForUrl(String url, HttpMethod httpMethod) {
        URLData urlData = null;

        //Get all route keys for this plugin
        for (ResourceData resourceData : getRoutes()) {
            String urlToFind = url;
            String urlPattern = resourceData.getPath();
            boolean urlMatch = false;
            Map<String, String> propertyMap = new HashMap<String, String>();

            if (urlToFind.contains("?")) {
                urlToFind = url.substring(0, url.indexOf("?"));
            }

            if (resourceData.getHttpMethod().equals(httpMethod)) {
                if (urlPattern.equals(urlToFind)) {
                    //Excact match
                    urlMatch = true;
                } else if (urlPattern.contains("{") && urlPattern.contains("}")) {
                    urlMatch = true;
                    //If route contains dynamic parts
                    String[] urlParts = url.split("/");       //The parts of the actual URL,split by /
                    String[] currUrlParts = urlPattern.split("/");  //The parts of the URL pattern, split by /

                    //A match needs the same amount of parts
                    if (urlParts.length == currUrlParts.length) {
                        URLData tempUrlData = null;
                        for (int i = 0; i < currUrlParts.length; i++) {
                            //If current part is a parameter/dynamic part, extract it an add it to the parameter map
                            if (currUrlParts[i].startsWith("{") && currUrlParts[i].endsWith("}")) { //this is a parameter, not part of the URL match
                                propertyMap.put(currUrlParts[i].substring(1, currUrlParts[i].length() - 1), urlParts[i]);
                            } else if (!currUrlParts[i].equals(urlParts[i])) {
                                //If the current non parameter/dynamic part does not match, the URL do not match
                                urlMatch = false;
                            }
                        }
                    } else {
                        urlMatch = false;
                    }
                }
            }

            //If Url match, build up a URL data object
            if (urlMatch) {
                urlData = new URLData(urlPattern, url, propertyMap, resourceData);
                break;
            }
        }

        return urlData;
    }


    private Set<ResourceData> getResourceContext(String resourcePackageScan) {
        Set<ResourceData> resourceContext = new HashSet<ResourceData>();

        if (resourcePackageScan.isEmpty()) return resourceContext;

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(resourcePackageScan))
                .setScanners(new MethodAnnotationsScanner()));

        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(Path.class);

        for (Method method : methodSet) {
            Secured authenticated = (Secured)method.getAnnotation(Secured.class);
            Path path = (Path)method.getAnnotation(Path.class);

            if (null != path) {
                resourceContext.add(new ResourceData(path.uri(), path.method(), authenticated != null, method));
            }
        }

        return resourceContext;
    }


}
