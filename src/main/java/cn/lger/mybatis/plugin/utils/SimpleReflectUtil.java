package cn.lger.mybatis.plugin.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2018-12-22.
 */
public class SimpleReflectUtil {

    private SimpleReflectUtil(){}

    public static void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    public static Object getSuperClassFieldValue(Object object, String fieldName) throws IllegalAccessException {
        Class clazz = object.getClass();
        List<Field> fields = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));
        clazz = clazz.getSuperclass();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field: fields){
            if (field.getName().equals(fieldName)){
                field.setAccessible(true);
                return field.get(object);
            }
        }
        return null;
    }

}
