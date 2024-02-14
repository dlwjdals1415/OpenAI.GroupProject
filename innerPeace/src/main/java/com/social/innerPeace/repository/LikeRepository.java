package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Post_Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Post_Like,Long>{
    List<Post_Like> findByPostNoPostNo(Long postNo);
}
