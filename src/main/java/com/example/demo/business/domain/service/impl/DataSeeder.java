package com.example.demo.business.domain.service.impl;

import com.example.demo.business.data.entity.*;
import com.example.demo.business.data.repository.*;
import com.example.demo.business.security.data.entity.NombreRol;
import com.example.demo.business.security.data.entity.Rol;
import com.example.demo.business.security.data.entity.Usuario;
import com.example.demo.business.security.data.repository.RolRepository;
import com.example.demo.business.security.data.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final FacultadRepository facultadRepository;
    private final EscuelaRepository escuelaRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final LaboratorioRepository laboratorioRepository;
    private final EquipamientoRepository equipamientoRepository;
    private final ConsumibleRepository consumibleRepository;
    private final PersonalLaboratorioRepository personalRepository;
    private final jakarta.persistence.EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Verificamos si ya hay datos en la base de datos para no borrarlos
        if (usuarioRepository.count() > 0) {
            return; // Si ya hay usuarios, asumimos que la BD ya fue inicializada y no borramos nada
        }

        // Si la base de datos está vacía, limpiamos por si acaso hay datos corruptos
        entityManager.createNativeQuery("DELETE FROM usos_equipamiento").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM usos_consumible").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM equipamientos").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM consumibles").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM personal_laboratorio").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM personas").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM laboratorios").executeUpdate();

        usuarioRepository.deleteAll();
        rolRepository.deleteAll();

        // 3. Sembrado de Roles
        if (rolRepository.count() == 0) {
            seedRoles();
        }

        // 4. Sembrado de Facultades y Escuelas
        if (facultadRepository.count() == 0) {
            seedFacultadesYEscuelas();
        }

        // 5. Sembrado de Usuarios
        if (usuarioRepository.count() == 0) {
            seedDefaultUsers();
        }

        // 6. Sembrado de Laboratorios, Equipos, Insumos y Personal de Prueba
        if (laboratorioRepository.count() == 0) {
            seedLaboratoriosEquiposYConsumibles();
        }
    }

    private void seedRoles() {
        for (NombreRol nombre : NombreRol.values()) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rolRepository.save(rol);
        }
    }

    private void seedDefaultUsers() {
        Rol rolAdmin = rolRepository.findByNombre(NombreRol.ADMIN).orElseThrow();
        Rol rolPersonal = rolRepository.findByNombre(NombreRol.PERSONAL).orElseThrow();

        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("Admin12345"));
        admin.setNombre("Administrador");
        admin.agregarRol(rolAdmin);
        usuarioRepository.save(admin);

        Usuario personal = new Usuario();
        personal.setUsername("personal");
        personal.setPassword(passwordEncoder.encode("Personal12345"));
        personal.setNombre("Personal de Laboratorio");
        personal.agregarRol(rolPersonal);
        usuarioRepository.save(personal);
    }

    private void seedLaboratoriosEquiposYConsumibles() {
        List<Facultad> facultades = facultadRepository.findAll();
        List<Escuela> escuelas = escuelaRepository.findAll();

        if (facultades.isEmpty() || escuelas.isEmpty()) return;

        // Buscamos facultad de Ingeniería y su escuela
        Facultad facIng = facultades.stream()
                .filter(f -> f.getNombre().contains("Ingeniería"))
                .findFirst()
                .orElse(facultades.get(0));

        Escuela escSistemas = escuelas.stream()
                .filter(e -> e.getNombre().contains("Sistemas"))
                .findFirst()
                .orElse(escuelas.get(0));

        // Creamos Laboratorio 1
        Laboratorio lab1 = new Laboratorio();
        lab1.setFacultad(facIng);
        lab1.setEscuela(escSistemas);
        lab1.setPoseeSistemaGestion(true);
        lab1.setActivo(true);
        laboratorioRepository.save(lab1);

        // Creamos Equipamiento para Lab 1
        Equipamiento eq1 = new Equipamiento();
        eq1.setNombre("Servidor GPU NVIDIA A100");
        eq1.setFuncion("Entrenamiento de modelos de redes neuronales profundas");
        eq1.setRangoPrecio("Alto");
        eq1.setAnoAdquisicion(2024);
        eq1.setHorasUso(240.5);
        eq1.setEstado("OPERATIVO");
        eq1.setProgramaMantenimiento("SEMESTRAL");
        eq1.setProgramaMantenimientoHoras(500);
        eq1.setSeCumpleMantenimiento("AL DÍA");
        eq1.setRequiereConsumible(false);
        eq1.setLaboratorio(lab1);
        equipamientoRepository.save(eq1);

        Equipamiento eq2 = new Equipamiento();
        eq2.setNombre("Estación de Trabajo Xeon");
        eq2.setFuncion("Procesamiento local de conjuntos de datos de prueba");
        eq2.setRangoPrecio("Medio");
        eq2.setAnoAdquisicion(2023);
        eq2.setHorasUso(450.0);
        eq2.setEstado("MANTENIMIENTO");
        eq2.setProgramaMantenimiento("TRIMESTRAL");
        eq2.setProgramaMantenimientoHoras(200);
        eq2.setSeCumpleMantenimiento("ATRASADO");
        eq2.setRequiereConsumible(false);
        eq2.setLaboratorio(lab1);
        equipamientoRepository.save(eq2);

        // Creamos Consumibles para Lab 1
        Consumible c1 = new Consumible();
        c1.setNombre("Pasta Térmica de Plata");
        c1.setMarca("Noctua");
        c1.setEmpresa("CoolerMaster Import");
        c1.setEstadoAdquirido("NUEVO");
        c1.setTipo("Mantenimiento Hardware");
        c1.setUnidadMedida("jeringa 5g");
        c1.setFuncion("Disipación de calor del procesador");
        c1.setRangoPrecio("Bajo");
        c1.setFechaAdquisicion(LocalDate.now());
        c1.setFechaVencimiento(LocalDate.now().plusYears(3));
        c1.setCantidad(10.0);
        c1.setStockMinimo(2.0);
        c1.setLaboratorio(lab1);
        consumibleRepository.save(c1);

        Consumible c2 = new Consumible();
        c2.setNombre("Alcohol Isopropílico");
        c2.setMarca("Química Industrial");
        c2.setEmpresa("Química del Norte");
        c2.setEstadoAdquirido("SELLADO");
        c2.setTipo("Limpieza de Componentes");
        c2.setUnidadMedida("Litros");
        c2.setFuncion("Limpieza de tarjetas electrónicas y disipadores");
        c2.setRangoPrecio("Bajo");
        c2.setFechaAdquisicion(LocalDate.now());
        c2.setFechaVencimiento(LocalDate.now().plusYears(2));
        c2.setCantidad(1.0); // BAJO STOCK
        c2.setStockMinimo(2.0);
        c2.setLaboratorio(lab1);
        consumibleRepository.save(c2);

        // Creamos Personal de Laboratorio para Lab 1
        PersonalLaboratorio p1 = new PersonalLaboratorio();
        p1.setNombres("Carlos René");
        p1.setApellidos("Zavaleta Medina");
        p1.setDni("12345678");
        p1.setGenero("MASCULINO");
        p1.setActivo(true);
        p1.setCargo("JEFE_LABORATORIO");
        p1.setEsDocente(true);
        p1.setRenacyt(true);
        p1.setEsDocenteInvestigadorUNT(true);
        p1.setCondicion("NOMBRADO");
        p1.setLaboratorio(lab1);

        personalRepository.save(p1);
    }

    private void seedFacultadesYEscuelas() {
        createFacultadWithEscuelas("Facultad de Ingeniería", Arrays.asList(
                "Ingeniería de Sistemas e Informática",
                "Ingeniería Industrial",
                "Ingeniería Civil",
                "Ingeniería Mecánica",
                "Ingeniería Mecatrónica",
                "Ingeniería de Minas",
                "Ingeniería Metalúrgica",
                "Ingeniería de Materiales",
                "Arquitectura y Urbanismo"
        ));

        createFacultadWithEscuelas("Facultad de Ingeniería Química", Arrays.asList(
                "Ingeniería Química",
                "Ingeniería Ambiental"
        ));

        createFacultadWithEscuelas("Facultad de Ciencias Físicas y Matemáticas", Arrays.asList(
                "Informática",
                "Física",
                "Matemáticas",
                "Estadística"
        ));

        createFacultadWithEscuelas("Facultad de Ciencias Biológicas", Arrays.asList(
                "Ciencias Biológicas",
                "Biología Pesquera",
                "Microbiología y Parasitología"
        ));

        createFacultadWithEscuelas("Facultad de Medicina", Arrays.asList(
                "Medicina"
        ));

        createFacultadWithEscuelas("Facultad de Enfermería", Arrays.asList(
                "Enfermería"
        ));

        createFacultadWithEscuelas("Facultad de Estomatología", Arrays.asList(
                "Estomatología"
        ));

        createFacultadWithEscuelas("Facultad de Farmacia y Bioquímica", Arrays.asList(
                "Farmacia y Bioquímica"
        ));

        createFacultadWithEscuelas("Facultad de Ciencias Agropecuarias", Arrays.asList(
                "Agronomía",
                "Ingeniería Agrícola",
                "Zootecnia"
        ));

        createFacultadWithEscuelas("Facultad de Ciencias Económicas", Arrays.asList(
                "Administración",
                "Contabilidad y Finanzas",
                "Economía"
        ));

        createFacultadWithEscuelas("Facultad de Derecho y Ciencias Políticas", Arrays.asList(
                "Derecho",
                "Ciencia Política y Gobernabilidad"
        ));

        createFacultadWithEscuelas("Facultad de Ciencias Sociales", Arrays.asList(
                "Trabajo Social",
                "Turismo",
                "Antropología",
                "Arqueología",
                "Historia"
        ));

        createFacultadWithEscuelas("Facultad de Educación y Ciencias de la Comunicación", Arrays.asList(
                "Ciencias de la Comunicación",
                "Educación Inicial",
                "Educación Primaria",
                "Educación Secundaria"
        ));

        createFacultadWithEscuelas("POSGRADO", Arrays.asList(
                "Posgrado"
        ));
    }

    private void createFacultadWithEscuelas(String nombreFacultad, List<String> nombresEscuelas) {
        Facultad facultad = new Facultad(nombreFacultad);
        Facultad savedFacultad = facultadRepository.save(facultad);
        for (String nombreEscuela : nombresEscuelas) {
            Escuela escuela = new Escuela(nombreEscuela, savedFacultad);
            escuelaRepository.save(escuela);
        }
    }
}
