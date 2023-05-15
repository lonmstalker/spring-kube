package io.lonmstalker.springkube.mapper

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface FilterMapper {

}