package com.cperazzolli.algasensors.device.management.common;

import io.hypersistence.tsid.TSID;

import java.util.Optional;

public class IDGenerator {

    private static final TSID.Factory tsIdFactory;

    static {
        Optional.ofNullable(System.getenv("tsid.node"))
                        .ifPresent(tsNode -> System.setProperty("tsid.node", tsNode));
        Optional.ofNullable(System.getenv("tsid.node.count"))
                .ifPresent(tsNodeCount -> System.setProperty("tsid.node.count", tsNodeCount));
        tsIdFactory = TSID.Factory.builder().build();
    }
    private IDGenerator() {
    }

    public static TSID generateId() {
        return tsIdFactory.generate();
    }
}
