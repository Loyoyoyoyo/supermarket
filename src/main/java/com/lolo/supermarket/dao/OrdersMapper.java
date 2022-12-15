package com.lolo.supermarket.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.supermarket.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
