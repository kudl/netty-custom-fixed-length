package com.kudlwork.netty.common.parser.editor;

import com.kudlwork.netty.common.tcp.protocol.type.CommonTypeWrapper;
import com.kudlwork.netty.common.tcp.protocol.type.CustomWrapper;

import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class CustomPropertyEditorManager {

    public static Map<Class<?>, PropertyEditor> defaultEditors() {
        Map<Class<?>, PropertyEditor> defaultEditors = new HashMap<>();
        defaultEditors.put(LocalDate.class, new LocalDateEditor());
        defaultEditors.put(LocalTime.class, new LocalTimeEditor());
        defaultEditors.put(BigDecimal.class, new BigDecimalEditor());

        defaultEditors.put(CommonTypeWrapper.BooleanSpaceYNType.class, new EnumTypeEditor(CommonTypeWrapper.BooleanSpaceYNType.class));
        defaultEditors.put(CommonTypeWrapper.BooleanYNType.class, new EnumTypeEditor(CommonTypeWrapper.BooleanYNType.class));

        defaultEditors.put(CustomWrapper.MessageType.class, new EnumTypeEditor(CustomWrapper.MessageType.class));
        defaultEditors.put(CustomWrapper.BusinessType.class, new EnumTypeEditor(CustomWrapper.BusinessType.class));
        return defaultEditors;
    }
}
