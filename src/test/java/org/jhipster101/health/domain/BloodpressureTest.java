package org.jhipster101.health.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jhipster101.health.domain.BloodpressureTestSamples.*;

import org.jhipster101.health.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BloodpressureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bloodpressure.class);
        Bloodpressure bloodpressure1 = getBloodpressureSample1();
        Bloodpressure bloodpressure2 = new Bloodpressure();
        assertThat(bloodpressure1).isNotEqualTo(bloodpressure2);

        bloodpressure2.setId(bloodpressure1.getId());
        assertThat(bloodpressure1).isEqualTo(bloodpressure2);

        bloodpressure2 = getBloodpressureSample2();
        assertThat(bloodpressure1).isNotEqualTo(bloodpressure2);
    }
}
