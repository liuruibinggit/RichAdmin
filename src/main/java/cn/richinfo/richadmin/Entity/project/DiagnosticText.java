package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class DiagnosticText implements Serializable{

    private Integer diagnosticTextId;
    private String projectNum;
    private String projectName;
    private String describe;
    private String conclusion;
    private String suggest;
    private String warnType;
    private Date month;
}
