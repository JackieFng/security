package per.fxt.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Fangxiaoting
 * @date 2021/7/12 17:42
 */
@Data
@TableName("Sys_User")
public class User implements Serializable {

    @TableId(value = "id")
    private Integer id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

}
