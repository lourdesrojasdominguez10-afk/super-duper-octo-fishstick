# ✦ LUMINA Studio Creativo — Proyecto Java

## Cómo abrir en IntelliJ IDEA

### Opción A — Abrir el proyecto Maven (recomendado)
1. **Abre IntelliJ IDEA**
2. `File → Open` → selecciona la carpeta **`lumina-app`**
3. IntelliJ detectará el `pom.xml` automáticamente y preguntará si importarlo como proyecto Maven → haz clic en **"Open as Project"**
4. Espera a que sincronice las dependencias (barra de progreso abajo)

### Opción B — Abrir solo el HTML (sin Java)
- Abre IntelliJ → `File → Open` → selecciona **`src/main/resources/static/index.html`**
- Haz clic en el icono del navegador que aparece arriba a la derecha del editor

---

## ▶ Ejecutar el servidor

### Desde IntelliJ
1. Abre `src/main/java/com/lumina/LuminaServer.java`
2. Haz clic en el botón **▶ verde** junto al método `main()`
3. Se abrirá automáticamente tu navegador en **http://localhost:8080**

### Desde terminal (Maven)
```bash
cd lumina-app
mvn compile exec:java
```

---

## Estructura del proyecto
```
lumina-app/
├── pom.xml                              ← Configuración Maven
├── src/
│   └── main/
│       ├── java/com/lumina/
│       │   └── LuminaServer.java        ← Servidor HTTP (JDK built-in)
│       └── resources/
│           └── static/
│               └── index.html           ← App LUMINA completa
└── .idea/
    └── runConfigurations/
        └── LUMINA_Server.xml            ← Config de ejecución para IntelliJ
```

## Notas
- **No requiere dependencias externas** — usa `com.sun.net.httpserver` incluido en el JDK 11+
- Compatible con **Java 11, 17, 21**
- El servidor se abre en el puerto **8080**
