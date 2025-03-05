package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.dto.req.CustomerRegisterDTO;
import com.backend.dto.res.CustomerAdminDTO;
import com.backend.dto.res.CustomerDTO;
import com.backend.entities.Customer;
import com.backend.entities.District;
import com.backend.repository.DistrictRepository;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CustomerMapper {

    @Autowired
    private DistrictRepository districtRepository;

    @Mapping(target = "address.district", source = "address.district.name")
    @Mapping(target = "address.province", source = "address.district.province.name")
    @Mapping(target = "address.districtId", source = "address.district.id")
    @Mapping(target = "address.provinceId", source = "address.district.province.id")
    public abstract CustomerDTO toDTO(Customer entity);

    @ToEntity
    @Mapping(target = "address.id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "address.district", expression = "java( getDistrictById(addressDTO.getDistrictId()) )")
    @Mapping(target = "address.provinceName", expression = "java( getProvinceNameByDistrictId(addressDTO.getDistrictId()) )")
    public abstract void updateEnitty(@MappingTarget Customer customer, CustomerRegisterDTO dto);

    @Mapping(target = "address.district", source = "address.district.name")
    @Mapping(target = "address.province", source = "address.district.province.name")
    public abstract CustomerAdminDTO toAdminDTO(Customer entity);

    protected District getDistrictById(long id) {
        return districtRepository.getReferenceById(id);
    }

    protected String getProvinceNameByDistrictId(long id) {
        return districtRepository.findProvinceNameById(id);
    }

}
