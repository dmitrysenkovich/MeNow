package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.UserSignUpDto;
import com.sam_solutions.app.model.User;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * UserSignUpDto to User converter.
 */
public class UserSignUpDtoToUserConverter implements Converter<UserSignUpDto, User> {
    /**
     * Converts UserSignUpDto to User object.
     * @param userSignUpDto object to be converted.
     * @return converted object.
     */
    @Override
    public User convert(UserSignUpDto userSignUpDto) {
        if (userSignUpDto == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(UserSignUpDto.class),
                    TypeDescriptor.valueOf(User.class), userSignUpDto, null);
        }

        User user = new User();
        user.setNick(userSignUpDto.getNick());
        user.setEmail(userSignUpDto.getEmail());
        user.setPhoneNumber(userSignUpDto.getPhoneNumber());
        user.setLogin(userSignUpDto.getLogin());
        user.setPassword(userSignUpDto.getPassword());
        return user;
    }
}
