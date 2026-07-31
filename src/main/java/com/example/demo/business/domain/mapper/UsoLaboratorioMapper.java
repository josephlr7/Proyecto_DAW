package com.example.demo.business.domain.mapper;

import com.example.demo.business.api.dto.UsoConsumibleResponseDTO;
import com.example.demo.business.api.dto.UsoEquipamientoResponseDTO;
import com.example.demo.business.data.entity.UsoConsumible;
import com.example.demo.business.data.entity.UsoEquipamiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsoLaboratorioMapper {

    @Mapping(target = "investigadorId", expression = "java(entity.getInvestigador() != null ? entity.getInvestigador().getId() : null)")
    @Mapping(target = "investigadorNombreCompleto", expression = "java(entity.getInvestigador() != null ? entity.getInvestigador().getNombres() + \" \" + entity.getInvestigador().getApellidos() : null)")
    @Mapping(target = "equipamientoId", expression = "java(entity.getEquipamiento() != null ? entity.getEquipamiento().getId() : null)")
    @Mapping(target = "equipamientoNombre", expression = "java(entity.getEquipamiento() != null ? entity.getEquipamiento().getNombre() : null)")
    UsoEquipamientoResponseDTO toEquipamientoResponse(UsoEquipamiento entity);

    @Mapping(target = "investigadorId", expression = "java(entity.getInvestigador() != null ? entity.getInvestigador().getId() : null)")
    @Mapping(target = "investigadorNombreCompleto", expression = "java(entity.getInvestigador() != null ? entity.getInvestigador().getNombres() + \" \" + entity.getInvestigador().getApellidos() : null)")
    @Mapping(target = "consumibleId", expression = "java(entity.getConsumible() != null ? entity.getConsumible().getId() : null)")
    @Mapping(target = "consumibleNombre", expression = "java(entity.getConsumible() != null ? entity.getConsumible().getNombre() : null)")
    UsoConsumibleResponseDTO toConsumibleResponse(UsoConsumible entity);
}

