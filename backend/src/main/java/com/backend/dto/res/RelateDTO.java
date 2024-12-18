package com.backend.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.RelateInfo}
 */
@Getter
@Setter
@NoArgsConstructor
public class RelateDTO {
    public RelateDTO(long id, String productId, String name, Integer price) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    private long id;
    private String productId;
    private String name;
    // obitan from ProductRelateInfo.product
    private Integer price;

    // Test only
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RelateDTO other = (RelateDTO) obj;
        if (id != other.id)
            return false;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RelateDTO [id=" + id + ", productId=" + productId + ", name=" + name + ", price=" + price + "]";
    }

}