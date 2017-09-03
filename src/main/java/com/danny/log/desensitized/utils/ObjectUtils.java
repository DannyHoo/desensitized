package com.danny.log.desensitized.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author huyuyang@lxfintech.com
 * @Title: ObjectUtils
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-09-03 11:36:31
 */
public class ObjectUtils {

    /**
     * 用序列化-反序列化方式实现深克隆
     * 缺点：1、被拷贝的对象必须要实现序列化
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCloneObject(T obj) {
        T t = (T) new Object();
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(obj);
            out.close();
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            t = (T) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 用序列化-反序列化的方式实现深克隆
     * 缺点：1、当实体中存在接口类型的参数，并且这个接口指向的实例为枚举类型时，会报错"com.alibaba.fastjson.JSONException: syntax error, expect {, actual string, pos 171, fieldName iLimitKey"
     *
     * @param objSource
     * @return
     */
    public static Object deepCloneByFastJson(Object objSource) {
        String tempJson = JSON.toJSONString(objSource);
        Object clone = JSON.parseObject(tempJson, objSource.getClass());
        return clone;
    }

    /**
     * 深度克隆对象
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object deepClone(Object objSource) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (null == objSource) return null;
        //是否jdk类型、基础类型、枚举类型
        if (isJDKType(objSource.getClass())
                || objSource.getClass().isPrimitive()
                || objSource instanceof Enum<?>) {
            if ("java.lang.String".equals(objSource.getClass().getName())) {//目前只支持String类型深复制
                return new String((String) objSource);
            } else {
                return objSource;
            }
        }
        // 获取源对象类型
        Class<?> clazz = objSource.getClass();
        Object objDes = clazz.newInstance();
        // 获得源对象所有属性
        Field[] fields = getAllFields(objSource);
        // 循环遍历字段，获取字段对应的属性值
        for (Field field : fields) {
            field.setAccessible(true);
            if (null == field) continue;
            Object value = field.get(objSource);
            if (null == value) continue;
            Class<?> type = value.getClass();
            if (isStaticFinal(field)) {
                continue;
            }
            try {

                //遍历集合属性
                if (type.isArray()) {//对数组类型的字段进行递归过滤
                    int len = Array.getLength(value);
                    if (len < 1) continue;
                    Class<?> c = value.getClass().getComponentType();
                    Array newArray = (Array) Array.newInstance(c, len);
                    for (int i = 0; i < len; i++) {
                        Object arrayObject = Array.get(value, i);
                        Array.set(newArray, i, deepClone(arrayObject));
                    }
                } else if (value instanceof Collection<?>) {
                    Collection newCollection = (Collection) value.getClass().newInstance();
                    Collection<?> c = (Collection<?>) value;
                    Iterator<?> it = c.iterator();
                    while (it.hasNext()) {
                        Object collectionObj = it.next();
                        newCollection.add(deepClone(collectionObj));
                    }
                    field.set(objDes, newCollection);
                    continue;
                } else if (value instanceof Map<?, ?>) {
                    Map newMap = (Map) value.getClass().newInstance();
                    Map<?, ?> m = (Map<?, ?>) value;
                    Set<?> set = m.entrySet();
                    for (Object o : set) {
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                        Object mapVal = entry.getValue();
                        newMap.put(entry.getKey(), deepClone(mapVal));
                    }
                    field.set(objDes, newMap);
                    continue;
                }

                //是否jdk类型或基础类型
                if (isJDKType(field.get(objSource).getClass())
                        || field.getClass().isPrimitive()
                        || isStaticType(field)
                        || value instanceof Enum<?>) {
                    if ("java.lang.String".equals(value.getClass().getName())) {//目前只支持String类型深复制
                        field.set(objDes, new String((String) value));
                    } else {
                        field.set(objDes, field.get(objSource));
                    }
                    continue;
                }

                //是否枚举
                if (value.getClass().isEnum()) {
                    field.set(objDes, field.get(objSource));
                    continue;
                }

                //是否自定义类
                if (isUserDefinedType(value.getClass())) {
                    field.set(objDes, deepClone(value));
                    continue;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objDes;
    }


    /**
     * 是否静态变量
     *
     * @param field
     * @return
     */
    private static boolean isStaticType(Field field) {
        return field.getModifiers() == 8 ? true : false;
    }

    private static boolean isStaticFinal(Field field) {
        return Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers());
    }

    /**
     * 是否jdk类型变量
     *
     * @param clazz
     * @return
     * @throws IllegalAccessException
     */
    private static boolean isJDKType(Class clazz) throws IllegalAccessException {
        //Class clazz = field.get(objSource).getClass();
        return StringUtils.startsWith(clazz.getPackage().getName(), "javax.")
                || StringUtils.startsWith(clazz.getPackage().getName(), "java.")
                || StringUtils.startsWith(clazz.getName(), "javax.")
                || StringUtils.startsWith(clazz.getName(), "java.");
    }

    /**
     * 是否用户自定义类型
     *
     * @param clazz
     * @return
     */
    private static boolean isUserDefinedType(Class<?> clazz) {
        return
                clazz.getPackage() != null
                        && !StringUtils.startsWith(clazz.getPackage().getName(), "javax.")
                        && !StringUtils.startsWith(clazz.getPackage().getName(), "java.")
                        && !StringUtils.startsWith(clazz.getName(), "javax.")
                        && !StringUtils.startsWith(clazz.getName(), "java.");
    }

    /**
     * 获取包括父类所有的属性
     *
     * @param objSource
     * @return
     */
    public static Field[] getAllFields(Object objSource) {
        /*获得当前类的所有属性(private、protected、public)*/
        List<Field> fieldList = new ArrayList<Field>();
        Class tempClass = objSource.getClass();
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 深度克隆对象
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Deprecated
    public static Object copy(Object objSource) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (null == objSource) return null;
        // 获取源对象类型
        Class<?> clazz = objSource.getClass();
        Object objDes = clazz.newInstance();
        // 获得源对象所有属性
        Field[] fields = getAllFields(objSource);
        // 循环遍历字段，获取字段对应的属性值
        for (Field field : fields) {
            field.setAccessible(true);
            // 如果该字段是 static + final 修饰
            if (field.getModifiers() >= 24) {
                continue;
            }
            try {
                // 设置字段可见，即可用get方法获取属性值。
                field.set(objDes, field.get(objSource));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objDes;
    }
}
