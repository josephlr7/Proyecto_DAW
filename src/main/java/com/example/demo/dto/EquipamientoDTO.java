package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipamientoDTO(
    Long id,

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(max = 100, message = "El nombre del equipo no puede superar los 100 caracteres")
    String nombre,

    @NotBlank(message = "La función del equipo es obligatoria")
    @Size(max = 255, message = "La función del equipo no puede superar los 255 caracteres")
    String funcion,

    @NotBlank(message = "El rango de precio es obligatorio")
    @Size(max = 50, message = "El rango de precio no puede superar los 50 caracteres")
    String rangoPrecio,

    @NotNull(message = "El año de adquisición es obligatorio")
    @Min(value = 1900, message = "El año de adquisición debe ser válido")
    Integer anoAdquisicion,

    @NotNull(message = "Las horas de uso son obligatorias")
    @DecimalMin(value = "0.0", message = "Las horas de uso no pueden ser negativas")
    Double horasUso,

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    String estado,

    @NotBlank(message = "El programa de mantenimiento es obligatorio")
    @Size(max = 50, message = "El programa de mantenimiento no puede superar los 50 caracteres")
    String programaMantenimiento,

    @Min(value = 0, message = "Las horas del programa de mantenimiento no pueden ser negativas")
    Integer programaMantenimientoHoras,

    @Size(max = 20, message = "El cumplimiento del mantenimiento no puede superar los 20 caracteres")
    String seCumpleMantenimiento,

    @NotNull(message = "Debe indicar si requiere consumible")
    Boolean requiereConsumible,

    @Size(max = 50, message = "El tipo de consumible requerido no puede superar los 50 caracteres")
    String tipoConsumibleRequerido,

    @NotNull(message = "El ID del laboratorio es obligatorio")
    Long laboratorioId
) {}
