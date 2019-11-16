package com.activiti6.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yezhaoxing
 * @date 2019/11/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private String firstName;

	private String lastName;

	private String password;
}
