package com.lolo.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}
