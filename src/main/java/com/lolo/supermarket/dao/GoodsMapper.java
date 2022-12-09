package com.lolo.supermarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.supermarket.bean.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {
}
