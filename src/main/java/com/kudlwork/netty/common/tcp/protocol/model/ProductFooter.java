package com.kudlwork.netty.common.tcp.protocol.model;

import com.kudlwork.netty.common.parser.annotation.FixedLengthElement;
import com.kudlwork.netty.common.parser.annotation.ToFixedLength;
import lombok.Data;

@Data
@ToFixedLength
public class ProductFooter {

    @FixedLengthElement(seq = 1, maxLength = 5)
    private String sms;

    @FixedLengthElement(seq = 2, maxLength = 5)
    private String smsName;
}
