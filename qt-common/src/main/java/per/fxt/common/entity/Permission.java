package per.fxt.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Fangxiaoting
 * @date 2021/7/12 17:29
 */
@Data
@TableName("Sys_permission")
public class Permission implements Serializable {

    @TableId(value = "id")
    private Integer id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "description")
    private String description;

    @TableField(value = "url")
    private String url;

    @TableField(value = "pid")
    private Integer pid;
}
