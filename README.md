# Taller 01 POO - Sistema de Control del Grupo POO

## Integrantes

- Renato Martínez - 21.776.430-0 - ITI - [@renatomartinez101](https://github.com/renatomartinez101)
- Alonso Arriagada - 21.803.339-3 - ITI - [@cyphrrql](https://github.com/cyphrrql)

## Descripción

Programa en Java (sin POO, usando vectores estáticos) que administra las solicitudes de ingreso al grupo de WhatsApp de POO, cruzando la lista de alumnos del curso con las solicitudes recibidas, generando reportes y estadísticas del proceso.

## Estructura del proyecto

- `taller01/` — único paquete del proyecto.
  - `Main.java` — clase única con todo el sistema: carga de archivos, procesamiento de solicitudes, inscripción manual, administración del curso, generación de reportes y análisis estadístico.
- `Alumnos.txt` — lista de alumnos del curso (entrada/persistencia).
- `Solicitudes.txt` — solicitudes de ingreso recibidas (entrada).
- `Reportes/` — carpeta generada automáticamente por el programa con los reportes versionados.

## Cómo clonar el repositorio

```bash
git clone https://github.com/cyphrrql/Taller-01-POO.git
cd Taller-01-POO
```

## Cómo ejecutar el programa

### Opción 1: Desde la terminal

Asegúrate de tener Java instalado (`java -version`), y de que `Alumnos.txt` y `Solicitudes.txt` estén en la misma carpeta desde donde ejecutas el programa.

```bash
javac -d bin src/taller01/Main.java
java -cp bin taller01.Main
```

### Opción 2: Desde Eclipse

1. File > Import > General > Existing Projects into Workspace.
2. Selecciona la carpeta del repositorio clonado.
3. Click derecho en `Main.java` > Run As > Java Application.

## Archivos necesarios

- `Alumnos.txt`: lista de alumnos del curso, formato `nombre;apellido;rut;paralelo`.
- `Solicitudes.txt`: solicitudes de ingreso recibidas, formato `nombre-apellido`.

Ambos deben estar en la carpeta raíz del proyecto (junto a donde se ejecuta el programa) para que el sistema los pueda leer.

## Funcionalidades

1. Cargar archivos (Alumnos y Solicitudes)
2. Procesar solicitudes (filtrado automático)
3. Inscripción manual al grupo
4. Administración del curso (cambiar paralelo, eliminar o inscribir alumnos)
5. Generar reportes (por paralelo y de rechazados, versionados en la carpeta `Reportes/`)
6. Análisis estadístico
