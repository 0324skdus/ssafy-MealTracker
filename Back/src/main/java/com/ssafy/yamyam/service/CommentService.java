package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dto.Comment;
import java.util.List;

public interface CommentService {

    boolean register(Comment comment);

    List<Comment> getByCommunityId(int communityId);

    boolean modify(Comment comment);

    boolean remove(int id);

    Comment findById(int id);

    List<Comment> getByUserId(String userId);
}

