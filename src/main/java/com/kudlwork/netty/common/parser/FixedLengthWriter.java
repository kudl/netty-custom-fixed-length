package com.kudlwork.netty.common.parser;

import com.kudlwork.netty.common.parser.annotation.*;
import com.kudlwork.netty.common.parser.editor.CustomPropertyEditorManager;
import com.kudlwork.netty.common.parser.exception.FixedLengthException;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyEditor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.kudlwork.netty.common.CommonConstants.DEFAULT_CHARSET;

@Slf4j
@SuppressWarnings("unchecked")
public class FixedLengthWriter extends FixedLengthBase {

    private int variableCount;
    private PropertyEditor propertyEditor;

    public FixedLengthWriter() {

    }

    public FixedLengthWriter(int variableCount) {
        this.variableCount = variableCount;
    }

    public <T> String write(T object) {
        try {
            return invokeAnnotations(object);
        } catch (Exception e) {
            log.error("Fixed Length Write Fail", e);
            throw new FixedLengthException("Fixed Length Write Fail", e);
        }
    }

    private <T> String invokeAnnotations(T object) throws IllegalAccessException, InvocationTargetException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();

        if (Objects.isNull(object)) {
            log.error("Not Found Target Object");
            throw new FixedLengthException("Not Found Target Object");
        }

        ToFixedLength toFixedLength = object.getClass().getAnnotation(ToFixedLength.class);
        if (Objects.isNull(toFixedLength)) {
            return sb.toString();
        }

        List<Field> sortedFields = getSortThenCheckDistinctSeq(object.getClass().getDeclaredFields());

        for (Field field : sortedFields) {
            FixedLengthElementWrapper elementWrapper = field.getAnnotation(FixedLengthElementWrapper.class);
            FixedLengthElement element = field.getAnnotation(FixedLengthElement.class);

            field.setAccessible(true);

            if (Objects.nonNull(elementWrapper)) {
                sb.append(elementWrapperAggregate(object, field, elementWrapper));
            }

            if (Objects.nonNull(element)) {
                sb.append(elementAggregate(getFieldValue(object, field), element));
            }
        }

        return sb.toString();
    }

    private <T> String getFieldValue(T object, Field field) throws IllegalAccessException, InvocationTargetException {
        if (Objects.isNull(field.get(object))) {
            return "";
        }

        for (Method method : field.getType().getDeclaredMethods()) {
            if (method.getName().equals("readValue")) {
                return (String) method.invoke(field.get(object));
            }
        }

        propertyEditor = CustomPropertyEditorManager.defaultEditors().get(field.getType());
        if (Objects.nonNull(propertyEditor)) {
            propertyEditor.setAsText(String.valueOf(field.get(object)));
            return String.valueOf(propertyEditor.getAsText());
        }

        return field.get(object).toString();
    }

    private String elementWrapperAggregate(Object object, Field field,
                                           FixedLengthElementWrapper elementWrapper) throws IllegalAccessException, InvocationTargetException, UnsupportedEncodingException {
        if (elementWrapper.type().equals(FieldLengthType.VARIABLE) && field.get(object) instanceof List) {
            StringBuffer sb = new StringBuffer();
            List<Object> fieldGroup = Collections.synchronizedList((List) field.get(object));

            for (int i = 0; i < variableCount; i++) {
                sb.append(invokeAnnotations(fieldGroup.get(i)));
            }

            return sb.toString();
        }

        return invokeAnnotations(field.get(object));
    }

    private String elementAggregate(String fieldValue, FixedLengthElement element) {
        if(element.padMode().equals(PadMode.LEFT)) {
            return leftPad(fieldValue, element.maxLength(), element.padChar());
        }

        return rightPad(fieldValue, element.maxLength(), element.padChar());
    }

    private String leftPad(String fieldValue, int fieldMaxLength, char padChar) {
        int fieldByteLength = fieldValue.getBytes(DEFAULT_CHARSET).length;
        if (fieldByteLength > fieldMaxLength) {
            log.error("Field length too long. {}, {}, {}", fieldValue, fieldMaxLength, padChar);
            throw new FixedLengthException("Field length too long.");
        }

        if (fieldByteLength == fieldMaxLength) {
            return fieldValue;
        }

        StringBuffer sb = new StringBuffer();
        while (sb.toString().getBytes(DEFAULT_CHARSET).length < fieldMaxLength - fieldByteLength) {
            sb.append(padChar);
        }
        sb.append(fieldValue);

        if (sb.toString().getBytes(DEFAULT_CHARSET).length != fieldMaxLength) {
            log.error("Field length error {}, {}, {}", fieldValue, fieldMaxLength, padChar);
            throw new FixedLengthException("Field length error");
        }

        return sb.toString();
    }

    private String rightPad(String fieldValue, int fieldMaxLength, char padChar) {
        int fieldByteLength = fieldValue.getBytes(DEFAULT_CHARSET).length;
        if (fieldByteLength > fieldMaxLength) {
            log.error("Field length too long. {}, {}, {}", fieldValue, fieldMaxLength, padChar);
            throw new FixedLengthException("Field length too long.");
        }

        if (fieldByteLength == fieldMaxLength) {
            return fieldValue;
        }

        StringBuffer sb = new StringBuffer(fieldValue);
        for(int i = 0; i < fieldMaxLength - fieldByteLength; i++) {
            sb.append(padChar);
        }

        if (sb.toString().getBytes(DEFAULT_CHARSET).length != fieldMaxLength) {
            log.error("Field length error {}, {}, {}", fieldValue, fieldMaxLength, padChar);
            throw new FixedLengthException("Field length error");
        }

        return sb.toString();
    }
}