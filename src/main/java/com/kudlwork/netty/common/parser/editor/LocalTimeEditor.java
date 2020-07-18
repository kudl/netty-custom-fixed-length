package com.kudlwork.netty.common.parser.editor;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyEditorSupport;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Slf4j
public class LocalTimeEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        LocalTime localTime = (LocalTime) getValue();
        return Objects.isNull(localTime) ? "" : localTime.format(DateTimeFormatter.ofPattern("HHmmss"));
    }

    @Override
    public void setAsText(String text) {
        LocalTime localTime = null;
        try {
            localTime = LocalTime.parse(text, DateTimeFormatter.ofPattern("HHmmss"));
        } catch (DateTimeParseException e) {
            log.warn("LocalTime setAsText {}", localTime, e);
            localTime = LocalTime.parse(text);
        }
        setValue(localTime);
    }
}
