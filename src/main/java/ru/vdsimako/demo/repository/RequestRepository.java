package ru.vdsimako.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.vdsimako.demo.model.Request;

@Repository
public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {
}
