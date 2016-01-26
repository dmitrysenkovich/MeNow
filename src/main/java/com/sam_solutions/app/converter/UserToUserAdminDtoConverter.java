package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserAdminDto;
import com.sam_solutions.app.model.User;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * User to UserAdminDto converter.
 */
public class UserToUserAdminDtoConverter implements Converter<User, UserAdminDto> {
    /**
     * Converts User to UserAdminDto object.
     * @param user object to be converted.
     * @return converted object.
     */
    @Override
    public UserAdminDto convert(User user) {
        if (user == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(User.class),
                    TypeDescriptor.valueOf(UserAdminDto.class), user, null);
        }

        UserAdminDto userAdminDto = new UserAdminDto();
        userAdminDto.setId(user.getId());
        userAdminDto.setLogin(user.getLogin());
        userAdminDto.setEmail(user.getEmail());
        userAdminDto.setAccess(user.getAccess());
        userAdminDto.setAvatarImageName(user.getAvatarImageName());
        userAdminDto.setAvatarImage(user.getAvatarImage());
        return userAdminDto;
    }
}
