package com.example.demo;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private PersonalLaboratorioService personalService;

    @Autowired
    private InvestigadorService investigadorService;

    @Autowired
    private EquipamientoService equipamientoService;

    @Autowired
    private ConsumibleService consumibleService;

    @Autowired
    private UsoService usoService;

    @Test
    @Transactional
    void verificarFlujoCompletoLaboratorio() {
        // 1. Registrar Laboratorio
        LaboratorioDTO labDto = new LaboratorioDTO(
                null,
                "Ingeniería",
                "Sistemas e Informática",
                "Ingeniería y Tecnología",
                "Inteligencia Artificial, Ciberseguridad",
                "Consolidado",
                "RESOL-2026-UNT",
                "laboratorio.sistemas@unt.edu.pe",
                "ODS 9",
                true
        );
        LaboratorioDTO labCreado = laboratorioService.registrarLaboratorio(labDto);
        assertNotNull(labCreado.id());
        assertEquals("Ingeniería", labCreado.facultad());

        // 2. Registrar Personal de Laboratorio (con perfil uno a uno)
        PerfilPersonalDTO perfilDto = new PerfilPersonalDTO(
                LocalDate.of(2026, 1, 1),
                "Ingeniero con 10 años de experiencia en administración de laboratorios.",
                "Oficina 204"
        );
        PersonalLaboratorioRequestDTO personalReq = new PersonalLaboratorioRequestDTO(
                "Juan Carlos",
                "Perez Gomez",
                "12345678",
                "Masculino",
                "RESOL-PERS-001",
                "Jefe de Laboratorio",
                "http://photos.com/perez.jpg",
                true,
                true, // Renacyt
                true,
                "Nombrado",
                "Principal",
                labCreado.id(),
                perfilDto
        );
        PersonalLaboratorioResponseDTO personalCreado = personalService.registrarPersonal(personalReq);
        assertNotNull(personalCreado.id());
        assertNotNull(personalCreado.perfil());
        assertEquals("Jefe de Laboratorio", personalCreado.cargo());

        // Verificar búsqueda de personal Renacyt
        List<PersonalLaboratorioResponseDTO> renacyts = personalService.obtenerInvestigadoresRenacyt();
        assertFalse(renacyts.isEmpty());
        assertEquals("12345678", renacyts.get(0).dni());

        // 3. Registrar Investigador (Herencia)
        InvestigadorDTO invDto = new InvestigadorDTO(
                null,
                "Ana Maria",
                "Sanchez Ruiz",
                "87654321",
                "Femenino",
                "Ingeniería de Sistemas",
                "Tesista de Pregrado"
        );
        InvestigadorDTO invCreado = investigadorService.registrarInvestigador(invDto);
        assertNotNull(invCreado.id());

        // 4. Registrar Equipamiento (Relación ManyToOne a Laboratorio)
        EquipamientoDTO eqDto = new EquipamientoDTO(
                null,
                "Servidor GPU NVIDIA H100",
                "Cómputo científico de alto rendimiento",
                "]500-1000]",
                2026,
                0.0,
                "operativo",
                "anual",
                1000,
                "Si",
                false,
                null,
                labCreado.id()
        );
        EquipamientoDTO eqCreado = equipamientoService.registrarEquipamiento(eqDto);
        assertNotNull(eqCreado.id());

        // 5. Registrar Consumible
        ConsumibleDTO conDto = new ConsumibleDTO(
                null,
                "Limpiador antiestático",
                "ContactClean",
                "LabSupplies S.A.",
                "liquido",
                "otro",
                "mililitros",
                "Limpieza de circuitos y fibra óptica",
                "5-10",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                1000.0,
                100.0,
                labCreado.id()
        );
        ConsumibleDTO conCreado = consumibleService.registrarConsumible(conDto);
        assertNotNull(conCreado.id());

        // 6. Registrar Usos (Horas de equipo y consumo de stock)
        UsoEquipamientoRequestDTO usoEqReq = new UsoEquipamientoRequestDTO(
                invCreado.id(),
                eqCreado.id(),
                "tesis de pregrado",
                "Entrenamiento de modelos de redes neuronales convolucionales",
                150.0, // Horas utilizadas
                LocalDate.now(),
                LocalTime.now(),
                "Entrenamiento exitoso."
        );
        UsoEquipamientoResponseDTO usoEqResp = usoService.registrarUsoEquipamiento(usoEqReq);
        assertNotNull(usoEqResp.id());

        // Validar incremento de horas de uso en equipamiento
        EquipamientoDTO eqConsultado = equipamientoService.obtenerPorId(eqCreado.id());
        assertEquals(150.0, eqConsultado.horasUso());

        // Registrar uso de consumible
        UsoConsumibleRequestDTO usoConReq = new UsoConsumibleRequestDTO(
                invCreado.id(),
                conCreado.id(),
                "tesis de pregrado",
                "Limpieza previa de componentes",
                250.0, // Cantidad consumida
                LocalDate.now(),
                LocalTime.now(),
                "Se consumió limpiador."
        );
        UsoConsumibleResponseDTO usoConResp = usoService.registrarUsoConsumible(usoConReq);
        assertNotNull(usoConResp.id());

        // Validar disminución de stock de consumible
        ConsumibleDTO conConsultado = consumibleService.obtenerPorId(conCreado.id());
        assertEquals(750.0, conConsultado.cantidad());

        // 7. Validar Reportes de Semáforo
        List<EquipamientoSemaforoDTO> semaforoEq = equipamientoService.obtenerReporteSemaforo(labCreado.id());
        assertFalse(semaforoEq.isEmpty());
        // 1000 - 150 = 850 horas restantes (mayor a 300, debe ser VERDE)
        assertEquals("VERDE", semaforoEq.get(0).estadoSemaforo());

        List<ConsumibleSemaforoDTO> semaforoCon = consumibleService.obtenerReporteSemaforo(labCreado.id());
        assertFalse(semaforoCon.isEmpty());
        // 750 cantidad es mayor a 100 * 1.5 = 150, debe ser VERDE
        assertEquals("VERDE", semaforoCon.get(0).estadoSemaforo());
    }
}
