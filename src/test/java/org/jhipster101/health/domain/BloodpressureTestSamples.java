package org.jhipster101.health.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BloodpressureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Bloodpressure getBloodpressureSample1() {
        return new Bloodpressure().id(1L).systolic(1).diastolic(1);
    }

    public static Bloodpressure getBloodpressureSample2() {
        return new Bloodpressure().id(2L).systolic(2).diastolic(2);
    }

    public static Bloodpressure getBloodpressureRandomSampleGenerator() {
        return new Bloodpressure()
            .id(longCount.incrementAndGet())
            .systolic(intCount.incrementAndGet())
            .diastolic(intCount.incrementAndGet());
    }
}
