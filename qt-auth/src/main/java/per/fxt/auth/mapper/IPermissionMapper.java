package per.fxt.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import per.fxt.auth.entity.Permission;

import java.util.List;

/**
 * @author Fangxiaoting
 * @date 2021/7/12 17:49
 */
public interface IPermissionMapper extends BaseMapper<Permission> {

    List<Permission> findByAdminUserId(@Param("userId") Integer userId);
}
