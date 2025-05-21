package com.ssafy.yamyam.dao;

import org.apache.ibatis.annotations.Param;

public interface CommunityLikeDao {

    boolean exists(@Param("communityId") int communityId, @Param("userId") String userId);

    void insert(@Param("communityId") int communityId, @Param("userId") String userId);

    boolean hasUserLiked(@Param("communityId") int communityId, @Param("userId") String userId);

    void addLike(@Param("communityId") int communityId, @Param("userId") String userId);

    void removeLike(@Param("communityId") int communityId, @Param("userId") String userId);
}

