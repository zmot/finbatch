package com.tm.finbatch.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ValueMultiplier implements ItemProcessor<Instrument, Instrument> {
    private static final Logger LOG = LoggerFactory.getLogger(ValueMultiplier.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Instrument process(Instrument instrument) throws Exception {
        Double multiplier = jdbcTemplate
                .query("SELECT multiplier FROM instrument_price_modifier WHERE type=?", new Object[]{instrument.type}, resultSet -> resultSet
                        .next() ? resultSet.getDouble("multiplier") : null);
        if (multiplier != null) {
            double updatedValue = instrument.getAmount() * multiplier;
            LOG.info("Multiplier {} found for instrument {}, old value={}, new value={}", multiplier, instrument.getType(), instrument
                    .getAmount(), updatedValue);
            instrument.setAmount(updatedValue);
        } else {
            LOG.info("Multiplier not found for instrument {}, processing as is", instrument.getType());
        }
        return instrument;
    }
}
