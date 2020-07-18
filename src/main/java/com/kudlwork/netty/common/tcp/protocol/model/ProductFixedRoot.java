package com.kudlwork.netty.common.tcp.protocol.model;

import com.kudlwork.netty.common.parser.annotation.FixedLengthElementWrapper;
import com.kudlwork.netty.common.parser.annotation.ToFixedLength;
import lombok.Data;

@Data
@ToFixedLength
public class ProductFixedRoot {

    @FixedLengthElementWrapper(seq = 1)
    private ProductHeader productHeader;

    @FixedLengthElementWrapper(seq = 2)
    private ProductFooter productFooter;
}
