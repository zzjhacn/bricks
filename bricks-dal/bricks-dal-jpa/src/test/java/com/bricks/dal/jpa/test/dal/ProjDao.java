package com.bricks.dal.jpa.test.dal;

import org.springframework.data.repository.Repository;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface ProjDao extends Repository<Proj, Long> {

	Proj save(Proj proj);

	Proj findById(Long id);

}
