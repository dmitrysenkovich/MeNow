package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.PostProfileDto;
import com.sam_solutions.app.model.Post;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * Post to PostProfileDto converter.
 */
public class PostToPostProfileDtoConverter implements Converter<Post, PostProfileDto> {
    /**
     * Converts Post to PostProfileDto object.
     * @param post object to be converted.
     * @return converted object.
     */
    @Override
    public PostProfileDto convert(Post post) {
        if (post == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(Post.class),
                    TypeDescriptor.valueOf(PostProfileDto.class), post, null);
        }

        PostProfileDto postProfileDto = new PostProfileDto();
        postProfileDto.setId(post.getId());
        postProfileDto.setMessage(post.getMessage());
        postProfileDto.setCreatedDate(post.getCreatedDate());
        return postProfileDto;
    }
}
