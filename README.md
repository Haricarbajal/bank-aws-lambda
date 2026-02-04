## javaLambda – Cálculo de cuotas de crédito con AWS Lambda (Java)

![Java](https://img.shields.io/badge/Java-21%2B-red)
![AWS Lambda](https://img.shields.io/badge/AWS-Lambda-orange)
![Maven](https://img.shields.io/badge/Build-Maven-blue)

### Descripción

Este proyecto implementa una función **AWS Lambda en Java** para el cálculo preciso de **cuotas mensuales de préstamos bancarios**.  
A partir del monto del préstamo, la tasa de interés y el plazo en meses, aplica la fórmula estándar de amortización 
![Fórmula](src/img/formula.svg)
utilizando `BigDecimal` y `MathContext.DECIMAL128` para garantizar exactitud numérica en escenarios financieros reales.

La función expone un handler `LambdaBank` que recibe un `BankRequest` y devuelve un `BankResponse` con dos simulaciones:  
- **Escenario estándar**: cuota calculada con la tasa de interés indicada.  
- **Escenario con cuenta bancaria**: cuota calculada aplicando una tasa preferencial (reducción de 0.2 puntos porcentuales), ideal para modelar beneficios comerciales a clientes.

---

### Características principales

- **Arquitectura serverless** sobre **AWS Lambda** usando `RequestHandler<BankRequest, BankResponse>`.
- **Cálculo financiero robusto** con `BigDecimal` y `MathContext.DECIMAL128` para evitar errores de redondeo.
- **Dos escenarios de negocio** (tasa estándar y tasa preferencial con cuenta bancaria).
- **Modelo de datos claro y desacoplado**:
  - `BankRequest`: monto (`amount`), plazo en meses (`term`), tasa de interés (`rate`).
  - `BankResponse`: cuota, tasa y plazo tanto para el escenario estándar como para el preferencial.
- **Empaquetado listo para producción** mediante `maven-shade-plugin` (fat JAR).

---

### Tecnologías utilizadas

- **Lenguaje**: Java  
- **Plataforma**: AWS Lambda  
- **Build tool**: Maven  
- **Dependencias clave**:
  - `aws-lambda-java-core`
  - `aws-lambda-java-events`
  - `aws-lambda-java-log4j2`
  - `junit-jupiter-api` (para testing)

---

### Estructura del proyecto
