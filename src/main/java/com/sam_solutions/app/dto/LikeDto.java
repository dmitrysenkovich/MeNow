package com.sam_solutions.app.dto;

/**
 * Like DTO representation.
 */
public class LikeDto {
    /**
     * Post likes count;
     */
    private int likesCount;

    /**
     * If user liked post.
     */
    private boolean liked;

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
