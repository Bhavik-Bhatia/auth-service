package com.ab.auth.actuator.metrics;

import com.ab.auth.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * /actuator/metrics/number.of.signups
 * /actuator/metrics/number.of.current.user
 *

 */
@Component
public class AuthMetrics {

    private Counter numberOfSignUps;

    private Gauge numberOfCurrentUsers;

    private final UserRepository userRepository;

    @Autowired
    public AuthMetrics(MeterRegistry meterRegistry, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.numberOfSignUps = Counter.builder("number.of.signups").register(meterRegistry);
        this.numberOfCurrentUsers = Gauge.builder("number.of.current.user", userRepository, UserRepository::findAllUsersCount).register(meterRegistry);
    }

    public void incrementNumberOfSignUps() {
        numberOfSignUps.increment();
    }

}
