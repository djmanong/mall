package com.djmanong.mall.cms.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 专题分类表
 * </p>
 *
 * @author djmanong
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cms_subject_category")
@ApiModel(value="SubjectCategory对象", description="专题分类表")
public class SubjectCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @ApiModelProperty(value = "分类图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "专题数量")
    @TableField("subject_count")
    private Integer subjectCount;

    @TableField("show_status")
    private Integer showStatus;

    @TableField("sort")
    private Integer sort;


}
