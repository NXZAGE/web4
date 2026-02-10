package com.itmo.nxzage.web4.services;

import java.time.OffsetDateTime;
import java.util.List;
import com.itmo.nxzage.web4.entity.Hit;
import com.itmo.nxzage.web4.entity.User;
import com.itmo.nxzage.web4.repository.HitRepository;
import com.itmo.nxzage.web4.repository.UserRespository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;

@Stateless
public class HitService {
    @Inject
    private SecurityContext securityContext;

    @Inject
    private AreaCheckService areaChecker;

    @Inject
    private HitRepository hitRepository;

    @Inject
    private UserRespository userRespository;

    public Hit registerHit(double x, double y, double r) {
        // TODO write some validation?
        long start = System.nanoTime();
        var registredAt = OffsetDateTime.now();
        User maker =
                userRespository.findByNameWithRoles(securityContext.getCallerPrincipal().getName()).get();
        boolean result = areaChecker.isHit(x, y, r);
        Hit hit = Hit.builder().user(maker).x(x).y(y).r(r).hit(result).registredAt(registredAt)
                .execTime(System.nanoTime() - start).build();
        hitRepository.save(hit);
        return hit;
    }

    public List<Hit> getHitsOfOwner() {
        return hitRepository
                .getHitsByUser(userRespository
                        .findByNameWithRoles(securityContext.getCallerPrincipal().getName()).get());
    }
}
