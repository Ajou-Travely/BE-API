package com.ajou.travely.repository;

import com.ajou.travely.domain.Photo;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from Photo p where p.id in :ids")
    void deleteAllPhotosByIdInQuery(@Param("ids") List<Long> ids);

    @Query("select p from Photo p where p.id in :ids")
    List<Photo> findPhotosByIdsInQuery(@Param("ids") List<Long> ids);

}
