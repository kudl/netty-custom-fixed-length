package com.kudlwork.netty.common.parser.editor;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Slf4j
public class LocalDateEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        LocalDate localDate = (LocalDate) getValue();
        return Objects.isNull(localDate) ? "" : localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @Override
    public void setAsText(String text) {
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (DateTimeParseException e) {
            log.warn("LocalDate setAsText {}", localDate, e);
            localDate = LocalDate.parse(text);
        }

        setValue(localDate);
    }
}
