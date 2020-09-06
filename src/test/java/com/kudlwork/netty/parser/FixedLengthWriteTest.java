package com.kudlwork.netty.parser;

import com.kudlwork.netty.common.parser.FixedLengthReader;
import com.kudlwork.netty.common.parser.FixedLengthWriter;
import com.kudlwork.netty.common.tcp.protocol.model.*;
import com.kudlwork.netty.common.tcp.protocol.type.CustomWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class FixedLengthWriteTest {

    private static final int POOL_SIZE = 4;

    @Test
    public void fixedLengthFixedTest() {
        ProductHeader productHeader = mockProductHeader();
        ProductFooter productFooter = mockProductFooter();
        ProductFixedRoot productFixedRoot = new ProductFixedRoot();
        productFixedRoot.setProductHeader(productHeader);
        productFixedRoot.setProductFooter(productFooter);

        String fixedLengthStr = new FixedLengthWriter().write(productFixedRoot);

        log.info(fixedLengthStr);
        assertNotNull(fixedLengthStr);
    }

    @Test
    public void fixedLengthVariableTest() {
        ProductHeader productHeader = mockProductHeader();
        ProductRequest productRequest = mockProductRequest();
        List<ProductItem> productItems = mockProductItems();
        ProductFooter productFooter = mockProductFooter();

        ProductVariableRoot productVariableRoot = new ProductVariableRoot();
        productVariableRoot.setProductHeader(productHeader);
        productVariableRoot.setProductRequest(productRequest);
        productVariableRoot.setProductItems(productItems);
        productVariableRoot.setProductFooter(productFooter);

        String fixedLengthStr = new FixedLengthWriter(productItems.size()).write(productVariableRoot);

        assertNotNull(fixedLengthStr);
    }

    @Test
    public void fixedLengthTest() throws ExecutionException, InterruptedException {
        List<String> data = Collections.synchronizedList(new ArrayList<>());
        List<ProductVariableRootV2> productVariableRoots = Collections.synchronizedList(new ArrayList<>());
        List<ProductVariableRootV2> mockProductVariableRoots = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < 90; i++) {
            ProductHeader productHeader = mockProductHeader();
            ProductRequest productRequest = mockProductRequest(i + 1);
            List<ProductItem> productItems = mockProductItems();
            ProductFooter productFooter = mockProductFooter(i + 1);
            ProductVariableRootV2 productVariableRoot = new ProductVariableRootV2();
            productVariableRoot.setProductHeader(productHeader);
            productVariableRoot.setProductRequest(productRequest);
            productVariableRoot.setProductItems(productItems);
            productVariableRoot.setProductFooter(productFooter);
            mockProductVariableRoots.add(productVariableRoot);
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool(POOL_SIZE);
        forkJoinPool.submit(() -> {
            mockProductVariableRoots.parallelStream().forEach(p -> {
                data.add(new FixedLengthWriter(2).write(p));
            });
        }).get();

        ForkJoinPool joinPool = new ForkJoinPool(POOL_SIZE);
        joinPool.submit(() -> {
            data.parallelStream().forEach(p -> {
                productVariableRoots.add(new FixedLengthReader().read(p, ProductVariableRootV2.class));
            });
        }).get();

        mockProductVariableRoots.sort(Comparator.comparing(x -> x.getProductRequest().getId()));
        productVariableRoots.sort(Comparator.comparing(x -> x.getProductRequest().getId()));

        assertNotNull(data);
        assertNotNull(productVariableRoots);
        assertEquals(mockProductVariableRoots.size(), data.size());
        assertEquals(mockProductVariableRoots.size(), productVariableRoots.size());
        assertEquals(mockProductVariableRoots.get(10).getProductRequest().getId(), productVariableRoots.get(10).getProductRequest().getId());
    }

    private ProductHeader mockProductHeader() {
        ProductHeader productHeader = new ProductHeader();
        productHeader.setCdNum(CustomWrapper.MessageType.REQUEST);
        productHeader.setCoNo(CustomWrapper.BusinessType.DEFAULT);
        productHeader.setInstCd("he");
        return productHeader;
    }

    private ProductRequest mockProductRequest() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setCount(2);
        productRequest.setId(5);
        productRequest.setPrName(new BigDecimal(5.256));
        return productRequest;
    }

    private ProductRequest mockProductRequest(int id) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setCount(2);
        productRequest.setId(id);
        productRequest.setPrName(new BigDecimal(5.256));
        return productRequest;
    }

    private List<ProductItem> mockProductItems() {
        ProductItem productItem = new ProductItem();
        productItem.setCreateAt(LocalDate.now());
        productItem.setName("nam1");

        ProductItem productItem2 = new ProductItem();
        productItem2.setCreateAt(LocalDate.now().minusDays(2));
        productItem2.setName("nam2");
        return Arrays.asList(productItem, productItem2);
    }

    private ProductFooter mockProductFooter(int id) {
        ProductFooter productFooter = new ProductFooter();
        productFooter.setSms("s" + id);
        productFooter.setSmsName("sName");
        return productFooter;
    }

    private ProductFooter mockProductFooter() {
        ProductFooter productFooter = new ProductFooter();
        productFooter.setSms("sms");
        productFooter.setSmsName("sName");
        return productFooter;
    }
}
