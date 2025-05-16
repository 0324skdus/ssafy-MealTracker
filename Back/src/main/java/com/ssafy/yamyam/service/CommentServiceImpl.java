package com.ssafy.yamyam.service;

import com.ssafy.yamyam.dao.CommentDao;
import com.ssafy.yamyam.dto.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    @Autowired
    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public boolean register(Comment comment) {
        return commentDao.insert(comment) > 0;
    }

    @Override
    public List<Comment> getByCommunityId(int communityId) {
        return commentDao.selectByCommunityId(communityId);
    }

    @Override
    public boolean modify(Comment comment) {
        return commentDao.update(comment) > 0;
    }

    @Override
    public boolean remove(int id) {
        return commentDao.delete(id) > 0;
    }
}
