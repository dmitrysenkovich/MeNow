package com.sam_solutions.app.converter;

import com.sam_solutions.app.dto.PostFeedDto;
import com.sam_solutions.app.model.Post;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * Post to PostFeedDto converter.
 */
public class PostToPostFeedDtoConverter implements Converter<Post, PostFeedDto> {
    /**
     * Converts Post to PostFeedDto object.
     * @param post object to be converted.
     * @return converted object.
     */
    @Override
    public PostFeedDto convert(Post post) {
        if (post == null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(Post.class),
                    TypeDescriptor.valueOf(PostFeedDto.class), post, null);
        }

        PostFeedDto postFeedDto = new PostFeedDto();
        postFeedDto.setId(post.getId());
        postFeedDto.setLogin(post.getUser().getLogin());
        postFeedDto.setMessage(post.getMessage());
        postFeedDto.setCreatedDate(post.getCreatedDate());
        return postFeedDto;
    }
}
