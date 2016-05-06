package com.bricks.dal.jpa.test.dal;

<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
=======
import org.springframework.data.repository.Repository;
>>>>>>> origin/master

/**
 * @author bricks <long1795@gmail.com>
 */
<<<<<<< HEAD
@Service
=======
>>>>>>> origin/master
public interface ProjDao extends Repository<Proj, Long> {

	Proj save(Proj proj);

	Proj findById(Long id);

<<<<<<< HEAD
	Page<Proj> findByIdGreaterThan(Long id, Pageable pg);

	Long countByIdGreaterThan(Long id);
=======
>>>>>>> origin/master
}
