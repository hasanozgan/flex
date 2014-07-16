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
    
    import com.hasanozgan.flex.core.annotations.Authenticated;
    import com.hasanozgan.flex.core.HttpMethod;
    import com.hasanozgan.flex.core.annotations.Resource;
    import com.hasanozgan.flex.core.models.request.RequestContext;
    import com.hasanozgan.flex.core.models.request.RequestContextWith;
    import com.hasanozgan.flex.core.models.response.Result;
    import com.hasanozgan.flex.core.models.response.ResultWith;
    
    /**
     * Created by hasanozgan on 14/07/14.
     */
    public class UserResource {
    
        @Authenticated
        @Resource(path = "/user/info", method = HttpMethod.GET)
        public static ResultWith<User> getUserInfo() {
            return Result.ok(new User(1, "hasan"));
        }
    
        @Authenticated
        @Resource(path = "/user/info", method = HttpMethod.POST)
        public static ResultWith<User> postUserInfo(RequestContextWith<User> ctx) {
            return Result.ok(ctx.getEntity());
        }
    
        @Authenticated
        @Resource(path = "/user/coupons/{gameType}", method = HttpMethod.GET)
        public static ResultWith<String> postUserInfo(RequestContext ctx) {
            return Result.ok(ctx.getParameter("gameType"));
        }
    
        @Resource(path = "/user/details", method = HttpMethod.GET)
        public static Result getUserDetails(RequestContext ctx) {
            return Result.ok();
        }
    }
```
