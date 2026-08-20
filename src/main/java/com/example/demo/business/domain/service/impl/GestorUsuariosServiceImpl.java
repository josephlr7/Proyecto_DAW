package com.example.demo.business.domain.service.impl;

import com.example.demo.business.api.dto.UsuarioUnificadoRequestDTO;
import com.example.demo.business.api.exception.RecursoDuplicadoException;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.data.entity.Investigador;
import com.example.demo.business.data.entity.Laboratorio;
import com.example.demo.business.data.entity.PersonalLaboratorio;
import com.example.demo.business.data.repository.InvestigadorRepository;
import com.example.demo.business.data.repository.LaboratorioRepository;
import com.example.demo.business.data.repository.PersonalLaboratorioRepository;
import com.example.demo.business.domain.service.GestorUsuariosService;
import com.example.demo.business.security.data.entity.NombreRol;
import com.example.demo.business.security.data.entity.Rol;
import com.example.demo.business.security.data.entity.Usuario;
import com.example.demo.business.security.data.repository.RolRepository;
import com.example.demo.business.security.data.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GestorUsuariosServiceImpl implements GestorUsuariosService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonalLaboratorioRepository personalRepository;
    private final InvestigadorRepository investigadorRepository;
    private final LaboratorioRepository laboratorioRepository;

    @Override
    @Transactional
    public void registrarUsuarioYPersona(UsuarioUnificadoRequestDTO request) {
        String dni = request.dni().trim();

        if (usuarioRepository.existsByUsernameIgnoreCase(dni)) {
            throw new RecursoDuplicadoException("El usuario con DNI " + dni + " ya existe.");
        }

        // Determinar el Rol
        NombreRol nombreRol;
        try {
            nombreRol = NombreRol.valueOf(request.rolSistema().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol no válido: " + request.rolSistema());
        }

        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado en BD"));

        // 1. Crear el Usuario (Credenciales)
        Usuario usuario = new Usuario();
        usuario.setUsername(dni); // DNI como username
        usuario.setPassword(passwordEncoder.encode(dni)); // DNI como password
        usuario.setNombre(request.nombres() + " " + request.apellidos());
        usuario.agregarRol(rol);
        
        usuarioRepository.save(usuario);

        // 2. Crear la Persona correspondiente en base al rol
        if (nombreRol == NombreRol.PERSONAL || nombreRol == NombreRol.ADMIN) {
            PersonalLaboratorio personal = new PersonalLaboratorio();
            personal.setNombres(request.nombres());
            personal.setApellidos(request.apellidos());
            personal.setDni(dni);
            personal.setGenero(request.genero());
            
            // Campos específicos
            personal.setCargo(request.cargo() != null ? request.cargo() : "NO ESPECIFICADO");
            personal.setCondicion(request.condicion() != null ? request.condicion() : "NO ESPECIFICADO");
            personal.setEsDocente(request.esDocente() != null && request.esDocente());
            personal.setRenacyt(request.renacyt() != null && request.renacyt());
            personal.setEsDocenteInvestigadorUNT(request.esDocenteInvestigadorUNT() != null && request.esDocenteInvestigadorUNT());

            if (request.laboratorioId() != null && request.laboratorioId() > 0) {
                Laboratorio lab = laboratorioRepository.findById(request.laboratorioId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado"));
                personal.setLaboratorio(lab);
            }

            personalRepository.save(personal);

        } else if (nombreRol == NombreRol.INVESTIGADOR) {
            Investigador investigador = new Investigador();
            investigador.setNombres(request.nombres());
            investigador.setApellidos(request.apellidos());
            investigador.setDni(dni);
            investigador.setGenero(request.genero());

            investigador.setProgramaEstudios(request.programaEstudios() != null ? request.programaEstudios() : "NO ESPECIFICADO");
            investigador.setGradoAcademico(request.gradoAcademico() != null ? request.gradoAcademico() : "NO ESPECIFICADO");

            investigadorRepository.save(investigador);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<com.example.demo.business.api.dto.UsuarioUnificadoResponseDTO> listarTodos() {
        // Obtenemos todos los usuarios (credenciales)
        java.util.List<Usuario> usuarios = usuarioRepository.findAll();
        java.util.List<com.example.demo.business.api.dto.UsuarioUnificadoResponseDTO> responses = new java.util.ArrayList<>();

        for (Usuario u : usuarios) {
            String roleName = u.getRoles().stream().findFirst().map(r -> r.getNombre().name()).orElse("UNKNOWN");
            
            // Buscar en Persona por DNI
            String dni = u.getUsername();
            com.example.demo.business.api.dto.UsuarioUnificadoResponseDTO dto = null;

            if (roleName.equals("INVESTIGADOR")) {
                java.util.Optional<Investigador> invOpt = investigadorRepository.findByDni(dni);
                if (invOpt.isPresent()) {
                    Investigador inv = invOpt.get();
                    dto = new com.example.demo.business.api.dto.UsuarioUnificadoResponseDTO(
                            inv.getId(), inv.getNombres(), inv.getApellidos(), inv.getDni(), inv.getGenero(), roleName, u.isActivo(),
                            null, null, null, null, null, null,
                            inv.getProgramaEstudios(), inv.getGradoAcademico()
                    );
                }
            } else {
                // PERSONAL o ADMIN
                java.util.Optional<PersonalLaboratorio> persOpt = personalRepository.findByDni(dni);
                if (persOpt.isPresent()) {
                    PersonalLaboratorio pers = persOpt.get();
                    Long labId = pers.getLaboratorio() != null ? pers.getLaboratorio().getId() : null;
                    dto = new com.example.demo.business.api.dto.UsuarioUnificadoResponseDTO(
                            pers.getId(), pers.getNombres(), pers.getApellidos(), pers.getDni(), pers.getGenero(), roleName, u.isActivo(),
                            pers.getCargo(), pers.getCondicion(), labId, pers.getEsDocente(), pers.getRenacyt(), pers.getEsDocenteInvestigadorUNT(),
                            null, null
                    );
                }
            }

            if (dto != null) {
                responses.add(dto);
            }
        }
        return responses;
    }

    @Override
    @Transactional
    public void actualizarUsuarioYPersona(String dni, UsuarioUnificadoRequestDTO request) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        // Se permite actualizar nombres
        usuario.setNombre(request.nombres() + " " + request.apellidos());
        usuarioRepository.save(usuario);

        String roleName = usuario.getRoles().stream().findFirst().map(r -> r.getNombre().name()).orElse("UNKNOWN");

        if (roleName.equals("INVESTIGADOR")) {
            Investigador investigador = investigadorRepository.findByDni(dni)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Investigador no encontrado"));
            
            investigador.setNombres(request.nombres());
            investigador.setApellidos(request.apellidos());
            investigador.setGenero(request.genero());
            investigador.setProgramaEstudios(request.programaEstudios());
            investigador.setGradoAcademico(request.gradoAcademico());
            
            investigadorRepository.save(investigador);
        } else {
            PersonalLaboratorio personal = personalRepository.findByDni(dni)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Personal no encontrado"));

            personal.setNombres(request.nombres());
            personal.setApellidos(request.apellidos());
            personal.setGenero(request.genero());
            personal.setCargo(request.cargo());
            personal.setCondicion(request.condicion());
            personal.setEsDocente(request.esDocente() != null && request.esDocente());
            personal.setRenacyt(request.renacyt() != null && request.renacyt());
            personal.setEsDocenteInvestigadorUNT(request.esDocenteInvestigadorUNT() != null && request.esDocenteInvestigadorUNT());

            if (request.laboratorioId() != null && request.laboratorioId() > 0) {
                Laboratorio lab = laboratorioRepository.findById(request.laboratorioId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado"));
                personal.setLaboratorio(lab);
            } else {
                personal.setLaboratorio(null);
            }

            personalRepository.save(personal);
        }
    }

    @Override
    @Transactional
    public void darDeBaja(String dni) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        
        usuario.setActivo(false); // Baja lógica del acceso
        usuarioRepository.save(usuario);

        String roleName = usuario.getRoles().stream().findFirst().map(r -> r.getNombre().name()).orElse("UNKNOWN");

        if (roleName.equals("INVESTIGADOR")) {
            Investigador inv = investigadorRepository.findByDni(dni).orElse(null);
            if(inv != null) {
                inv.setActivo(false);
                investigadorRepository.save(inv);
            }
        } else {
            PersonalLaboratorio pers = personalRepository.findByDni(dni).orElse(null);
            if(pers != null) {
                pers.setActivo(false);
                personalRepository.save(pers);
            }
        }
    }
}
