package com.kudlwork.netty.common.tcp.protocol.model;

import com.kudlwork.netty.common.parser.annotation.FieldLengthType;
import com.kudlwork.netty.common.parser.annotation.FixedLengthElementWrapper;
import com.kudlwork.netty.common.parser.annotation.ToFixedLength;
import com.kudlwork.netty.common.parser.annotation.VariableIndexType;
import lombok.Data;

import java.util.List;

@Data
@ToFixedLength(type = VariableIndexType.END, startIndex = 51, size = 1)
public class ProductVariableRootV3 {

    @FixedLengthElementWrapper(seq = 1)
    private ProductHeader productHeader;

    @FixedLengthElementWrapper(seq = 2)
    private ProductRequest productRequest;

    @FixedLengthElementWrapper(seq = 3, type = FieldLengthType.VARIABLE)
    private List<ProductItem> productItems;

    @FixedLengthElementWrapper(seq = 4)
    private ProductFooter productFooter;
}
