package com.ssafy.yamyam.dao;

import com.ssafy.yamyam.dto.Community;
import java.util.List;

public interface CommunityDao {

    int insert(Community community);

    Community selectById(int id);

    List<Community> selectAll();

    int update(Community community);

    int delete(int id);
    //좋아요수 카운트 기능
    int incrementLikeCount(int communityId);

}
