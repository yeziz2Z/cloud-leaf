package com.leaf.admin.pojo.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class OrgTreeVO {

    private Long parentId;

    private Long key;

    private String title;

    private List<OrgTreeVO> children;

}
