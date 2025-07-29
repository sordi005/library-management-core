# 📚 Library Management System

> **Sistema de gestión bibliotecaria con arquitectura hexagonal y API REST nativa**

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-blue.svg)](https://gradle.org/)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.4.4-green.svg)](https://hibernate.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

## 🎯 **Descripción**

Sistema de gestión bibliotecaria desarrollado con **arquitectura hexagonal**, implementando patrones enterprise y buenas prácticas de desarrollo. Construido **sin frameworks** para demostrar conocimientos fundamentales de Java y arquitectura de software.

## 🏗️ **Arquitectura**

```
📁 Clean Architecture / Hexagonal
├── 🎯 Presentation Layer (Controllers)
├── 🔧 Application Layer (Services)  
├── 🎨 Domain Layer (Entities/Models)
├── 💾 Infrastructure Layer (Repositories)
├── 🔀 Mapping Layer (DTOs/Mappers)
└── ⚡ Cross-cutting (Exceptions/Utils)
```

### **Principios Aplicados**
- ✅ **SOLID Principles**
- ✅ **Clean Architecture**
- ✅ **Domain-Driven Design (DDD)**
- ✅ **Repository Pattern**
- ✅ **Dependency Injection**
- ✅ **Exception Handling Strategy**

## 🚀 **Tecnologías**

| Categoría | Tecnología | Propósito |
|-----------|------------|-----------|
| **Core** | Java 21 | Lenguaje base |
| **Build** | Gradle 8.5 | Gestión de dependencias |
| **ORM** | Hibernate 6.4.4 | Mapeo objeto-relacional |
| **Database** | MySQL 8.0 | Base de datos |
| **Validation** | Jakarta Validation | Validación de DTOs |
| **HTTP** | Java Native HTTP Server | API REST |
| **Logging** | Custom Logger | Sistema de logs |
| **Testing** | JUnit 5 | Testing unitario |

## 📊 **Estado del Proyecto**

### ✅ **Completado**
- **Modelos de dominio**: Todas las entidades (User, Book, Loan, etc.)
- **DTOs**: Todos los DTOs para transferencia de datos
- **API Users**: CRUD completo de usuarios
- **Arquitectura base**: Configuración y estructura
- **Manejo de excepciones**: Sistema centralizado
- **Logging**: Sistema profesional implementado

### 🚧 **En Desarrollo**
- APIs para Books, Loans, Authors, etc.
- Sistema de autenticación
- Validaciones avanzadas
- Documentación OpenAPI

## 🛠️ **Instalación y Configuración**

### **Prerrequisitos**
```bash
Java 21+
MySQL 8.0+ (solo el servidor, la BD se crea automáticamente)
Gradle 8.5+
```

### **1. Clonar Repositorio**
```bash
git clone <repository-url>
cd library-management-core
```

### **2. Configurar MySQL**
Solo necesitas tener **MySQL Server** ejecutándose en `localhost:3306` con:
- Usuario: `root`
- Password: `1234`

**¡La base de datos `librarydb` se crea automáticamente!** ✨

### **3. Ejecutar Aplicación**
```bash
./gradlew run
```

**El sistema automáticamente:**
- ✅ Verifica conexión a MySQL
- ✅ Crea la base de datos `librarydb` si no existe
- ✅ Crea todas las tablas automáticamente
- ✅ Carga datos de prueba
- ✅ Inicia el servidor en `http://localhost:8080`

## 📡 **API Endpoints**

### **👥 Users API**

| Método | Endpoint | Descripción | Body |
|--------|----------|-------------|------|
| `GET` | `/api/users` | Listar todos los usuarios | - |
| `GET` | `/api/users/{id}` | Obtener usuario por ID | - |
| `POST` | `/api/users` | Crear nuevo usuario | CreateUserDTO |
| `PUT` | `/api/users/{id}` | Actualizar usuario | UpdateUserDTO |
| `DELETE` | `/api/users/{id}` | Eliminar usuario | - |

### **🏥 Health Check**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/health` | Estado del servicio |

## 📝 **Ejemplos de Uso**

### **Crear Usuario**
```bash
POST /api/users
Content-Type: application/json

{
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@email.com",
  "phoneNumber": "+54911234567",
  "dateOfBirth": "1990-05-15",
  "dni": "12345678",
  "address": {
    "street": "Av. Corrientes",
    "number": "1234",
    "city": "Buenos Aires",
    "province": "CABA",
    "country": "Argentina",
    "postalCode": "C1043"
  }
}
```

### **Actualizar Usuario**
```bash
PUT /api/users/1
Content-Type: application/json

{
  "firstName": "Juan Carlos",
  "lastName": "Pérez",
  "email": "juan.carlos@email.com",
  "phoneNumber": "+54911234567",
  "dateOfBirth": "1990-05-15"
}
```

## 🏛️ **Estructura del Proyecto**

```
src/main/java/com/libraryManagement/
├── 📁 config/                 # Configuraciones
│   ├── db/                    # Configuración JPA
│   ├── server/                # Servidor HTTP
│   ├── dependency/            # Inyección de dependencias
│   └── data/                  # Datos de prueba
├── 📁 model/                  # Entidades de dominio
├── 📁 dto/                    # Data Transfer Objects
├── 📁 mapper/                 # Transformadores DTO ↔ Entity
├── 📁 repository/             # Acceso a datos
├── 📁 service/                # Lógica de negocio
├── 📁 controller/             # Controladores REST
├── 📁 exception/              # Manejo de excepciones
├── 📁 validation/             # Validadores
└── 📁 util/                   # Utilidades
```

## 🎯 **Características Técnicas**

### **🔧 Dependency Injection Manual**
```java
@Component
public class DependencyContainer {
    // Gestión manual de dependencias sin Spring
}
```

### **🛡️ Exception Handling Centralizado**
```java
@GlobalExceptionHandler
public class GlobalExceptionHandler {
    // Manejo centralizado de todas las excepciones
}
```

### **📊 Logging Profesional**
```java
Logger.info("UserService", "Usuario creado exitosamente");
Logger.error("UserService", "Error al crear usuario", exception);
```

### **🎨 Context-Based Mapping**
```java
// Mapeo inteligente según contexto
UserDetailDTO detailDTO = userMapper.toDetailDTO(user, 
    UserMappingContext.withLoans());
```

## 🧪 **Testing**

```bash
# Ejecutar tests
./gradlew test

# Ver reporte de cobertura
./gradlew jacocoTestReport
```

## 📈 **Roadmap**

### **📋 Próximas Funcionalidades**
- [ ] **Books API** - Gestión de libros
- [ ] **Loans API** - Sistema de préstamos
- [ ] **Authors API** - Gestión de autores
- [ ] **Authentication** - JWT Security
- [ ] **Swagger Documentation** - API docs
- [ ] **Docker** - Containerización
- [ ] **Spring Migration** - Migración a Spring Boot

## 👨‍💻 **Autor**

**Santiago Sordi Gil**
- Email: sordisantiago@gmail.com
- LinkedIn: [Tu perfil]
- GitHub: [Tu perfil]

## 📄 **Licencia**

Este proyecto está bajo la licencia MIT - ver [LICENSE.md](LICENSE.md) para detalles.

---

> **"Código limpio no es escrito siguiendo reglas. Es escrito por programadores que se preocupan por hacer que el código sea legible y mantenible."** - Robert C. Martin
