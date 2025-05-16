package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.Community;

import java.util.List;

public interface CommunityService {

    boolean register(Community community);

    Community getById(int id);

    List<Community> getAll();

    boolean modify(Community community);

    boolean remove(int id);

    //좋아요 기능
    boolean hasUserLiked(int communityId, String userId);

    void addLike(int communityId, String userId);

}
