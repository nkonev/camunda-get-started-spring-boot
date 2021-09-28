# Camunda Platform - Getting Started with Camunda Platform and Spring Boot

This Repository contains the example Spring Boot application for the guide at [camunda.org](http://camunda.org/get-started/spring-boot.html).

Every step of the tutorial was tagged in this repository. You can jump to the final state of each step
by the following command:

```
git checkout -f Step-X
```

If you want to follow the tutorial along please clone this repository and checkout the `Start` tag.

```
git clone https://github.com/camunda/camunda-get-started-spring-boot.git
git checkout -f Start
```

License: The source files in this repository are made available under the [Apache License Version 2.0](./LICENSE).

# Links
Spring Boot https://docs.camunda.org/get-started/spring-boot/model/
Business Process creation https://docs.camunda.org/get-started/quick-start/service-task/
Modeler (GUI) https://camunda.com/download/modeler/

# Open DB
```
docker exec -it camunda-get-started-spring-boot_postgresql_1 psql -U camunda
```

# Ask kratos
[doc](https://www.ory.sh/kratos/docs/reference/api/#operation/toSession)
```
curl -i -H 'Cookie: ory_kratos_session=MTYzMTYzMTgxM3xEdi1CQkFFQ180SUFBUkFCRUFBQVJfLUNBQUVHYzNSeWFXNW5EQThBRFhObGMzTnBiMjVmZEc5clpXNEdjM1J5YVc1bkRDSUFJRXhqYVV0elJIbG9NMlEzYkdwdWVscFhZbk5ETm10emVsZHFTRWhLWTNoS3ytInr_UoJIHlpKDwk7jFBNWDn5-ziOP6L8Si2HJj7gMw=='  'http://127.0.0.1:4433/sessions/whoami'
```
It responds 'x-kratos-authenticated-identity-id' header so we can use http forward auth

# Start BFF
```
cd bff
npm run start
```


# Create app
```
curl -i -X POST 'http://localhost:8080/mortgage-application' -d '{"property": "Красная Площадь, д. 1"}' -H "X-USER-ID: 30e6037a-dce0-4280-aeac-dfe6fe66900d" -H "Content-Type: application/json"
curl -i -X POST 'http://localhost:8080/mortgage-application' -d '{"property": "Зелёная Площадь, д. 1"}' -H "X-USER-ID: 30e6037a-dce0-4280-aeac-dfe6fe66900d" -H "Content-Type: application/json"
```

# Get app
```
curl -i 'http://localhost:8080/mortgage-application' -H "X-USER-ID: 30e6037a-dce0-4280-aeac-dfe6fe66900d"
```

# JS versions
```
npm --version
7.6.0

node --version
v15.11.0
```

# Open camunda
```
http://localhost:8066
```
demo/demo

# Open employee workspace
http://127.0.0.1:9083

# Open customer workspace
http://127.0.0.1:8083
