package com.kudlwork.netty.parser;

import com.kudlwork.netty.common.parser.FixedLengthReader;
import com.kudlwork.netty.common.tcp.protocol.model.ProductFixedRoot;
import com.kudlwork.netty.common.tcp.protocol.model.ProductVariableRoot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class FixedLengthReaderTest {

    @Test
    public void fixedLengthFixedTest() {
        String data = "fixed  001 1000  smss0101";

        ProductFixedRoot productFixedRoot = new FixedLengthReader().read(data, ProductFixedRoot.class);

        log.info(productFixedRoot.toString());
        assertNotNull(productFixedRoot);
    }

    @Test
    public void fixedLengthVariableTest() {
        String data = " 1111  001 1000   28               5.256    2 nam1       20200706 nam2       20200704  s28sName";

        ProductVariableRoot productVariableRoot = new FixedLengthReader().read(data, ProductVariableRoot.class);

        log.info(productVariableRoot.toString());
        assertNotNull(productVariableRoot);
    }
}
