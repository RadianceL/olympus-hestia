# dynamic-datasource-connector
<div>
  <img src="https://img.shields.io/badge/%F0%9F%93%85%20Last%20update%20-%20July%2020%202022-green.svg" alt="Last update: April, 2022">
</div>


## Guide

---
#### STEP 1
change `@SpringBootApplication` to `@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)`

note: this for disable spring database auto configuration check default database config, dynamic-datasource-connector use user-defined `spring.database.dynamic.databaseConnectionConfig`
#### STEP 2
config `spring.database.dynamic.databaseConnectionConfig` like the example below
```yml
spring:
  profiles:
    active: ${APPLICATION_PROFILE:daily}
  sql:
    init:
      encoding: UTF-8
  datasource:
    dynamic:
      databaseConnectionConfig:
        CHN:
          url: jdbc:mysql://${DATABASE_URL_CHN}?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=GMT%2B8
          username: ${DATABASE_USERNAME_CHN}
          password: ${DATABASE_PASSWORD_CHN}
        USA:
          url: jdbc:mysql://${DATABASE_URL_EN}?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=GMT-5
          username: ${DATABASE_USERNAME_EN}
          password: ${DATABASE_PASSWORD_EN}
```

#### STEP 3
define your own selector for database, for example `HandlerInterceptor`
```java
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HttpCountrySelectorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String currentDatabase = request.getHeader(RequestBasicHeader.CONSTITUTIONAL_CODE);
        if (StringUtils.isNotBlank(currentDatabase)) {
            // this for select database, utils depend on ThreadLocal
            DatasourceSelectorHolder.setCurrentDatabase(countryCode);
        }
        return true;
    }

    /**
     * 处理后调用（正常）
     */
    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) {

    }

    /**
     * 处理后调用(任何情况)
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        DatasourceSelectorHolder.clear();
        GlobalizationLocalUtil.clear();
    }
}
```

that`s all, Finish!
## Verify

```logcatfilter
2022-07-20 14:49:25.869  INFO 11987 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2706 ms
2022-07-20 14:49:26.035  INFO 11987 --- [           main] com.zaxxer.hikari.HikariDataSource       : CHN - Starting...
2022-07-20 14:49:26.391  INFO 11987 --- [           main] com.zaxxer.hikari.HikariDataSource       : CHN - Start completed.
2022-07-20 14:49:26.391  INFO 11987 --- [           main] com.zaxxer.hikari.HikariDataSource       : USA - Starting...
2022-07-20 14:49:26.520  INFO 11987 --- [           main] com.zaxxer.hikari.HikariDataSource       : USA - Start completed.
```
