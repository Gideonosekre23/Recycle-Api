package com.Ossolutions.RecycleApi.Repository;
import com.Ossolutions.RecycleApi.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    List<User> findByLocation(String location);
    List<User> findByLocationOrderByPointsDesc(String location);

    List<User> findByOrderByPointsDesc();
}
