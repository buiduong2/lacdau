package com.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.Hibernate.isInitialized;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.dto.res.PriceInfo;
import com.backend.entities.Brand;
import com.backend.entities.Category;
import com.backend.entities.Product;
import com.backend.entities.ProductStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepo;


    @Autowired
    private TestProductRepository repository;

    @Test
    void findDetailById_ItLoadEager() {

        Optional<Product> pOptional = repository.findDetailByProductCodeAndStatus("BPK0005",
                ProductStatus.ACTIVE);
        assertThat(pOptional)
                .isNotNull()
                .get()
                .matches(p -> isInitialized(p.getDetail()))
                .matches(p -> !isInitialized(p.getBrand()))
                .matches(p -> !isInitialized(p.getCategory()))
                .matches(p -> !isInitialized(p.getAttributeValues()))
                .matches(p -> isInitialized(p.getMainImage()))// Có vẻ như OneToOne với FK INdependent
                                                              // là đặc biệt
                .extracting(Product::getDetail)
                .matches(detail -> isInitialized(detail.getThumbnails()));

    }

    @Test
    void findDetailById_ItFetchRightData() {
        Optional<Product> pOptional = repository.findDetailByProductCodeAndStatus("BPK0005",
                ProductStatus.ACTIVE);
        assertThat(pOptional)
                .isNotNull();

        Product p = pOptional.get();
        Category category = categoryRepo.findById(p.getCategory().getId()).get();

        assertThat(p.getProductCode()).isEqualTo("BPK0005");
        assertThat(p.getName()).isEqualTo("COMBO PHÍM CHUỘT GIẢ CƠ LIMEIDE T21 TRẮNG");
        assertThat(p.getViewCount()).isEqualTo(3511);
        assertThat(p.getDetail().getSpecifications()).isEqualTo(String.join(
                "\n",
                new String[] {
                        "Bàn phím giả cơ chuyên game cao cấp Limei T21 cho khả năng ngõ phím tốt, gõ văn bản hay chơi game đều rất tuyệt vời",
                        "Thiết kế đẹp gọn gàng thời trang, hầm hố gaming với dải đèn led đẹp, có khả năng chống nước.",
                        "Phù hợp với cả văn phòng, học tập, chơi game",
                        "Có thể sử dụng 1 lúc nhiều phím",
                        "Có nút khóa phím windows, tránh đụng nhằm rồi bị văng khỏi game",
                        "Kết nối: cổng usb 2.0",
                        "Chữ khắc trên phim rõ nét, chất lượng khá tốt",
                        "Bàn phím hổ trợ đèn LED RAINBOW",
                        "Có thể bấm combo nhiều phím cùng 1 lúc",
                        "Có nhiều phím đa phương tiện thông qua sử dụng kết hợp với nút FN",
                        "Phím bấm khá êm, nhẹ và không gây ồn ào",
                        "Bảo Hành : 12 tháng chính hãng"
                }));

        assertThat(category.getName()).isEqualTo("BÀN PHÍM GIẢ CƠ");

    }

    @Test
    void testFindAllBrandsByCategoryIdIn() {
        List<Brand> brands = repository
                .findAllBrandsByCategoryIdInAndProductStatusIsActive(categoryRepo.findIdsWithIdbyParentId(23L));

        assertThat(brands).hasSize(25);
    }

    @Test
    void testFindMinMaxPriceByCategoryIdIn() {
        List<Long> categoryIds = categoryRepo.findIdsWithIdbyParentId(23L);
        PriceInfo priceInfo = repository
                .findMinMaxPriceByCategoryIdInAndStatusIsActive(categoryIds);
        List<Product> products = repository.findByCategoryIdIn(categoryIds);
        Optional<Integer> min = products.stream()
                .map(p -> p.getSalePrice() != null ? p.getSalePrice() : p.getOriginalPrice())
                .min(Comparator.comparingInt(Integer::valueOf));
        Optional<Integer> max = products.stream()
                .map(p -> p.getSalePrice() != null ? p.getSalePrice() : p.getOriginalPrice())
                .max(Comparator.comparingInt(Integer::valueOf));

        assertThat(priceInfo).isNotNull();
        assertThat(priceInfo.getMin()).isEqualTo(min.get());
        assertThat(priceInfo.getMax()).isEqualTo(max.get());
    }

    @Test
    void testFindForUpdateFullById() {
        Optional<Product> pOptional = repository.findDetailByProductCodeAndStatus("BPK0005", ProductStatus.ACTIVE);

        assertThat(pOptional).isNotNull()
                .get()
                .matches(p -> isInitialized(p.getDetail()))
                .matches(p -> isInitialized(p.getDetail().getThumbnails()))
                .matches(p -> isInitialized(p.getMainImage()));
    }

}
