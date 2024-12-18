package com.backend.utils;

import static com.backend.utils.Utils.getFormatProductCode;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class UtilsTest {
    @Test
    void testIsPrefixOfProductCode_whenValid_shouldTrue() {
        String productCode = "PADDA0042";
        String prefix = "PADDA";
        assertThat(Utils.isPrefixOfProductCode(productCode, prefix)).isTrue();
    }

    @Test
    void testIsPrefixOfProductCode_whenContainPrefixOnly_shouldFalse() {
        String productCode = "PADDA";
        String prefix = "PADDA";
        assertThat(Utils.isPrefixOfProductCode(productCode, prefix)).isFalse();
    }

    @Test
    void testIsPrefixOfProductCode_whenContainWrongPrefix_shouldFalse() {
        String productCode = "PADDAB001";
        String prefix = "PADDA";
        assertThat(Utils.isPrefixOfProductCode(productCode, prefix)).isFalse();
    }

    @Test
    void testIsPrefixOfProductCode_whenContainLessPrefix_shouldFalse() {
        String productCode = "PADDAB001";
        String prefix = "PADDABA";
        assertThat(Utils.isPrefixOfProductCode(productCode, prefix)).isFalse();
    }

    @Test
    void testIsPrefixOfProductCode_whenProductCodeIsNull_shouldFalse() {
        String productCode = null;
        String prefix = "any";
        assertThat(Utils.isPrefixOfProductCode(productCode, prefix)).isFalse();
    }

    void testIsPrefixOfProductCode_whenPrefixIsNull_shouldFalse() {
        String productCode = "PADDAB001";
        String prefix = null;
        assertThat(Utils.isPrefixOfProductCode(productCode, prefix)).isFalse();
    }

    @Test
    void testGetFormatProductCode_ItShouldGeneratePrefixWith5Number() {
        String prefix = "PAD";
        long number = 10;
        assertThat(getFormatProductCode(prefix, number)).isEqualTo("PAD00010");
    }

    
    @Test
    void testGetFormatProductCode_ItShouldGeneratePrefixWith6Number() {
        String prefix = "PAD";
        long number = 123456;
        assertThat(getFormatProductCode(prefix, number)).isEqualTo("PAD123456");
    }

    @Test
    void testGetFormatProductCode_ItShouldGeneratePrefixWithEqual5Number() {
        String prefix = "PAD";
        long number = 12345;
        assertThat(getFormatProductCode(prefix, number)).isEqualTo("PAD12345");
    }

}
