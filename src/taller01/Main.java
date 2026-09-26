// Renato Martínez 21.776.430-0 ITI
// Alonso Arriagada 21.803.339-3 ITI
package taller01;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    static final int CAPACIDAD_MAXIMA = 100;

    static final String ARCHIVO_ALUMNOS = "Alumnos.txt";

    static final String ARCHIVO_SOLICITUDES = "Solicitudes.txt";

    static final String CARPETA_REPORTES = "Reportes";

    static String[] nombreAlumno = new String[CAPACIDAD_MAXIMA];
    static String[] apellidoAlumno = new String[CAPACIDAD_MAXIMA];
    static String[] rutAlumno = new String[CAPACIDAD_MAXIMA];
    static String[] paraleloAlumno = new String[CAPACIDAD_MAXIMA];
    static int cantidadAlumnos = 0;

    static String[] nombreSolicitud = new String[CAPACIDAD_MAXIMA];
    static String[] apellidoSolicitud = new String[CAPACIDAD_MAXIMA];
    static int cantidadSolicitudes = 0;

    static String[] nombreMiembro = new String[CAPACIDAD_MAXIMA];
    static String[] apellidoMiembro = new String[CAPACIDAD_MAXIMA];
    static String[] rutMiembro = new String[CAPACIDAD_MAXIMA];
    static String[] paraleloMiembro = new String[CAPACIDAD_MAXIMA];
    static String[] origenMiembro = new String[CAPACIDAD_MAXIMA];
    static int cantidadMiembros = 0;

    static String[] nombreRechazado = new String[CAPACIDAD_MAXIMA];
    static String[] apellidoRechazado = new String[CAPACIDAD_MAXIMA];
    static String[] rutRechazado = new String[CAPACIDAD_MAXIMA];
    static boolean[] soloRutRechazado = new boolean[CAPACIDAD_MAXIMA];
    static int cantidadRechazados = 0;

    static boolean alumnosCargados = false;
    static boolean solicitudesCargadas = false;
    static boolean solicitudesYaProcesadas = false;

    static int intentosManuales = 0;

    static int miembrosPorManual = 0;
    static int miembrosPorArchivo = 0;

    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            try {
                mostrarMenuPrincipal();
                int opcion = leerEntero();

                switch (opcion) {
                    case 1:
                        cargarArchivos();
                        break;
                    case 2:
                        procesarSolicitudes();
                        break;
                    case 3:
                        inscripcionManual();
                        break;
                    case 4:
                        administracionCurso();
                        break;
                    case 5:
                        generarReportes();
                        break;
                    case 6:
                        analisisEstadistico();
                        break;
                    case 7:
                        System.out.println("Saliendo del sistema. Hasta luego!");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opcion invalida. Ingrese un numero entre 1 y 7.");
                }
            } catch (NoSuchElementException e) {

                System.out.println("No se detecto mas entrada de datos. Cerrando el sistema.");
                continuar = false;
            } catch (Exception e) {

                System.out.println("Ocurrio un error inesperado (" + e.getMessage()
                        + "). Se volvera al menu principal.");
            }
        }

        teclado.close();
    }

    static void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("===== Sistema de Control del Grupo POO =====");
        System.out.println("1) Cargar archivos (Alumnos y Solicitudes)");
        System.out.println("2) Procesar solicitudes (Filtrado automatico)");
        System.out.println("3) Inscripcion manual al grupo");
        System.out.println("4) Administracion del curso");
        System.out.println("5) Generar reportes");
        System.out.println("6) Analisis estadistico");
        System.out.println("7) Salir");
        System.out.print("Ingrese opcion: ");
    }

    static int leerEntero() {
        String linea = teclado.nextLine();
        try {
            return Integer.parseInt(linea.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static String leerTexto() {
        String linea = teclado.nextLine();
        return linea == null ? "" : linea.trim();
    }

    static void cargarArchivos() {
        cantidadAlumnos = 0;
        cantidadSolicitudes = 0;
        alumnosCargados = false;
        solicitudesCargadas = false;
        solicitudesYaProcesadas = false;

        try (Scanner lectorAlumnos = new Scanner(new File(ARCHIVO_ALUMNOS))) {
            while (lectorAlumnos.hasNextLine() && cantidadAlumnos < CAPACIDAD_MAXIMA) {
                String linea = lectorAlumnos.nextLine().trim();
                if (linea.isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(";");
                if (partes.length != 4) {
                    System.out.println("Linea con formato invalido en " + ARCHIVO_ALUMNOS
                            + ", se omite: " + linea);
                    continue;
                }
                String paralelo = partes[3].trim().toUpperCase();
                if (!paraleloValido(paralelo)) {
                    System.out.println("Paralelo invalido en " + ARCHIVO_ALUMNOS
                            + ", se omite linea: " + linea);
                    continue;
                }
                nombreAlumno[cantidadAlumnos] = partes[0].trim();
                apellidoAlumno[cantidadAlumnos] = partes[1].trim();
                rutAlumno[cantidadAlumnos] = partes[2].trim();
                paraleloAlumno[cantidadAlumnos] = paralelo;
                cantidadAlumnos++;
            }
            if (cantidadAlumnos >= CAPACIDAD_MAXIMA) {
                System.out.println("Aviso: se alcanzo la capacidad maxima de " + CAPACIDAD_MAXIMA
                        + " alumnos, algunas lineas podrian no haberse cargado.");
            }
            alumnosCargados = true;
        } catch (IOException e) {
            System.out.println("No se encontro el archivo " + ARCHIVO_ALUMNOS + ". No se cargaron alumnos.");
        }

        try (Scanner lectorSolicitudes = new Scanner(new File(ARCHIVO_SOLICITUDES))) {
            while (lectorSolicitudes.hasNextLine() && cantidadSolicitudes < CAPACIDAD_MAXIMA) {
                String linea = lectorSolicitudes.nextLine().trim();
                if (linea.isEmpty()) {
                    continue;
                }
                String[] partes = linea.split("-", 2);
                if (partes.length != 2) {
                    System.out.println("Linea con formato invalido en " + ARCHIVO_SOLICITUDES
                            + ", se omite: " + linea);
                    continue;
                }
                nombreSolicitud[cantidadSolicitudes] = partes[0].trim();
                apellidoSolicitud[cantidadSolicitudes] = partes[1].trim();
                cantidadSolicitudes++;
            }
            if (cantidadSolicitudes >= CAPACIDAD_MAXIMA) {
                System.out.println("Aviso: se alcanzo la capacidad maxima de " + CAPACIDAD_MAXIMA
                        + " solicitudes, algunas lineas podrian no haberse cargado.");
            }
            solicitudesCargadas = true;
        } catch (IOException e) {
            System.out.println("No se encontro el archivo " + ARCHIVO_SOLICITUDES + ". No se cargaron solicitudes.");
        }

        System.out.println();
        System.out.println("Archivos cargados con exito!");
        System.out.println("- " + cantidadAlumnos + " alumnos en la lista.");
        System.out.println("- " + cantidadSolicitudes + " solicitudes de ingreso.");
    }

    static void procesarSolicitudes() {
        if (!solicitudesCargadas || !alumnosCargados) {
            System.out.println("Debe cargar los archivos (opcion 1) antes de procesar solicitudes.");
            return;
        }
        if (cantidadSolicitudes == 0) {
            System.out.println("No hay solicitudes cargadas para procesar.");
            return;
        }

        System.out.println("Procesando solicitudes...");
        System.out.println();

        int admitidosEnEsteProceso = 0;
        int rechazadosEnEsteProceso = 0;

        for (int i = 0; i < cantidadSolicitudes; i++) {
            String nombre = nombreSolicitud[i];
            String apellido = apellidoSolicitud[i];

            int indiceAlumno = buscarAlumnoPorNombre(nombre, apellido);

            if (indiceAlumno != -1) {
                String rut = rutAlumno[indiceAlumno];
                if (buscarMiembroPorRut(rut) != -1) {

                    continue;
                }
                boolean agregado = agregarMiembro(nombreAlumno[indiceAlumno], apellidoAlumno[indiceAlumno],
                        rut, paraleloAlumno[indiceAlumno], "Archivo");
                if (agregado) {
                    System.out.println("[OK]       " + nombre + " " + apellido + " -> admitido en "
                            + paraleloAlumno[indiceAlumno]);
                    admitidosEnEsteProceso++;
                } else {
                    System.out.println("[ERROR]    No hay espacio para admitir a " + nombre + " " + apellido);
                }
            } else {
                if (buscarRechazadoPorNombre(nombre, apellido) != -1) {

                    continue;
                }
                boolean agregado = agregarRechazadoPorNombre(nombre, apellido);
                if (agregado) {
                    System.out.println("[RECHAZO]  " + nombre + " " + apellido + " -> no pertenece a ningun paralelo");
                    rechazadosEnEsteProceso++;
                } else {
                    System.out.println("[ERROR]    No hay espacio para registrar el rechazo de " + nombre + " " + apellido);
                }
            }
        }

        solicitudesYaProcesadas = true;
        System.out.println();
        System.out.println("Resumen: " + admitidosEnEsteProceso + " admitidos / " + rechazadosEnEsteProceso + " rechazados.");
    }

    static void inscripcionManual() {
        if (!alumnosCargados) {
            System.out.println("Debe cargar los archivos (opcion 1) antes de inscribir manualmente.");
            return;
        }

        System.out.println("Como desea inscribir a la persona?");
        System.out.println("1) Por nombre completo");
        System.out.println("2) Por RUT");
        System.out.print("Ingrese opcion: ");
        int opcion = leerEntero();

        if (opcion == 1) {
            inscribirPorNombre();
        } else if (opcion == 2) {
            inscribirPorRut();
        } else {
            System.out.println("Opcion invalida.");
        }
    }

    static void inscribirPorNombre() {
        System.out.print("Ingrese nombre: ");
        String nombre = leerTexto();
        System.out.print("Ingrese apellido: ");
        String apellido = leerTexto();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            System.out.println("El nombre y el apellido no pueden estar en blanco.");
            return;
        }

        intentosManuales++;
        int indiceAlumno = buscarAlumnoPorNombre(nombre, apellido);

        if (indiceAlumno == -1) {
            System.out.println(nombre + " " + apellido + " no pertenece a ningun paralelo del curso.");
            if (buscarRechazadoPorNombre(nombre, apellido) == -1) {
                if (!agregarRechazadoPorNombre(nombre, apellido)) {
                    System.out.println("Aviso: no hay espacio disponible para registrar mas rechazados.");
                }
            }
            return;
        }

        String rut = rutAlumno[indiceAlumno];
        if (buscarMiembroPorRut(rut) != -1) {
            System.out.println(nombre + " " + apellido + " ya es miembro del grupo.");
            return;
        }

        boolean agregado = agregarMiembro(nombreAlumno[indiceAlumno], apellidoAlumno[indiceAlumno],
                rut, paraleloAlumno[indiceAlumno], "Manual");
        if (agregado) {
            System.out.println(nombre + " " + apellido + " fue inscrito correctamente en "
                    + paraleloAlumno[indiceAlumno] + ".");
        } else {
            System.out.println("No hay espacio disponible para inscribir mas miembros.");
        }
    }

    static void inscribirPorRut() {
        System.out.print("Ingrese RUT: ");
        String rut = leerTexto();

        if (rut.isEmpty()) {
            System.out.println("El RUT no puede estar en blanco.");
            return;
        }

        intentosManuales++;
        int indiceAlumno = buscarAlumnoPorRut(rut);

        if (indiceAlumno == -1) {
            System.out.println("El RUT " + rut + " no pertenece a ningun paralelo del curso.");
            System.out.println("No tenemos su nombre, por lo que se registrara solo el RUT en los rechazados.");
            if (buscarRechazadoSoloRut(rut) == -1) {
                if (!agregarRechazadoSoloRut(rut)) {
                    System.out.println("Aviso: no hay espacio disponible para registrar mas rechazados.");
                }
            }
            return;
        }

        if (buscarMiembroPorRut(rut) != -1) {
            System.out.println(nombreAlumno[indiceAlumno] + " " + apellidoAlumno[indiceAlumno]
                    + " ya es miembro del grupo.");
            return;
        }

        boolean agregado = agregarMiembro(nombreAlumno[indiceAlumno], apellidoAlumno[indiceAlumno],
                rut, paraleloAlumno[indiceAlumno], "Manual");
        if (agregado) {
            System.out.println(nombreAlumno[indiceAlumno] + " " + apellidoAlumno[indiceAlumno]
                    + " fue inscrito correctamente en " + paraleloAlumno[indiceAlumno] + ".");
        } else {
            System.out.println("No hay espacio disponible para inscribir mas miembros.");
        }
    }

    static void administracionCurso() {
        if (!alumnosCargados) {
            System.out.println("Debe cargar los archivos (opcion 1) antes de administrar el curso.");
            return;
        }

        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Administracion del curso ---");
            System.out.println("1) Cambiar paralelo de un alumno");
            System.out.println("2) Eliminar alumno del curso");
            System.out.println("3) Inscribir alumno nuevo");
            System.out.println("4) Volver");
            System.out.print("Ingrese opcion: ");
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    cambiarParaleloAlumno();
                    break;
                case 2:
                    eliminarAlumno();
                    break;
                case 3:
                    inscribirAlumnoNuevo();
                    break;
                case 4:
                    volver = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    static void cambiarParaleloAlumno() {
        System.out.print("Ingrese RUT del alumno: ");
        String rut = leerTexto();

        if (rut.isEmpty()) {
            System.out.println("El RUT no puede estar en blanco.");
            return;
        }

        int indice = buscarAlumnoPorRut(rut);
        if (indice == -1) {
            System.out.println("No existe ningun alumno con el RUT " + rut + ".");
            return;
        }

        System.out.println("Alumno: " + nombreAlumno[indice] + " " + apellidoAlumno[indice]
                + " (actualmente en " + paraleloAlumno[indice] + ")");
        System.out.print("Nuevo paralelo (C1/C2): ");
        String nuevoParalelo = leerTexto().toUpperCase();

        if (!paraleloValido(nuevoParalelo)) {
            System.out.println("Paralelo invalido. Debe ser C1 o C2.");
            return;
        }

        paraleloAlumno[indice] = nuevoParalelo;

        int indiceMiembro = buscarMiembroPorRut(rut);
        if (indiceMiembro != -1) {
            paraleloMiembro[indiceMiembro] = nuevoParalelo;
        }

        guardarAlumnosEnArchivo();
        System.out.println("Paralelo actualizado! Cambios guardados en " + ARCHIVO_ALUMNOS);
    }

    static void eliminarAlumno() {
        System.out.print("Ingrese RUT del alumno a eliminar: ");
        String rut = leerTexto();

        if (rut.isEmpty()) {
            System.out.println("El RUT no puede estar en blanco.");
            return;
        }

        int indice = buscarAlumnoPorRut(rut);
        if (indice == -1) {
            System.out.println("No existe ningun alumno con el RUT " + rut + ".");
            return;
        }

        String nombreCompleto = nombreAlumno[indice] + " " + apellidoAlumno[indice];

        nombreAlumno[indice] = "";
        apellidoAlumno[indice] = "";
        rutAlumno[indice] = "";
        paraleloAlumno[indice] = "";

        int indiceMiembro = buscarMiembroPorRut(rut);
        if (indiceMiembro != -1) {
            nombreMiembro[indiceMiembro] = "";
            apellidoMiembro[indiceMiembro] = "";
            rutMiembro[indiceMiembro] = "";
            paraleloMiembro[indiceMiembro] = "";
            origenMiembro[indiceMiembro] = "";
        }

        guardarAlumnosEnArchivo();
        System.out.println(nombreCompleto + " fue eliminado del curso. Cambios guardados en " + ARCHIVO_ALUMNOS);
    }

    static void inscribirAlumnoNuevo() {
        if (cantidadAlumnos >= CAPACIDAD_MAXIMA) {
            System.out.println("No hay espacio disponible: se alcanzo el maximo de " + CAPACIDAD_MAXIMA + " alumnos.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = leerTexto();
        System.out.print("Apellido: ");
        String apellido = leerTexto();
        System.out.print("RUT: ");
        String rut = leerTexto();
        System.out.print("Paralelo (C1/C2): ");
        String paralelo = leerTexto().toUpperCase();

        if (nombre.isEmpty() || apellido.isEmpty() || rut.isEmpty()) {
            System.out.println("Nombre, apellido y RUT no pueden quedar en blanco.");
            return;
        }
        if (!paraleloValido(paralelo)) {
            System.out.println("Paralelo invalido. Debe ser C1 o C2.");
            return;
        }
        if (buscarAlumnoPorRut(rut) != -1) {
            System.out.println("Ya existe un alumno inscrito con el RUT " + rut + ".");
            return;
        }

        nombreAlumno[cantidadAlumnos] = nombre;
        apellidoAlumno[cantidadAlumnos] = apellido;
        rutAlumno[cantidadAlumnos] = rut;
        paraleloAlumno[cantidadAlumnos] = paralelo;
        cantidadAlumnos++;

        guardarAlumnosEnArchivo();
        System.out.println(nombre + " " + apellido + " fue inscrito en la lista del curso (paralelo " + paralelo + ").");
        System.out.println("Nota: aun no es miembro del grupo, debe procesarse o inscribirse manualmente.");
    }

    static void guardarAlumnosEnArchivo() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO_ALUMNOS))) {
            for (int i = 0; i < cantidadAlumnos; i++) {
                if (rutAlumno[i] == null || rutAlumno[i].isEmpty()) {
                    continue;
                }
                escritor.write(nombreAlumno[i] + ";" + apellidoAlumno[i] + ";" + rutAlumno[i] + ";" + paraleloAlumno[i]);
                escritor.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar " + ARCHIVO_ALUMNOS + ": " + e.getMessage());
        }
    }

    static void generarReportes() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Generar reportes ---");
            System.out.println("1) Reporte paralelo C1");
            System.out.println("2) Reporte paralelo C2");
            System.out.println("3) Reporte de rechazados");
            System.out.println("4) Volver");
            System.out.print("Ingrese opcion: ");
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    generarReporteParalelo("C1");
                    break;
                case 2:
                    generarReporteParalelo("C2");
                    break;
                case 3:
                    generarReporteRechazados();
                    break;
                case 4:
                    volver = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    static void generarReporteParalelo(String paralelo) {
        File carpeta = new File(CARPETA_REPORTES);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        String prefijo = "Reporte" + paralelo;
        int version = obtenerSiguienteVersion(prefijo);
        String rutaArchivo = CARPETA_REPORTES + File.separator + prefijo + "-V" + version + ".txt";

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            escritor.write("=== Miembros del grupo - Paralelo " + paralelo + " ===");
            escritor.newLine();
            for (int i = 0; i < cantidadMiembros; i++) {
                if (rutMiembro[i] == null || rutMiembro[i].isEmpty()) {
                    continue;
                }
                if (paraleloMiembro[i].equalsIgnoreCase(paralelo)) {
                    escritor.write(nombreMiembro[i] + " " + apellidoMiembro[i] + " - " + rutMiembro[i]);
                    escritor.newLine();
                }
            }
            System.out.println("Reporte generado: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    static void generarReporteRechazados() {
        File carpeta = new File(CARPETA_REPORTES);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        int version = obtenerSiguienteVersion("Rechazados");
        String rutaArchivo = CARPETA_REPORTES + File.separator + "Rechazados-V" + version + ".txt";

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            escritor.write("=== Solicitudes rechazadas ===");
            escritor.newLine();
            for (int i = 0; i < cantidadRechazados; i++) {
                if (soloRutRechazado[i]) {
                    escritor.write("Sin nombre registrado, RUT: " + rutRechazado[i]);
                } else {
                    escritor.write(nombreRechazado[i] + " " + apellidoRechazado[i]
                            + " - No pertenece a ningun paralelo del curso");
                }
                escritor.newLine();
            }
            System.out.println("Reporte generado: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    static int obtenerSiguienteVersion(String prefijo) {
        int version = 1;
        File archivo = new File(CARPETA_REPORTES + File.separator + prefijo + "-V" + version + ".txt");
        while (archivo.exists()) {
            version++;
            archivo = new File(CARPETA_REPORTES + File.separator + prefijo + "-V" + version + ".txt");
        }
        return version;
    }

    static void analisisEstadistico() {
        int totalIntentos = cantidadSolicitudes + intentosManuales;

        System.out.println();
        System.out.println("--- Analisis estadistico ---");

        if (totalIntentos == 0) {
            System.out.println("Aun no hay intentos de ingreso registrados (cargue archivos y/o procese solicitudes).");
            return;
        }

        double porcentajeRechazo = (cantidadRechazados * 100.0) / totalIntentos;
        double tasaAdmision = (cantidadMiembros * 100.0) / totalIntentos;

        System.out.println("Total de intentos de ingreso: " + totalIntentos);
        System.out.println("Rechazados: " + cantidadRechazados + " (" + formatearPorcentaje(porcentajeRechazo) + "%)");
        System.out.println("Admitidos: " + cantidadMiembros + " (" + formatearPorcentaje(tasaAdmision) + "%)");

        int miembrosC1 = 0;
        int miembrosC2 = 0;
        for (int i = 0; i < cantidadMiembros; i++) {
            if (rutMiembro[i] == null || rutMiembro[i].isEmpty()) {
                continue;
            }
            if (paraleloMiembro[i].equalsIgnoreCase("C1")) {
                miembrosC1++;
            } else if (paraleloMiembro[i].equalsIgnoreCase("C2")) {
                miembrosC2++;
            }
        }
        System.out.println("Admitidos por paralelo -> C1: " + miembrosC1 + " | C2: " + miembrosC2);

        int alumnosC1 = 0;
        int alumnosC2 = 0;
        int alumnosActivos = 0;
        for (int i = 0; i < cantidadAlumnos; i++) {
            if (rutAlumno[i] == null || rutAlumno[i].isEmpty()) {
                continue;
            }
            alumnosActivos++;
            if (paraleloAlumno[i].equalsIgnoreCase("C1")) {
                alumnosC1++;
            } else if (paraleloAlumno[i].equalsIgnoreCase("C2")) {
                alumnosC2++;
            }
        }
        if (alumnosActivos > 0) {
            double pctC1 = (alumnosC1 * 100.0) / alumnosActivos;
            double pctC2 = (alumnosC2 * 100.0) / alumnosActivos;
            System.out.println("Alumnos del curso -> C1: " + alumnosC1 + " (" + formatearPorcentaje(pctC1)
                    + "%) | C2: " + alumnosC2 + " (" + formatearPorcentaje(pctC2) + "%)");
        }

        int soloRut = 0;
        for (int i = 0; i < cantidadRechazados; i++) {
            if (soloRutRechazado[i]) {
                soloRut++;
            }
        }
        System.out.println("Rechazados de los que solo se tiene el RUT: " + soloRut);

        System.out.println("Miembros admitidos por archivo: " + miembrosPorArchivo
                + " | por inscripcion manual: " + miembrosPorManual);

        int duplicadas = contarSolicitudesDuplicadas();
        System.out.println("Solicitudes duplicadas (repeticiones de una misma persona): " + duplicadas);

        String apellidoTop = apellidoMasRepetido();
        if (apellidoTop != null) {
            System.out.println("Apellido mas repetido en la lista de alumnos: " + apellidoTop);
        }
    }

    static String formatearPorcentaje(double valor) {

        return String.format(Locale.US, "%.1f", valor);
    }

    static int contarSolicitudesDuplicadas() {
        int duplicadas = 0;
        for (int i = 0; i < cantidadSolicitudes; i++) {
            for (int j = 0; j < i; j++) {
                if (nombreSolicitud[i].equalsIgnoreCase(nombreSolicitud[j])
                        && apellidoSolicitud[i].equalsIgnoreCase(apellidoSolicitud[j])) {
                    duplicadas++;
                    break;
                }
            }
        }
        return duplicadas;
    }

    static String apellidoMasRepetido() {
        String[] apellidosUnicos = new String[CAPACIDAD_MAXIMA];
        int[] conteos = new int[CAPACIDAD_MAXIMA];
        int cantidadUnicos = 0;

        for (int i = 0; i < cantidadAlumnos; i++) {
            if (apellidoAlumno[i] == null || apellidoAlumno[i].isEmpty()) {
                continue;
            }
            int encontrado = -1;
            for (int j = 0; j < cantidadUnicos; j++) {
                if (apellidosUnicos[j].equalsIgnoreCase(apellidoAlumno[i])) {
                    encontrado = j;
                    break;
                }
            }
            if (encontrado == -1) {
                apellidosUnicos[cantidadUnicos] = apellidoAlumno[i];
                conteos[cantidadUnicos] = 1;
                cantidadUnicos++;
            } else {
                conteos[encontrado]++;
            }
        }

        if (cantidadUnicos == 0) {
            return null;
        }

        int indiceMax = 0;
        for (int j = 1; j < cantidadUnicos; j++) {
            if (conteos[j] > conteos[indiceMax]) {
                indiceMax = j;
            }
        }
        return apellidosUnicos[indiceMax] + " (" + conteos[indiceMax] + " veces)";
    }

    static int buscarAlumnoPorNombre(String nombre, String apellido) {
        for (int i = 0; i < cantidadAlumnos; i++) {
            if (rutAlumno[i] == null || rutAlumno[i].isEmpty()) {
                continue;
            }
            if (nombreAlumno[i].equalsIgnoreCase(nombre) && apellidoAlumno[i].equalsIgnoreCase(apellido)) {
                return i;
            }
        }
        return -1;
    }

    static int buscarAlumnoPorRut(String rut) {
        for (int i = 0; i < cantidadAlumnos; i++) {
            if (rutAlumno[i] == null || rutAlumno[i].isEmpty()) {
                continue;
            }
            if (rutAlumno[i].equalsIgnoreCase(rut)) {
                return i;
            }
        }
        return -1;
    }

    static int buscarMiembroPorRut(String rut) {
        for (int i = 0; i < cantidadMiembros; i++) {
            if (rutMiembro[i] == null || rutMiembro[i].isEmpty()) {
                continue;
            }
            if (rutMiembro[i].equalsIgnoreCase(rut)) {
                return i;
            }
        }
        return -1;
    }

    static int buscarRechazadoPorNombre(String nombre, String apellido) {
        for (int i = 0; i < cantidadRechazados; i++) {
            if (soloRutRechazado[i]) {
                continue;
            }
            if (nombreRechazado[i].equalsIgnoreCase(nombre) && apellidoRechazado[i].equalsIgnoreCase(apellido)) {
                return i;
            }
        }
        return -1;
    }

    static int buscarRechazadoSoloRut(String rut) {
        for (int i = 0; i < cantidadRechazados; i++) {
            if (soloRutRechazado[i] && rutRechazado[i].equalsIgnoreCase(rut)) {
                return i;
            }
        }
        return -1;
    }

    static boolean agregarMiembro(String nombre, String apellido, String rut, String paralelo, String origen) {
        if (cantidadMiembros >= CAPACIDAD_MAXIMA) {
            return false;
        }
        nombreMiembro[cantidadMiembros] = nombre;
        apellidoMiembro[cantidadMiembros] = apellido;
        rutMiembro[cantidadMiembros] = rut;
        paraleloMiembro[cantidadMiembros] = paralelo;
        origenMiembro[cantidadMiembros] = origen;
        cantidadMiembros++;

        if (origen.equals("Manual")) {
            miembrosPorManual++;
        } else {
            miembrosPorArchivo++;
        }
        return true;
    }

    static boolean agregarRechazadoPorNombre(String nombre, String apellido) {
        if (cantidadRechazados >= CAPACIDAD_MAXIMA) {
            return false;
        }
        nombreRechazado[cantidadRechazados] = nombre;
        apellidoRechazado[cantidadRechazados] = apellido;
        rutRechazado[cantidadRechazados] = "";
        soloRutRechazado[cantidadRechazados] = false;
        cantidadRechazados++;
        return true;
    }

    static boolean agregarRechazadoSoloRut(String rut) {
        if (cantidadRechazados >= CAPACIDAD_MAXIMA) {
            return false;
        }
        nombreRechazado[cantidadRechazados] = "";
        apellidoRechazado[cantidadRechazados] = "";
        rutRechazado[cantidadRechazados] = rut;
        soloRutRechazado[cantidadRechazados] = true;
        cantidadRechazados++;
        return true;
    }

    static boolean paraleloValido(String paralelo) {
        return "C1".equalsIgnoreCase(paralelo) || "C2".equalsIgnoreCase(paralelo);
    }
}