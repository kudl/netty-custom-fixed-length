package com.kudlwork.netty.common.tcp.protocol.model;

import com.kudlwork.netty.common.parser.annotation.FixedLengthElement;
import com.kudlwork.netty.common.parser.annotation.ToFixedLength;
import lombok.Data;

@Data
@ToFixedLength
public class Product {

    @FixedLengthElement(seq = 1, maxLength = 1, padChar = '1')
    private Integer id;

    @FixedLengthElement(seq = 3, maxLength = 7)
    private String etc;

    @FixedLengthElement(seq = 2, maxLength = 3)
    private String name;
}
