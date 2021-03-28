package me.travelplan.service.route;

import me.travelplan.service.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByCreatedBy(User user);
}