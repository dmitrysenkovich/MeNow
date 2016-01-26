package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserRelationDto;
import com.sam_solutions.app.model.UserRelation;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * UserRelationDto to UserRelation converter.
 */
public class UserRelationDtoToUserRelationConverter implements Converter<UserRelationDto, UserRelation> {
    /**
     * Converts UserRelationDto to UserRelation object.
     * @param userRelationDto object to be converted.
     * @return converted object.
     */
    @Override
    public UserRelation convert(UserRelationDto userRelationDto) {
        if (userRelationDto == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(UserRelationDto.class),
                    TypeDescriptor.valueOf(UserRelation.class), userRelationDto, null);
        }

        UserRelation userRelation = new UserRelation();
        userRelation.setOwner(userRelationDto.getOwner());
        userRelation.setViewer(userRelationDto.getViewer());
        userRelation.setIsAllowed(userRelationDto.getIsAllowed());
        userRelation.setIsFollower(userRelationDto.getIsFollower());
        return userRelation;
    }
}
