package com.ajou.travely.repository;

import com.ajou.travely.domain.Photo;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select p.name from Photo p where p.id in :ids")
    List<String> findPhotoNamesByIdsInQuery(@Param("ids") List<Long> ids);

}
