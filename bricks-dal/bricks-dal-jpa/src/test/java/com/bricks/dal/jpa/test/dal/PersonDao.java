package com.bricks.dal.jpa.test.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface PersonDao extends Repository<Person, Long> {

	Person save(Person proj);

	Person findById(Long id);

	Page<Person> findByIdGreaterThan(Long id, Pageable pg);

	Long countByIdGreaterThan(Long id);

}
