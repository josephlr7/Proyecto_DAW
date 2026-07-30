package com.example.demo.mapper;

import com.example.demo.dto.UsoConsumibleResponseDTO;
import com.example.demo.dto.UsoEquipamientoResponseDTO;
import com.example.demo.entity.UsoConsumible;
import com.example.demo.entity.UsoEquipamiento;
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
