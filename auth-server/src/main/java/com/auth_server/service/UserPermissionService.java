package com.auth_server.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.auth_server.dto.req.UpdatePermissionReq;
import com.auth_server.dto.res.PermissionListDTO;
import com.auth_server.dto.res.UserManagerListDTO;
import com.auth_server.dto.res.UserPermissionDTO;
import com.auth_server.entity.UserPermission;
import com.auth_server.exception.ResourceNotFoundException;
import com.auth_server.mapper.UserPermissionMapper;
import com.auth_server.repository.UserPermissionRepository;
import com.auth_server.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPermissionService {

    private final UserPermissionRepository repository;

    private final UserRepository userRepository;

    private final UserPermissionMapper mapper;

    public List<UserManagerListDTO> findAll() {
        return mapper.toManagerDTOs(repository.findAllManager());
    }

    public UserPermissionDTO findPermissionById(Long id) {
        return userRepository.findById(id)
                .map(u -> {
                    UserPermissionDTO dto = mapper.toUserDTO(u);
                    List<PermissionListDTO> permissions = findPermissionListByUserId(id);
                    dto.setPermissions(permissions);
                    return dto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("UserId = " + id + " Not Found"));
    }

    public List<PermissionListDTO> findPermissionListByUserId(Long userId) {
        return repository.findDTOsByUserId(userId);
    }

    @Transactional
    public UserPermissionDTO updatePermission(long userId, UpdatePermissionReq req) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User Id = " + userId + " Not found");
        }
        Set<UserPermission> reqP = mapper.toEntitys(req.getPermissions(), userId);
        List<UserPermission> oldPs = repository.findByUserId(userId);
        for (UserPermission oldP : oldPs) {
            if (!reqP.contains(oldP)) {
                if (oldP.getRevokedAt() == null) {
                    oldP.setRevokedAt(LocalDateTime.now());
                }
            } else {
                if (oldP.getRevokedAt() != null) {
                    oldP.setRevokedAt(null);
                }
                reqP.remove(oldP);
            }
        }

        if (!reqP.isEmpty()) {
            repository.saveAll(reqP);
        }

        return findPermissionById(userId);
    }

}
