package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserRelationDto;
import com.sam_solutions.app.model.UserRelation;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * UserRelation to UserRelationDto converter.
 */
public class UserRelationToUserRelationDtoConverter implements Converter<UserRelation, UserRelationDto> {
    /**
     * Converts UserRelation to UserProfileDto object.
     * @param userRelation object to be converted.
     * @return converted object.
     */
    @Override
    public UserRelationDto convert(UserRelation userRelation) {
        if (userRelation == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(UserRelation.class),
                    TypeDescriptor.valueOf(UserRelationDto.class), userRelation, null);
        }

        UserRelationDto userRelationDto = new UserRelationDto();
        userRelationDto.setOwner(userRelation.getOwner());
        userRelationDto.setViewer(userRelation.getViewer());
        userRelationDto.setIsAllowed(userRelation.getIsAllowed());
        userRelationDto.setIsFollower(userRelation.getIsFollower());
        return userRelationDto;
    }
}
