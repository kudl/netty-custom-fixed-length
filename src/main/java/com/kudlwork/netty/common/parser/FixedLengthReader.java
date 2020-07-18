package com.kudlwork.netty.common.parser;

import com.kudlwork.netty.common.parser.annotation.*;
import com.kudlwork.netty.common.parser.editor.CustomPropertyEditorManager;
import com.kudlwork.netty.common.parser.exception.FixedLengthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static com.kudlwork.netty.common.CommonConstants.DEFAULT_CHARSET;

@Slf4j
@SuppressWarnings("unchecked")
public class FixedLengthReader extends FixedLengthBase {

    private int variableCount = 0;
    private String content = "";
    private PropertyEditor propertyEditor;

    public FixedLengthReader() {

    }

    public <T> T read(String content, Class<T> clazz) {
        try {
            this.content = content;
            return invokeAnnotations(clazz);
        } catch (Exception e) {
            log.error("Fixed Length Read Fail", e);
            throw new FixedLengthException("Fixed Length Read Fail", e);
        }
    }

    private <T> T invokeAnnotations(Class<T> clazz) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, UnsupportedEncodingException {
        if (StringUtils.isEmpty(content)) {
            log.error("Empty content");
            throw new FixedLengthException("Empty content");
        }

        Object object = clazz.getDeclaredConstructor().newInstance();

        ToFixedLength toFixedLength = object.getClass().getAnnotation(ToFixedLength.class);
        if (Objects.isNull(toFixedLength)) {
            return null;
        }

        List<Field> sortedFields = getSortThenCheckDistinctSeq(object.getClass().getDeclaredFields());

        for (Field field : sortedFields) {
            FixedLengthElementWrapper elementWrapper = field.getAnnotation(FixedLengthElementWrapper.class);
            FixedLengthElement element = field.getAnnotation(FixedLengthElement.class);

            field.setAccessible(true);

            if (Objects.nonNull(elementWrapper)) {
                field.set(object, elementWrapperMapping(field, elementWrapper));
            }

            if (Objects.nonNull(element)) {
                field.set(object, elementMapping(field, element));
            }
        }

        return (T) object;
    }

    private Object elementWrapperMapping(Field field, FixedLengthElementWrapper elementWrapper)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, UnsupportedEncodingException {

        if (elementWrapper.type().equals(FieldLengthType.VARIABLE) && field.getType().equals(List.class)) {
            List<Object> fieldGroup = new ArrayList<>();

            for (int i = 0; i < variableCount; i++) {
                fieldGroup.add(invokeAnnotations((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
            }

            return fieldGroup;
        } else {
            return invokeAnnotations(field.getType());
        }
    }

    private Object elementMapping(Field field, FixedLengthElement element) throws UnsupportedEncodingException {
        Object fieldObject = extract(field, element.maxLength(), element.padChar());

        if(element.type().equals(FeatureType.VARIABLE_COUNT)) {
            variableCount = (int) fieldObject;
        }

        return fieldObject;
    }

    private Object extract(Field field, int maxLength, char padChar) throws UnsupportedEncodingException {
        StringBuffer fieldValue = new StringBuffer();
        StringBuffer newContent = new StringBuffer();

        AtomicInteger index = new AtomicInteger();
        for(char ch : content.toCharArray()) {
            if(maxLength >= index.addAndGet(String.valueOf(ch).getBytes(DEFAULT_CHARSET).length)) {
                fieldValue.append(ch);
            } else {
                newContent.append(ch);
            }
        }

        content = newContent.toString();
        findPropertyEditor(field);

        propertyEditor.setAsText(fieldValue.substring(getFieldIndex(fieldValue.toString(), padChar)));
        return propertyEditor.getValue();
    }

    private void findPropertyEditor(Field field) {
        propertyEditor = CustomPropertyEditorManager.defaultEditors().get(field.getType());
        if(Objects.isNull(propertyEditor)) {
            propertyEditor = PropertyEditorManager.findEditor(field.getType());
        }
    }

    private int getFieldIndex(String fieldValue, char padChar) {
        char[] fieldChar = fieldValue.toCharArray();

        for(int i = 0; i < fieldChar.length; i++) {
            if(i == fieldChar.length - 1 && fieldChar[i] == padChar) {
                return i + 1;
            }

            if(i == fieldChar.length - 1 || fieldChar[i] != padChar) {
                return i;
            }

            if(fieldChar[i + 1] != padChar) {
                return i + 1;
            }
        }
        return 0;
    }
}
