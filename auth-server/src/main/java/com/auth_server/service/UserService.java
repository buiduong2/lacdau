package com.auth_server.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.auth_server.dto.req.UserEditReq;
import com.auth_server.dto.req.UserFilter;
import com.auth_server.dto.res.UserListDTO;
import com.auth_server.dto.res.UserProfile;
import com.auth_server.entity.User;
import com.auth_server.exception.ResourceNotFoundException;
import com.auth_server.mapper.UserMapper;
import com.auth_server.repository.UserRepository;
import com.auth_server.utils.UserSpecs;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserSpecs specs;

    private final UserMapper mapper;

    public UserProfile getProfileById(long id) {
        return repository.findById(id).map(mapper::toProfileDTO)
                .orElseThrow(() -> new RuntimeException("SERVER NOT FOUND"));
    }

    public Page<UserListDTO> findAll(UserFilter filter, Pageable pageable) {
        Pageable pageOnlyPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<User> page = repository.findAll(specs.byFilter(filter, pageable), pageOnlyPageable);
        Page<UserListDTO> pageDTO = page.map(mapper::toListDTO);
        List<Long> ids = page.get().map(User::getId).toList();
        Map<Long, Boolean> idAndIsModerate = repository.findIsModerateByIdIn(ids);

        return pageDTO.map(user -> mapper.updateModerateListDTO(user, idAndIsModerate));
    }

    private User findById(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    @Transactional
    public void edit(long id, UserEditReq req) {
        User user = findById(id);
        mapper.updateEntity(user, req);
        mapper.updateEmailEntity(user, req.getEmail());
    }

    public UserListDTO findDTOById(long id) {
        User user = findById(id);
        UserListDTO dto = mapper.toListDTO(user);
        Map<Long, Boolean> idAndIsModerate = repository.findIsModerateByIdIn(List.of(id));

        return mapper.updateModerateListDTO(dto, idAndIsModerate);
    }

}
