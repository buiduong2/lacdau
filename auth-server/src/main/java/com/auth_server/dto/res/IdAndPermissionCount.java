package com.auth_server.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdAndPermissionCount {
    private Long id;
    private boolean isNotEmpty;

    public IdAndPermissionCount(Long id, boolean isNotEmpty) {
        this.id = id;
        this.isNotEmpty = isNotEmpty;
    }

}
