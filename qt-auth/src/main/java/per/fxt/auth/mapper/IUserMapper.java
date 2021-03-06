package per.fxt.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import per.fxt.auth.entity.User;

/**
 * @author Fangxiaoting
 * @date 2021/7/12 17:56
 */
public interface IUserMapper extends BaseMapper<User> {

    User findByUserName(@Param("username") String username);
}
