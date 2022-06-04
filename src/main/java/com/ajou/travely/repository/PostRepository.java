package com.ajou.travely.repository;

import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select p from Post p left join fetch p.user u where u.id in :userIds",
    countQuery = "select count(p) from Post p where p.user.id in :userIds")
    Page<Post> findAllPostsByUserIds(List<Long> userIds, Pageable pageable);

    @Query("select distinct p from Post p left join fetch p.user left join fetch p.schedule where p.user = :user order by p.id desc")
    List<Post> findAllByUser(User user);
}
