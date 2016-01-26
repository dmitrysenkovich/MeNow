package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.PostProfileDto;
import com.sam_solutions.app.model.Post;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * Post to PostProfileDto converter.
 */
public class PostProfileDtoToPostConverter implements Converter<PostProfileDto, Post> {
    /**
     * Converts PostProfileDto to Postobject.
     * @param postProfileDto object to be converted.
     * @return converted object.
     */
    @Override
    public Post convert(PostProfileDto postProfileDto) {
        if (postProfileDto == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(PostProfileDto.class),
                    TypeDescriptor.valueOf(Post.class), postProfileDto, null);
        }

        Post post = new Post();
        post.setId(postProfileDto.getId());
        post.setMessage(postProfileDto.getMessage());
        post.setCreatedDate(postProfileDto.getCreatedDate());
        return post;
    }
}
