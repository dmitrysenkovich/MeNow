package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserFollowingDto;
import com.sam_solutions.app.model.User;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * User to UserFollowingDto converter.
 */
public class UserToUserFollowingDtoConverter implements Converter<User, UserFollowingDto> {
    /**
     * Converts User to UserFollowingDto object.
     * @param user object to be converted.
     * @return converted object.
     */
    @Override
    public UserFollowingDto convert(User user) {
        if (user == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(User.class),
                    TypeDescriptor.valueOf(UserFollowingDto.class), user, null);
        }

        UserFollowingDto userFollowingDto = new UserFollowingDto();
        userFollowingDto.setLogin(user.getLogin());
        userFollowingDto.setNick(user.getNick());
        userFollowingDto.setAvatarImage(user.getAvatarImage());
        userFollowingDto.setAvatarImageName(user.getAvatarImageName());
        return userFollowingDto;
    }
}
