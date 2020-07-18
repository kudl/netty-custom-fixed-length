package com.kudlwork.netty.common.tcp.protocol.model;

import com.kudlwork.netty.common.parser.annotation.FixedLengthElement;
import com.kudlwork.netty.common.parser.annotation.ToFixedLength;
import lombok.Data;

import java.time.LocalDate;

@Data
@ToFixedLength
public class ProductItem {

    @FixedLengthElement(seq = 1, maxLength = 5)
    private String name;

    @FixedLengthElement(seq = 2, maxLength = 15)
    private LocalDate createAt;
}
