package cn.richinfo.richadmin.Entity.user;

import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class User {

    private Integer id;
    private String userId;
    private String userName;
    private String userPwd;
    private String email;
    private String account;
    private String deptId;
    private String dept;
    private String room;
    private String position;
    private String businessLineId;
    private String businessLine;
    private Date createTime;
    private Date updateTime;
    private String projectNums;
    private List<ApprovalInformation> list;
}
