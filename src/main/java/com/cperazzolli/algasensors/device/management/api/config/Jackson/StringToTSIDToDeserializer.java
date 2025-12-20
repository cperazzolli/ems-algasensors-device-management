package com.cperazzolli.algasensors.device.management.api.config.Jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.hypersistence.tsid.TSID;

import java.io.IOException;

public class StringToTSIDToDeserializer extends JsonDeserializer<TSID> {
    @Override
    public TSID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return TSID.from(p.getText());
    }
}
