package com.itmo.nxzage.web4.services;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AreaCheckService {
    public boolean isHit(double x, double y, double r) {

        if (x <= 0 && y >= 0 && (x * x + y * y <= r * r)) {
            return true;
        }


        if (x >= 0 && y >= 0 && x <= r / 2 && y <= r - 2 * x) {
            return true;
        }


        if (x >= 0 && x <= r && y <= 0 && y >= -r / 2) {
            return true;
        }

        return false;
    }

}
