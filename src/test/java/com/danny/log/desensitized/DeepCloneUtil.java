package com.danny.log.desensitized;

import com.danny.log.desensitized.entity.UserInfo;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huyuyang@lxfintech.com
 * @Title: DeepCloneUtil
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-07-21 11:52:41
 */
public class DeepCloneUtil {
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InstantiationException {

    }



    /**
     * 返回对象的所以属性 包括继承至父类的属性
     * @param o
     * @return
     */
    private static List<Field> getAllFieads(Object o) {

        List<Field> fields = new ArrayList<Field>();

        if (null == o)
            return fields;

        Class<?> type = o.getClass();
        do {
            for (Field f : type.getDeclaredFields()) {
                fields.add(f);
            }
            type = type.getSuperclass();
        } while (null != type);

        return fields;

    }



    /**
     * 基于效率的考虑不需要进行深度复制的类型：包括基本类型 和 包装类
     * @param o
     * @return
     */
    public static boolean isSimpleObject(Object o) {

        Class<?> type = o.getClass();
        if (type.isPrimitive()) { // 基本类型
            return true;
        }

        // 不可更改的变量类型 如 String，Long
        if (type.equals(String.class))
            return true;
        if (type.equals(Long.class))
            return true;
        if(type.equals(Boolean.class))
            return true;
        if(type.equals(Short.class))
            return true;
        if(type.equals(Integer.class))
            return true;
        if(type.equals(Character.class))
            return true;

        if(type.equals(Float.class))
            return true;

        if(type.equals(Double.class))
            return true;
        if(type.equals(Byte.class))
            return true;

        return false;
    }


    /**
     * 没有对cloneAble接口进行限制
     * @param o
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object cloneObject(Object o) throws IllegalArgumentException, IllegalAccessException, InstantiationException{
        if(null == o)
            return null;
        // 使用Map保存原对象和副本对象之间的结构，防止被多次引用的对象重复重建
        Map<Object,Object> map = new HashMap<Object,Object>();
        return cloneObject(o,map);
    }



    private static Object cloneObject(Object o, Map<Object, Object> map)
            throws IllegalArgumentException, IllegalAccessException,
            InstantiationException {
        if (null == o)
            return null;
        Object newInstance = null;

        newInstance = map.get(o);
        if (null != newInstance) {
            return newInstance;
        }

        if(isSimpleObject(o))
            return o;

        // 数组类型
        if(o.getClass().isArray()){
            return cloneArray(o,map);
        }

        Class<?> type = o.getClass();
        newInstance = type.newInstance();
        map.put(o, newInstance);

        cloneFields(o, newInstance, map);

        return newInstance;
    }

    /**
     * 克隆数组对象
     * @param o
     * @param map
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private static Object cloneArray(Object o,Map<Object, Object> map) throws IllegalArgumentException, IllegalAccessException, InstantiationException{
        if(null == o)
            return null;

        if(!o.getClass().isArray()){
            return cloneObject(o,map);
        }

        int len = Array.getLength(o);

        Object array = Array.newInstance(o.getClass().getComponentType(), len);
        map.put(o, array);

        for(int i = 0; i < len; i++){
            Array.set(array, i, cloneObject(Array.get(o, i),map));
        }

        return array;
    }

    /**
     * 对于final类型的变量 如果其为引用， 则尽管引用的值不需要更改，但引用对象的数据还是需要填充的
     * @param object
     * @param newObject
     * @param map
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static void cloneFinalObject(Object object, Object newObject, Map<Object, Object> map)throws IllegalArgumentException, IllegalAccessException, InstantiationException{
        if(object == null || newObject == null || object == newObject  || !newObject.getClass().equals(newObject.getClass()))
            return ;



        // 对于final类型的变量
        if(null != map.get(newObject)){
            return;
        }
        map.put(newObject, newObject);

        cloneFields(object, newObject, map);


        return ;
    }


    private static void cloneFields(Object object, Object newObject,
                                    Map<Object, Object> map) throws SecurityException,
            IllegalArgumentException, IllegalAccessException,
            InstantiationException {

        if(null == object || null == newObject){
            return ;
        }
        List<Field> fields = getAllFieads(object);

        for (Field f : fields) {
            // 静态变量过滤掉 或者final的变量
            if(Modifier.isStatic(f.getModifiers()))
                continue;

            // 常量
            if(Modifier.isFinal(f.getModifiers())){
                cloneFinalObject(f.get(object),f.get(newObject),map);
            }else{
                f.setAccessible(true);
                f.set(newObject, cloneObject(f.get(object), map));
            }

        }
    }
}
