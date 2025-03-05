package com.backend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.dto.req.EmployeeRegisterDTO;
import com.backend.dto.res.EmployeeAdminDTO;
import com.backend.dto.res.EmployeeAdminListDTO;
import com.backend.dto.res.EmployeeDTO;
import com.backend.entities.District;
import com.backend.entities.Employee;
import com.backend.repository.DistrictRepository;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EmployeeMapper {

    @Autowired
    private DistrictRepository districtRepository;

    @Mapping(target = "address.district", source = "address.district.name")
    @Mapping(target = "address.province", source = "address.district.province.name")
    @Mapping(target = "address.districtId", source = "address.district.id")
    @Mapping(target = "address.provinceId", source = "address.district.province.id")
    public abstract EmployeeDTO toDTO(Employee entity);

    @ToEntity
    @Mapping(target = "address.id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "address.district", expression = "java( getDistrictById(addressDTO.getDistrictId()) )")
    @Mapping(target = "address.provinceName", expression = "java( getProvinceNameByDistrictId(addressDTO.getDistrictId()) )")
    public abstract void updateEnitty(@MappingTarget Employee entity, EmployeeRegisterDTO dto);

    public abstract List<EmployeeAdminListDTO> toAdminListDTOs(List<Employee> employees);

    @Mapping(target = "address.district", source = "address.district.name")
    @Mapping(target = "address.province", source = "address.district.province.name")
    public abstract EmployeeAdminDTO toAdminDTO(Employee employee);

    protected District getDistrictById(long id) {
        return districtRepository.getReferenceById(id);
    }

    protected String getProvinceNameByDistrictId(long id) {
        return districtRepository.findProvinceNameById(id);
    }
}
