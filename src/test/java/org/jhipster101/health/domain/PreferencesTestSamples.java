package org.jhipster101.health.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PreferencesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Preferences getPreferencesSample1() {
        return new Preferences().id(1L).weekelygoal("weekelygoal1").weightunits("weightunits1");
    }

    public static Preferences getPreferencesSample2() {
        return new Preferences().id(2L).weekelygoal("weekelygoal2").weightunits("weightunits2");
    }

    public static Preferences getPreferencesRandomSampleGenerator() {
        return new Preferences()
            .id(longCount.incrementAndGet())
            .weekelygoal(UUID.randomUUID().toString())
            .weightunits(UUID.randomUUID().toString());
    }
}
