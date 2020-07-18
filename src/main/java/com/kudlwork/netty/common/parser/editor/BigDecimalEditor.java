package com.kudlwork.netty.common.parser.editor;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class BigDecimalEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        BigDecimal bigDecimal = (BigDecimal) getValue();
        return Objects.isNull(bigDecimal) ? "" : String.valueOf(bigDecimal.setScale(3, RoundingMode.HALF_UP));
    }

    @Override
    public void setAsText(String text) {
        if(StringUtils.isEmpty(text)) {
            setValue(new BigDecimal(0));
            return;
        }

        setValue(new BigDecimal(text));
    }
}
