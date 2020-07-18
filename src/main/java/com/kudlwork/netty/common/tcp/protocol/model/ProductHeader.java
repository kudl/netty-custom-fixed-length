package com.kudlwork.netty.common.tcp.protocol.model;

import com.kudlwork.netty.common.parser.annotation.FixedLengthElement;
import com.kudlwork.netty.common.parser.annotation.ToFixedLength;
import com.kudlwork.netty.common.tcp.protocol.type.CustomWrapper;
import lombok.Data;

@Data
@ToFixedLength
public class ProductHeader {

    @FixedLengthElement(seq = 1, maxLength = 5)
    private String instCd;

    @FixedLengthElement(seq = 2, maxLength = 5)
    private CustomWrapper.BusinessType coNo;

    @FixedLengthElement(seq = 3, maxLength = 5)
    private CustomWrapper.MessageType cdNum;
}
