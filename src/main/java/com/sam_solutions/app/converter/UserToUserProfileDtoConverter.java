package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserProfileDto;
import com.sam_solutions.app.model.User;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * User to UserProfileDto converter.
 */
public class UserToUserProfileDtoConverter implements Converter<User, UserProfileDto> {
    /**
     * Converts User to UserProfileDto object.
     * @param user object to be converted.
     * @return converted object.
     */
    @Override
    public UserProfileDto convert(User user) {
        if (user == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(User.class),
                    TypeDescriptor.valueOf(UserProfileDto.class), user, null);
        }

        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setLogin(user.getLogin());
        userProfileDto.setNick(user.getNick());
        userProfileDto.setStatus(user.getStatus());
        userProfileDto.setAvatarImageName(user.getAvatarImageName());
        return userProfileDto;
    }
}
