package com.bricks.dal.jpa.test.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface ProjDao extends Repository<Proj, Long> {

	Proj save(Proj proj);

	Proj findById(Long id);

	Page<Proj> findByIdGreaterThan(Long id, Pageable pg);

	Long countByIdGreaterThan(Long id);
}
