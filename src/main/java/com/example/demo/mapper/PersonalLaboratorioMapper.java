package com.example.demo.mapper;

import com.example.demo.dto.PerfilPersonalDTO;
import com.example.demo.dto.PersonalLaboratorioRequestDTO;
import com.example.demo.dto.PersonalLaboratorioResponseDTO;
import com.example.demo.entity.PerfilPersonal;
import com.example.demo.entity.PersonalLaboratorio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonalLaboratorioMapper {

    @Mapping(target = "laboratorioId", expression = "java(entity.getLaboratorio() != null ? entity.getLaboratorio().getId() : null)")
    PersonalLaboratorioResponseDTO toResponse(PersonalLaboratorio entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "laboratorio", ignore = true)
    PersonalLaboratorio toEntity(PersonalLaboratorioRequestDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personal", ignore = true)
    PerfilPersonal toPerfilEntity(PerfilPersonalDTO dto);

    PerfilPersonalDTO toPerfilDto(PerfilPersonal entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "laboratorio", ignore = true)
    void updateEntityFromRequest(PersonalLaboratorioRequestDTO request, @MappingTarget PersonalLaboratorio entity);
}
