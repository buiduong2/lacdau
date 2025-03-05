package com.backend.dto.res;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvinceDTO {

    private long id;

    private String name;

    private List<DistrictDTO> districts;

    @Getter
    @Setter
    public static class DistrictDTO {

        private long id;

        private String name;
    }
}
