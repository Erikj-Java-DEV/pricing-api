# 🧵 Inditex Pricing API

Este proyecto es una solución backend desarrollada en **Java con Spring Boot** para resolver una prueba técnica de la plataforma core de ecommerce de **Inditex**. Permite exponer un endpoint REST que, dado un producto, marca y fecha, devuelve el precio aplicable según una tabla de tarifas (PRICE_LIST) gestionada internamente.

---

## 🚀 Tecnologías utilizadas

| Tecnología | Descripción |
|------------|-------------|
| **Java 17** | Lenguaje principal del proyecto |
| **Spring Boot 3.1.4** | Framework principal para la creación del servicio REST |
| **H2 Database** | Base de datos en memoria para pruebas |
| **JUnit + Spring Test** | Frameworks de testing |
| **Maven** | Sistema de build y dependencias |
| **Jacoco** | Medición de cobertura de tests |
| **Lombok** | Reducción de boilerplate |
| **GitHub Actions** | Pipeline de integración continua (CI/CD) |

---

## 🧱 Arquitectura y organización

La solución está estructurada respetando el principio de **arquitectura hexagonal**:

```
┌──────────────────────────┐
│ Infrastructure (REST, DB)│
└────────────┬─────────────┘
             ↓
       Application Layer
             ↓
        Domain (modelo puro)
```

- **Domain:** contiene la entidad `Price`, aislada de frameworks.
- **Application:** orquesta el caso de uso de negocio `GetApplicablePriceUseCase`.
- **Infrastructure:** expone el endpoint REST, maneja excepciones y accede a la base de datos (repository + mapper opcional).

Esto asegura una **separación clara de responsabilidades**, sin acoplamientos entre capas.

---

## ✨ Buenas prácticas aplicadas

### ✅ CLEAN CODE
- Métodos cortos y expresivos.
- Nombres semánticos.
- Responsabilidad única por clase.

### ✅ Principios SOLID
- **S**: `GetApplicablePriceUseCase` se centra solo en lógica de aplicación.
- **O**: la lógica se puede extender para nuevos filtros de precios sin modificar clases existentes.
- **L**: `Price` puede utilizarse desde diferentes capas sin romper la funcionalidad.
- **I**: interfaces como `PriceRepository` son claras y pequeñas.
- **D**: las dependencias son inyectadas desde fuera (constructor), no instanciadas dentro.

---

## 🧪 Testing y cobertura

- Se han implementado **tests de integración** para los 5 escenarios requeridos en el enunciado.
- Además, se añadieron **tests unitarios complementarios**:
  - Gestión de excepciones.
  - Comprobación del modelo y su `toString()`.
  - Casos negativos.
- **Cobertura Jacoco: 100%** del código productivo (`src/main`).

---

## 📦 DTOs y control de excepciones

- Se utiliza el patrón **DTO** (`PriceResponseDto`) para desacoplar la respuesta REST de la entidad `Price`. Esto permite:
  - Ocultar información innecesaria.
  - Controlar el formato de salida.
  - Aplicar transformaciones si fueran necesarias.

- Excepciones personalizadas como `PriceNotFoundException` ayudan a devolver códigos HTTP claros (`404`, `400`, `500`) según el tipo de error:
  - `@RestControllerAdvice` centraliza su manejo.

---

## 🔐 Validación y respuestas claras

- Se validan los parámetros de entrada (formato de fecha, existencia de campos...).
- Las respuestas de error son JSONs con `timestamp`, `message` y `code`.
- Se han cubierto todos los casos con tests.

---

## 🔄 Commit y ramas

- **Estrategia de commits**:
  - En inglés, verbos en presente.
  - Ejemplo: `🧪 Add integration tests for PriceController`.
  
Categoría | Uso
feat: | Añadir una nueva funcionalidad al sistema
fix: | Corrección de errores (bugs)
refactor: | Cambios en el código que no afectan su comportamiento funcional
test: | Creación o modificación de tests (unitarios, integración, etc.)
docs: | Cambios en la documentación (README.md, comentarios, etc.)
chore: | Tareas menores del sistema (configuraciones, ajustes, etc.)
perf: | Mejoras de rendimiento
style: | Cambios de estilo y formato (naming, indentación, etc.)
security: | Cambios relacionados con la seguridad del sistema
deps: | Actualización de dependencias del proyecto
remove: | Eliminación de código, archivos o funcionalidades obsoletas
ci: | Configuración o cambios relacionados al sistema de CI/CD


- Se trabaja con rama `DEV`, luego se mergea a `main`. El CI/CD corre sobre ambas ramas.

---

## ⚙️ CI/CD con GitHub Actions

Archivo `.github/workflows/ci.yml`:

- Ejecuta en `push` y `pull_request` sobre `main` y `DEV`.
- Instala Java 17.
- Compila, lanza tests, y verifica Jacoco.
- Falla si la cobertura es menor al 80%.

```yaml
  - name: 📊 Subir reporte Jacoco
    if: success()
    uses: actions/upload-artifact@v3
    with:
      name: jacoco-report
      path: target/site/jacoco
```

---

## 🧩 Flujo completo de ejecución (explicación detallada)

1. **El controlador** (`PriceController`) expone un endpoint `/api/prices` que espera tres parámetros: `date`, `productId`, `brandId`.
   - Aquí decidimos usar `@RequestParam` porque se trata de una operación de consulta clara (GET), sin necesidad de usar un body.

2. **Validación automática** con `@DateTimeFormat` asegura que la fecha se recibe con formato ISO. Si no, el `GlobalExceptionHandler` se activa.

3. El **caso de uso** (`GetApplicablePriceUseCase`) orquesta la lógica:
   - Recibe parámetros limpios del controlador.
   - Llama a `PriceRepository` para obtener la tarifa más prioritaria que cumpla con la fecha, producto y marca.
   - Si no encuentra nada, lanza una excepción customizada.

4. El resultado es una instancia de `Price` (modelo del dominio), **que no se expone directamente**.
   - Se transforma en un **DTO** (`PriceResponseDto`) para exponer solo los datos necesarios (ocultando el `id`, `priority`, etc.).
   - Esto sigue el principio de **encapsulamiento** y mejora la claridad del contrato REST.

5. La respuesta HTTP contiene:
   - Código `200 OK`.
   - JSON con producto, marca, precio, moneda, fechas y tarifa.

6. En caso de error:
   - `PriceNotFoundException` → 404 con mensaje detallado.
   - `IllegalArgumentException`, `MethodArgumentTypeMismatchException` → 400 con mensaje amigable.
   - Otros errores caen en el `RuntimeException` y se manejan como `500`.

7. **Logging**:
   - Se utilizan `log.info` y `log.warn` en puntos clave para trazabilidad (puede integrarse con herramientas de métricas).

---

## 🧠 Posibles mejoras futuras

- Implementar autenticación JWT.
- Exponer métricas vía Actuator + Prometheus.
- Probar integración con Kafka si el flujo lo requiriera.
- Agregar tests de carga (con JMeter o Gatling).
- Versionado del endpoint (`/api/v1/prices`).

---

## ✅ Conclusión

Esta solución está alineada con todos los criterios técnicos solicitados:

✔️ Arquitectura limpia y desacoplada (DDD + Hexagonal)
✔️ Buenas prácticas (SOLID, Clean Code)
✔️ Testing completo y cobertura al 100%
✔️ Manejo robusto de errores
✔️ CI/CD listo para producción
✔️ Código mantenible, extensible y profesional

---

Gracias por la oportunidad de desarrollar esta prueba 🙌

> _“Clean code always looks like it was written by someone who cares.” – Robert C. Martin_

