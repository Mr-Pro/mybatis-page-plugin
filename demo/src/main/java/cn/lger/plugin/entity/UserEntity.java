package cn.lger.plugin.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-17.
 */
@Data
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 0L;

    private Integer id;

    private String name;

    private Integer age;


}
