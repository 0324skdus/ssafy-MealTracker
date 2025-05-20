package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.CommunityDao;
import com.ssafy.yamyam.dao.CommunityLikeDao;
import com.ssafy.yamyam.dto.Community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl implements CommunityService {

    private final CommunityDao communityDao;
    private final CommunityLikeDao communityLikeDao;

    @Autowired
    public CommunityServiceImpl(CommunityDao communityDao,CommunityLikeDao communityLikeDao) {
        this.communityDao = communityDao;
        this.communityLikeDao = communityLikeDao;
    }

    @Override
    public boolean register(Community community) {
        return communityDao.insert(community) > 0;
    }

    @Override
    public Community getById(int id) {
        return communityDao.selectById(id);
    }

    @Override
    public List<Community> getAll() {
        return communityDao.selectAll();
    }

    @Override
    public boolean modify(Community community) {
        return communityDao.update(community) > 0;
    }

    @Override
    public boolean remove(int id) {
        return communityDao.delete(id) > 0;
    }
    //좋아요 기능
    @Override
    public boolean hasUserLiked(int communityId, String userId) {
        return communityLikeDao.exists(communityId, userId);
    }

    @Override
    public void addLike(int communityId, String userId) {
        if (communityLikeDao.exists(communityId, userId)) {
            throw new IllegalStateException("이미 추천한 게시글입니다.");
        }
        communityLikeDao.insert(communityId, userId);
        communityDao.incrementLikeCount(communityId); // like_count 컬럼 필요
    }
    //게시판 조회
    public List<Community> getBoardList() {
        return communityDao.selectBoardList();
    }

}
