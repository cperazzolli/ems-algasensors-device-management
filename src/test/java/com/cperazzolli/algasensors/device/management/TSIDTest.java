package com.cperazzolli.algasensors.device.management;

import com.cperazzolli.algasensors.device.management.common.IDGenerator;
import io.hypersistence.tsid.TSID;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;




class TSIDTest {

    @Test
    void sholdGenerateTSID() {

        TSID tsid = IDGenerator.generateId();

        Assertions.assertThat(tsid.getInstant()).isCloseTo(Instant.now(), Assertions.within(1, ChronoUnit.MINUTES));

    }
}