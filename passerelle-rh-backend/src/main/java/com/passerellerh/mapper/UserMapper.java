package com.passerellerh.mapper;

import com.passerellerh.dto.UpdateProfileRequest;
import com.passerellerh.dto.UserResponseDTO;
import com.passerellerh.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @org.mapstruct.Mapping(target = "competences", ignore = true)
    UserResponseDTO toDto(User user);

    @org.mapstruct.AfterMapping
    default void mapCompetences(User user, @MappingTarget UserResponseDTO dto) {
        if (user instanceof com.passerellerh.entity.Utilisateur) {
            com.passerellerh.entity.Utilisateur utilisateur = (com.passerellerh.entity.Utilisateur) user;
            if (utilisateur.getCompetences() != null) {
                dto.setCompetences(utilisateur.getCompetences().stream()
                        .map(this::toCompetenceDto)
                        .collect(java.util.stream.Collectors.toList()));
            }
            if (utilisateur.getPasseport() != null && utilisateur.getPasseport().getBadges() != null) {
                dto.setBadges(utilisateur.getPasseport().getBadges().stream()
                        .map(b -> {
                            com.passerellerh.dto.BadgeDTO bDto = new com.passerellerh.dto.BadgeDTO();
                            bDto.setId(b.getId());
                            bDto.setNom(b.getNom());
                            bDto.setSeuilScore(b.getSeuilScore());
                            bDto.setIconeUrl(b.getIconeUrl());
                            return bDto;
                        })
                        .collect(java.util.stream.Collectors.toList()));
            }
        }
    }

    com.passerellerh.dto.CompetenceDTO toCompetenceDto(com.passerellerh.entity.Competence competence);

    com.passerellerh.entity.Competence toEntity(com.passerellerh.dto.CompetenceDTO dto);

    com.passerellerh.dto.BadgeDTO toBadgeDto(com.passerellerh.entity.Badge badge);

    com.passerellerh.entity.Badge toEntity(com.passerellerh.dto.BadgeDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateProfileRequest dto, @MappingTarget User user);
}
