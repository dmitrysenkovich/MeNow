package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserSettingsDto;
import com.sam_solutions.app.model.User;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * User to UserSettingsDto converter.
 */
public class UserToUserSettingsDtoConverter implements Converter<User, UserSettingsDto> {
    /**
     * Converts User to UserSettingsDto object.
     * @param user object to be converted.
     * @return converted object.
     */
    @Override
    public UserSettingsDto convert(User user) {
        if (user == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(User.class),
                    TypeDescriptor.valueOf(UserSettingsDto.class), user, null);
        }

        UserSettingsDto userSettingsDto = new UserSettingsDto();
        userSettingsDto.setPermissionType(user.getPermissionType().getValue());
        userSettingsDto.setPhoneNumber(user.getPhoneNumber());
        userSettingsDto.setNotification(user.getNotification().toString());
        return userSettingsDto;
    }
}
