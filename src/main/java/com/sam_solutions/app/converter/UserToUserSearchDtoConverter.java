package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserSearchDto;
import com.sam_solutions.app.model.User;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * User to UserSearchDto converter.
 */
public class UserToUserSearchDtoConverter implements Converter<User, UserSearchDto> {
    /**
     * Converts User to UserSearchDto object.
     * @param user object to be converted.
     * @return converted object.
     */
    @Override
    public UserSearchDto convert(User user) {
        if (user == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(User.class),
                    TypeDescriptor.valueOf(UserSearchDto.class), user, null);
        }

        UserSearchDto userSearchDto = new UserSearchDto();
        userSearchDto.setId(user.getId());
        userSearchDto.setLogin(user.getLogin());
        userSearchDto.setNick(user.getNick());
        userSearchDto.setStatus(user.getStatus());
        userSearchDto.setAvatarImageName(user.getAvatarImageName());
        return userSearchDto;
    }
}

