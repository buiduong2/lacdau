package com.auth_server.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth_server.dto.req.ChangePasswordAdminReq;
import com.auth_server.dto.req.RegisterReq;
import com.auth_server.dto.req.UserEditReq;
import com.auth_server.dto.req.UserFilter;
import com.auth_server.dto.res.UserListDTO;
import com.auth_server.service.SystemUserService;
import com.auth_server.service.UserService;
import com.auth_server.validation.SystemUserOwnerUnique;
import com.auth_server.validation.SystemUserOwnerUnique.Column;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService service;

    private final SystemUserService systemUserService;

    @GetMapping
    public Page<UserListDTO> findAll(@Valid UserFilter filter, @PageableDefault(size = 5) Pageable pageable) {
        return service.findAll(filter, pageable);
    }

    @GetMapping("{id}")
    public UserListDTO findById(@PathVariable long id) {
        return service.findDTOById(id);
    }

    @GetMapping("/check/username/{username}")
    public boolean checkUsernameExists(@PathVariable String username) {
        return systemUserService.checkExistsUsername(username);
    }

    @GetMapping("/check/email/{email}")
    public boolean checkEmailExists(@PathVariable String email) {
        return systemUserService.checkEmailExistsByEmail(email);
    }

    @PostMapping
    public void createUser(@Valid @RequestBody RegisterReq req) {
        systemUserService.register(req);
    }

    @PutMapping("{id}")
    @SystemUserOwnerUnique(column = Column.EMAIL)
    public void editUser(@PathVariable long id, @Valid @RequestBody UserEditReq req) {
        service.edit(id, req);
    }

    @PutMapping("{id}/change-password")
    public void changePassword(@PathVariable long id, @Valid @RequestBody ChangePasswordAdminReq req) {
        systemUserService.changePassword(req, id);
    }

}
