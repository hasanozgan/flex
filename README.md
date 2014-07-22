## Flex Micro Rest Framework

flex for your legacy servlet project.


#### Easy Install
```xml
    <web-app>
      <filter>
        <filter-name>FlexFilter</filter-name>
        <filter-class>com.hasanozgan.flex.core.FlexFilter</filter-class>
        <init-param>
          <param-name>resource-scan</param-name>
          <param-value>com.hasanozgan.flex.examples</param-value>
          </init-param>
        <init-param>
          <param-name>api-root</param-name>
          <param-value>/api</param-value>
        </init-param>
        <init-param>
          <param-name>authenticator</param-name>
          <param-value>com.hasanozgan.flex.examples.MyAuthenticator</param-value>
        </init-param>
      </filter>
      <filter-mapping>
        <filter-name>FlexFilter</filter-name>
        <url-pattern>/api/*</url-pattern>
      </filter-mapping>
    </web-app>
```


#### Resource Example

```java    
    package com.hasanozgan.flex.examples;
    
    import com.hasanozgan.flex.core.annotations.Secured;
    import com.hasanozgan.flex.core.HttpMethod;
    import com.hasanozgan.flex.core.annotations.Path;
    import com.hasanozgan.flex.core.models.request.HttpContext;
    import com.hasanozgan.flex.core.models.response.Result;
    import com.hasanozgan.flex.core.models.response.Results;
    
    /**
     * Created by hasanozgan on 14/07/14.
     */
    public class UserResource {
    
        @Secured
        @Path(path = "/user/info", method = HttpMethod.GET)
        public static Result<User> getUserInfo() {
            return Results.ok(new User(1, "hasan"));
        }
    
        @Secured
        @Path(path = "/user/info", method = HttpMethod.POST)
        public static Result<User> postUserInfo(HttpContext<User> ctx) {
            return Results.ok(ctx.getEntity());
        }
    
        @Secured
        @Path(path = "/user/coupons/{gameType}", method = HttpMethod.GET)
        public static Result<String> getUserCoupons(HttpContext ctx) {
            return Results.ok(ctx.getPathParameter("gameType"));
        }
    
        @Path(path = "/user/details", method = HttpMethod.GET)
        public static Result getUserDetails(HttpContext ctx) {
            return Results.ok();
        }
    }
```
