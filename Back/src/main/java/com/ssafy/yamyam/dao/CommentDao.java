package com.ssafy.yamyam.dao;

import com.ssafy.yamyam.dto.Comment;
import java.util.List;

public interface CommentDao {
	
	// 댓글 달기
    int insert(Comment comment);

    
    List<Comment> selectByCommunityId(int communityId);

    int update(Comment comment);

    int delete(int id);
}
