package com.uniajc.servicios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.uniajc.modelo.Inscripcion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias para las reglas de negocio y validaciones de InscripcionService.
 * Se ejecutan de forma aislada sin requerir conexión a base de datos.
 */
public class InscripcionServiceTest {

    private InscripcionService servicio;

    @BeforeEach
    public void setUp() {
        // En pruebas unitarias desacoplamos el DAO para validar la lógica pura
        servicio = new InscripcionService(null);
    }

    @Test
    @DisplayName("Debe lanzar excepción si la inscripción es nula")
    public void testInscripcionNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.validarInscripcion(null, false);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID del estudiante es menor o igual a 0")
    public void testEstudianteInvalido() {
        Inscripcion inscripcion = new Inscripcion(0, 1);
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.validarInscripcion(inscripcion, false);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID del grupo es menor o igual a 0")
    public void testGrupoInvalido() {
        Inscripcion inscripcion = new Inscripcion(1, 0);
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.validarInscripcion(inscripcion, false);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si la nota es mayor a 5.0")
    public void testNotaSuperiorA5() {
        Inscripcion inscripcion = new Inscripcion(1, 1);
        inscripcion.setFinalGrade(5.1);
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.validarInscripcion(inscripcion, false);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si la nota es menor a 0.0")
    public void testNotaMenorA0() {
        Inscripcion inscripcion = new Inscripcion(1, 1);
        inscripcion.setFinalGrade(-0.5);
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.validarInscripcion(inscripcion, false);
        });
    }

    @Test
    @DisplayName("Debe calcular automáticamente estado APROBADO si nota >= 3.0")
    public void testCalculoAutomaticoAprobado() {
        Inscripcion inscripcion = new Inscripcion(1, 1);
        inscripcion.setFinalGrade(3.5);
        inscripcion.setStatus("ACTIVO");

        assertDoesNotThrow(() -> {
            servicio.validarInscripcion(inscripcion, true);
        });

        assertEquals("APROBADO", inscripcion.getStatus(), "La nota de 3.5 debe asignar automáticamente el estado APROBADO.");
    }

    @Test
    @DisplayName("Debe calcular automáticamente estado REPROBADO si nota < 3.0")
    public void testCalculoAutomaticoReprobado() {
        Inscripcion inscripcion = new Inscripcion(1, 1);
        inscripcion.setFinalGrade(2.8);
        inscripcion.setStatus("ACTIVO");

        assertDoesNotThrow(() -> {
            servicio.validarInscripcion(inscripcion, true);
        });

        assertEquals("REPROBADO", inscripcion.getStatus(), "La nota de 2.8 debe asignar automáticamente el estado REPROBADO.");
    }

    @Test
    @DisplayName("Debe asignar ACTIVO por defecto si no tiene calificación asignada")
    public void testEstadoPorDefectoSinNota() {
        Inscripcion inscripcion = new Inscripcion(1, 1);
        inscripcion.setFinalGrade(null);
        inscripcion.setStatus("");

        assertDoesNotThrow(() -> {
            servicio.validarInscripcion(inscripcion, true);
        });

        assertEquals("ACTIVO", inscripcion.getStatus());
    }

    @Test
    @DisplayName("Debe preservar estado CANCELADO incluso si tiene nota asignada")
    public void testPreservarEstadoCancelado() {
        Inscripcion inscripcion = new Inscripcion(1, 1);
        inscripcion.setFinalGrade(4.0);
        inscripcion.setStatus("CANCELADO");

        assertDoesNotThrow(() -> {
            servicio.validarInscripcion(inscripcion, true);
        });

        assertEquals("CANCELADO", inscripcion.getStatus());
    }
}
