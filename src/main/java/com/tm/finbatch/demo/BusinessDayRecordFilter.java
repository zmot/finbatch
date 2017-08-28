package com.tm.finbatch.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.Set;

class BusinessDayRecordFilter implements ItemProcessor<Instrument, Instrument> {
    private static final Set<DayOfWeek> WEEKEND_DAYS = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    private static final Logger LOG = LoggerFactory.getLogger(BusinessDayRecordFilter.class);

    @Override
    public Instrument process(Instrument instrument) throws Exception {
        LocalDate date = instrument.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (WEEKEND_DAYS.contains(date.getDayOfWeek())) {
            LOG.info("Skipping non business day record: " + instrument);
            return null;
        } else{
            LOG.info("Processing normally: " + instrument);
            return instrument;
        }
    }
}
