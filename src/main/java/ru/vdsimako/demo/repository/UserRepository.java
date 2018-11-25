package ru.vdsimako.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.vdsimako.demo.model.User;
import ru.vdsimako.demo.model.enums.UserStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findAll();

    List<User> findAllByUserStatus(UserStatus status);

    Optional<User> findById(Long id);
}
