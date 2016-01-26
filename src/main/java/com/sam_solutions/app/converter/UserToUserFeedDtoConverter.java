package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserFeedDto;
import com.sam_solutions.app.model.User;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * User to UserFeedDto converter.
 */
public class UserToUserFeedDtoConverter implements Converter<User, UserFeedDto> {
    /**
     * Converts User to UserFeedDto object.
     * @param user object to be converted.
     * @return converted object.
     */
    @Override
    public UserFeedDto convert(User user) {
        if (user == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(User.class),
                    TypeDescriptor.valueOf(UserFeedDto.class), user, null);
        }

        UserFeedDto userFeedDto = new UserFeedDto();
        userFeedDto.setNick(user.getNick());
        userFeedDto.setAvatarImage(user.getAvatarImage());
        userFeedDto.setAvatarImageName(user.getAvatarImageName());
        return userFeedDto;
    }
}
