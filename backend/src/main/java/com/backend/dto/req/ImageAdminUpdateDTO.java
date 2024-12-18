package com.backend.dto.req;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImageAdminUpdateDTO {

    @Size(min = 1)
    private List<ImageCreateDTO> createThumbnails;

    private List<ImageEditDTO> editThumbnails;

    @Getter
    @Setter
    @ToString
    public static class ImageEditDTO {
        @NotNull
        private Long id;
        @NotNull
        private Integer position;
    }

    @Getter
    @Setter
    @ToString
    public static class ImageCreateDTO {
        @NotNull
        private Integer position;
    }
}
