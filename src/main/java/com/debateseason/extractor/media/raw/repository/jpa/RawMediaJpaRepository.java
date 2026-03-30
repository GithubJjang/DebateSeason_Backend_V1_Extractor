package com.debateseason.extractor.media.raw.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.raw.entity.RawMediaEntity;

@Repository
public interface RawMediaJpaRepository extends JpaRepository<RawMediaEntity,Long>{

	@Query(
		value = """
				SELECT id FROM media
				ORDER BY id DESC
				LIMIT 1;
		""",
		nativeQuery = true
	)
	Long findLastIdOrderByIdDesc();

	@Query(
		value = """
			SELECT * FROM media
			WHERE checked = 0
			ORDER BY id ASC
			LIMIT :limit
			;
		""",
		nativeQuery = true
	)
	List<RawMediaEntity> findOldestUnCheckedMediaOrderByIdAsc(@Param("limit") int limit);

	@Modifying
	@Query(
		value = "UPDATE media SET checked = 1 WHERE id = :id",
		nativeQuery = true
	)
	void updateChecked(@Param("id") Long id);

}
