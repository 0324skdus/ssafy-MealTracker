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
    //게시판 조회용 리스트
    List<Community> selectBoardList();

    List<Community> selectByLikeCountDesc();

    List<Community> selectByCreatedAtDesc();

    List<Community> selectByCommunityType(String type);
}
