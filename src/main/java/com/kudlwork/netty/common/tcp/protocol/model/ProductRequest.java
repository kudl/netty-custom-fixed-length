package com.kudlwork.netty.common.tcp.protocol.model;

import com.kudlwork.netty.common.parser.annotation.FixedLengthElement;
import com.kudlwork.netty.common.parser.annotation.ToFixedLength;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ToFixedLength
public class ProductRequest {

    @FixedLengthElement(seq = 1, maxLength = 5)
    private Integer id;

    @FixedLengthElement(seq = 2, maxLength = 20)
    private BigDecimal prName;

    @FixedLengthElement(seq = 3, maxLength = 5)
    private Integer count;
}
