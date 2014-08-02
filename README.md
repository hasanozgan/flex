## Flex Micro Framework for REST App

Flex for your legacy servlet project.


#### Easy Install
```xml
  <dependency>
    <groupId>com.hasanozgan.flex</groupId>
    <artifactId>flex-core</artifactId>
    <version>0.3.0-SNAPSHOT</version>
  </dependency>
``` 

```xml
<web-app>
  <filter>
    <filter-name>FlexFilter</filter-name>
    <filter-class>com.hasanozgan.flex.core.FlexFilter</filter-class>
    <init-param>
      <param-name>controller-scan</param-name>
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
import com.hasanozgan.flex.core.annotations.Action;
import com.hasanozgan.flex.core.models.request.HttpContext;
import com.hasanozgan.flex.core.models.response.Result;
import com.hasanozgan.flex.core.models.response.Results;

/**
 * Created by hasanozgan on 14/07/14.
 */
public class UserController {

    @Secured
    @Action(uri = "/user/info", method = HttpMethod.GET)
    public Result<User> getUserInfo() {
        return Results.ok(new User(1, "hasan"));
    }

    @Secured
    @Action(uri = "/user/info", method = HttpMethod.POST)
    public Result<User> postUserInfo(HttpContext<User> ctx) {
        return Results.ok(ctx.getEntity());
    }

    @Secured
    @Action(uri = "/user/coupons/{gameType}", method = HttpMethod.GET)
    public Result<String> getUserCoupons(HttpContext ctx) {
        return Results.ok(ctx.getPathParameter("gameType"));
    }

    @Action(uri = "/user/details", method = HttpMethod.GET)
    public Result getUserDetails(HttpContext ctx) {
        return Results.ok();
    }
}
```
