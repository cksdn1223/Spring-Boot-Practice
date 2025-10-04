swagger open api 를 추가했는데
작동이 안되는 문제가발생

OpenApiConfig
파일에
```java
    @Bean
    public OpenAPI carDatabaseOpenApi() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info()
                        .title("Todo List API")
                        .description("Todo List 애플리케이션을 위한 API 명세서입니다.")
                        .version("v1.0.0")
                );
    }
```
추가 해서 Jwt 인증 받을 수 있게 함

하지만 추가로 오류 발생
SecurityConfig 에서

```java
                        // Swagger UI와 API 문서를 위한 경로를 인증 없이 허용합니다.
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
```
추가해서 경로를 허용해줌

추가로 버전이
2.5.0 사용시 오류발생
2.0.2 버전도 오류발생

최신버전인 2.8.0 버전으로 바꿨더니 문제없이 작동