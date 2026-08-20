package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.UsuarioUnificadoRequestDTO;
import com.example.demo.business.api.dto.UsuarioUnificadoResponseDTO;

import java.util.List;

public interface GestorUsuariosService {
    void registrarUsuarioYPersona(UsuarioUnificadoRequestDTO request);
    List<UsuarioUnificadoResponseDTO> listarTodos();
    void actualizarUsuarioYPersona(String dni, UsuarioUnificadoRequestDTO request);
    void darDeBaja(String dni);
}
