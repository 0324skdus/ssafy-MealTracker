package com.ssafy.yamyam.dao;

import org.apache.ibatis.annotations.Param;

public interface CommunityLikeDao {

    boolean exists(@Param("communityId") int communityId, @Param("userId") String userId);

    void insert(@Param("communityId") int communityId, @Param("userId") String userId);
}

