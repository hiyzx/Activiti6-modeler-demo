package com.activiti6.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yezhaoxing
 * @date 2019/11/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVo {

	private String id;

	private String name;

	private String applyName;

	private Long day;

	private String remark;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

}
