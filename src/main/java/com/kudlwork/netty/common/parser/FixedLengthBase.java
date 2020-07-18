package com.kudlwork.netty.common.parser;

import com.kudlwork.netty.common.parser.annotation.FixedLengthElement;
import com.kudlwork.netty.common.parser.annotation.FixedLengthElementWrapper;
import com.kudlwork.netty.common.parser.exception.FixedLengthException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class FixedLengthBase {

    public FixedLengthBase() {

    }

    private static final int DEFAULT_SEQ = 999999;

    protected List<Field> getSortThenCheckDistinctSeq(Field[] fields) {
        if(Objects.isNull(fields)) {
            log.error("Not Found Fields");
            throw new FixedLengthException("Not Found Fields");
        }

        List<Field> sortedFields = Arrays.stream(fields)
                .filter(field -> field.getAnnotation(FixedLengthElement.class) != null
                        || field.getAnnotation(FixedLengthElementWrapper.class) != null)
                .sorted(Comparator.comparing(field -> getSeq(field)))
                .collect(Collectors.toList());

        if(isDistinctSeq(sortedFields)) {
            log.error("Duplicate field seq.");
            throw new FixedLengthException("Duplicate field seq.");
        }

        return sortedFields;
    }

    protected int getSeq(Field field) {
        if(Objects.nonNull(field.getAnnotation(FixedLengthElement.class))) {
            return field.getAnnotation(FixedLengthElement.class).seq();
        }

        if(Objects.nonNull(field.getAnnotation(FixedLengthElementWrapper.class))) {
            return field.getAnnotation(FixedLengthElementWrapper.class).seq();
        }

        return DEFAULT_SEQ;
    }

    private boolean isDistinctSeq(List<Field> sortedFields) {
        int distinctSeqCount = sortedFields.stream()
                .collect(Collectors.groupingBy(f -> getSeq(f), Collectors.counting()))
                .size();

        if (sortedFields.size() != distinctSeqCount) {
            return true;
        }

        return false;
    }
}
