package com.kudlwork.netty.common.parser.editor;

import com.kudlwork.netty.common.tcp.protocol.type.EnumType;

import java.beans.PropertyEditorSupport;
import java.util.Objects;

public class EnumTypeEditor extends PropertyEditorSupport {
    private Class<?> fieldType;

    public EnumTypeEditor(Class<?> fieldType){
        this.fieldType = fieldType;
    }

    @Override
    public String getAsText() {
        EnumType enumType = (EnumType)getValue();
        return enumType.readValue();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        EnumType[] enumTypes = (EnumType[])this.fieldType.getEnumConstants();
        EnumType noneType = null;
        for(EnumType enumType : enumTypes){
            if(enumType.toString().equals(text) || enumType.readValue().equals(text)){
                setValue(enumType);
                return;
            }

            if(enumType.toString().equals("NONE") || enumType.readValue().equals("NONE")){
                noneType = enumType;
            }
        }

        if(Objects.isNull(noneType)) {
            throw new IllegalArgumentException("No Such EnumType: " + text);
        }

        setValue(noneType);
    }
}
