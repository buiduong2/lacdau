package com.auth_server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth_server.dto.req.UpdatePermissionReq;
import com.auth_server.dto.res.UserManagerListDTO;
import com.auth_server.dto.res.UserPermissionDTO;
import com.auth_server.service.UserPermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/user-permissions")
@RequiredArgsConstructor
public class UserPermissionController {

    private final UserPermissionService service;

    @GetMapping
    public List<UserManagerListDTO> findAllManager() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public UserPermissionDTO findPermissionById(@PathVariable Long id) {
        return service.findPermissionById(id);
    }

    @PutMapping("{id}")
    public UserPermissionDTO updatePermission(@PathVariable Long id,@Valid @RequestBody UpdatePermissionReq req) {
        return service.updatePermission(id, req);
    }
}
