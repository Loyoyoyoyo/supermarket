package com.lolo.supermarket.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lolo.supermarket.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT name FROM role WHERE id IN(\n" +
            "\tSELECT rid FROM user_role WHERE uid =(\n" +
            "\tSELECT id FROM user WHERE username = #{principal}))")
    List<String> RoleInfoByUserMapper(@Param("principal") String principal);

    @Select({
            "<script>",
            "select info from permission where id in(",
            "select pid from role_permission where rid in(",
            "select id from role where name in",
            "<foreach collection='roles' item='name' open='(' separator=',' close=')'>",
            "#{name}",
            "</foreach>",
            "))",
            "</script>"
    })
    List<String> PermissionInfoByRoleMapper(@Param("roles") List<String> roles);

}
