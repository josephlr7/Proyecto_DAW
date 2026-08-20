package com.example.demo.business.domain.mapper;

import com.example.demo.business.api.dto.PersonalLaboratorioRequestDTO;
import com.example.demo.business.api.dto.PersonalLaboratorioResponseDTO;
import com.example.demo.business.data.entity.PersonalLaboratorio;
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
    @Mapping(target = "laboratorio", ignore = true)
    void updateEntityFromRequest(PersonalLaboratorioRequestDTO request, @MappingTarget PersonalLaboratorio entity);
}

