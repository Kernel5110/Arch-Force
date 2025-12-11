# Collaborative Document Editor - Spring Boot REST API

## Descripción del Proyecto

Editor de Documentos Colaborativo implementado con **Spring Boot 3.2** y **Java 17**. El sistema demuestra la aplicación práctica de **8 Patrones de Diseño GoF** organizados en 4 módulos funcionales.

## 🏗️ Arquitectura - 4 Módulos Funcionales

### Módulo 1: Creación de Documentos
**Patrones Implementados:**
- ✅ **Factory Method**: Creación de diferentes tipos de elementos (párrafos, imágenes, tablas, listas)
- ✅ **Builder**: Construcción paso a paso de documentos complejos

**Endpoints:**
- `POST /api/documents/create` - Crear un nuevo documento
- `POST /api/documents/add-element` - Agregar elementos al documento
- `GET /api/documents/{id}` - Obtener un documento

### Módulo 2: Estructura y Estilos
**Patrones Implementados:**
- ✅ **Composite**: Jerarquía de documentos (secciones, subsecciones, elementos)
- ✅ **Decorator**: Aplicación dinámica de estilos (negrita, cursiva, color, tamaño)

**Endpoints:**
- `GET /api/documents/structure` - Obtener estructura jerárquica
- `POST /api/styles/apply` - Aplicar estilos a elementos

### Módulo 3: Edición y Versionado
**Patrones Implementados:**
- ✅ **Command**: Operaciones de deshacer/rehacer
- ✅ **Memento**: Guardar y restaurar versiones del documento

**Endpoints:**
- `POST /api/documents/undo` - Deshacer última operación
- `POST /api/documents/redo` - Rehacer operación
- `GET /api/versions/list` - Listar todas las versiones
- `POST /api/versions/create` - Crear nueva versión
- `POST /api/versions/restore` - Restaurar una versión específica

### Módulo 4: Colaboración y Exportación
**Patrones Implementados:**
- ✅ **Observer**: Notificación de cambios a colaboradores
- ✅ **Strategy**: Exportación a diferentes formatos (PDF, HTML, Markdown)

**Endpoints:**
- `POST /api/collaborators/add` - Agregar colaborador
- `GET /api/collaborators/list` - Listar colaboradores
- `POST /api/export/document` - Exportar documento
- `GET /api/export/formats` - Obtener formatos disponibles

## 🚀 Tecnologías

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Maven**: Gestión de dependencias
- **Lombok**: Reducción de código boilerplate
- **Jackson**: Serialización JSON

## 📦 Estructura del Proyecto

```
src/main/java/com/collaborativeeditor/
├── CollaborativeEditorApplication.java  # Clase principal
├── controller/                          # Controladores REST
│   ├── CreationController.java
│   ├── StructureController.java
│   ├── VersioningController.java
│   └── CollaborationController.java
├── dto/                                 # Data Transfer Objects
├── service/                             # Servicios de negocio
├── exception/                           # Manejo global de excepciones
├── module1/creation/                    # Factory Method + Builder
│   ├── factory/
│   ├── builder/
│   └── model/
├── module2/structure/                   # Composite + Decorator
│   ├── composite/
│   └── decorator/
├── module3/versioning/                  # Command + Memento
│   ├── command/
│   └── memento/
└── module4/collaboration/               # Observer + Strategy
    ├── observer/
    └── strategy/
```

## 🔧 Instalación y Ejecución

### Requisitos Previos
- Java 17 o superior
- Maven 3.6+

### Compilar el Proyecto
```bash
mvn clean compile
```

### Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### Empaquetar como JAR
```bash
mvn clean package
java -jar target/collaborative-document-editor-1.0.0.jar
```

## 📝 Ejemplos de Uso

### 1. Crear un Documento (Builder Pattern)
```bash
curl -X POST http://localhost:8080/api/documents/create \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi Documento",
    "author": "Juan Pérez",
    "metadata": "Documento de ejemplo"
  }'
```

### 2. Agregar Elemento (Factory Method Pattern)
```bash
curl -X POST http://localhost:8080/api/documents/add-element \
  -H "Content-Type: application/json" \
  -d '{
    "documentId": "abc-123",
    "elementType": "paragraph",
    "elementData": {
      "content": "Este es un párrafo de ejemplo"
    }
  }'
```

### 3. Aplicar Estilos (Decorator Pattern)
```bash
curl -X POST http://localhost:8080/api/styles/apply \
  -H "Content-Type: application/json" \
  -d '{
    "documentId": "abc-123",
    "elementIndex": 0,
    "styles": ["bold", "italic", "color:red"]
  }'
```

### 4. Crear Versión (Memento Pattern)
```bash
curl -X POST "http://localhost:8080/api/versions/create?documentId=abc-123&versionName=v1.0"
```

### 5. Agregar Colaborador (Observer Pattern)
```bash
curl -X POST http://localhost:8080/api/collaborators/add \
  -H "Content-Type: application/json" \
  -d '{
    "documentId": "abc-123",
    "collaboratorName": "María García",
    "email": "maria@example.com"
  }'
```

### 6. Exportar Documento (Strategy Pattern)
```bash
curl -X POST http://localhost:8080/api/export/document \
  -H "Content-Type: application/json" \
  -d '{
    "documentId": "abc-123",
    "format": "html"
  }'
```

## 🎯 Principios SOLID Aplicados

✅ **Single Responsibility**: Cada clase tiene una única responsabilidad bien definida
✅ **Open/Closed**: Extensible sin modificar código existente (decoradores, estrategias)
✅ **Liskov Substitution**: Las implementaciones son intercambiables
✅ **Interface Segregation**: Interfaces específicas por función
✅ **Dependency Inversion**: Dependencias mediante abstracciones

## 📊 Patrones de Diseño Implementados

| Patrón | Tipo | Módulo | Propósito |
|--------|------|--------|-----------|
| Factory Method | Creacional | 1 | Crear elementos de documento |
| Builder | Creacional | 1 | Construir documentos complejos |
| Composite | Estructural | 2 | Jerarquía de documentos |
| Decorator | Estructural | 2 | Aplicar estilos dinámicos |
| Command | Comportamiento | 3 | Deshacer/Rehacer operaciones |
| Memento | Comportamiento | 3 | Versionado de documentos |
| Observer | Comportamiento | 4 | Notificar colaboradores |
| Strategy | Comportamiento | 4 | Exportar a múltiples formatos |

## 📚 Documentación Adicional

- Todas las clases principales incluyen **JavaDoc** completo
- Los DTOs implementan validación con **Bean Validation**
- Manejo global de excepciones con **@RestControllerAdvice**
- Respuestas JSON consistentes mediante `ApiResponse<T>`

## 🔍 Testing

El proyecto está listo para pruebas con herramientas como:
- **Postman** - Colecciones de pruebas REST
- **curl** - Comandos de línea
- **REST Client** - Extensiones de IDEs

## 👥 Autor

Proyecto creado como demostración de Patrones de Diseño GoF en Spring Boot.

## 📄 Licencia

Este proyecto es de código abierto para fines educativos.
