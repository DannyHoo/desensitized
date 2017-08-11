package com.danny.log.desensitized.utils;

import com.danny.log.desensitized.entity.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
/**
 * @author huyuyang@lxfintech.com
 * @Title: DeepCloneTest
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-07-21 14:32:39
 */
public class DeepCloneTest {

    public static void main(String[] args) throws Exception {
        BaseUserInfo baseUserInfo = getUserInfo();
        Object object = deepClone(baseUserInfo);
        System.out.println(object);
    }


    /**
     * 拷贝对象方法
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object deepClone(Object objSource) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (null == objSource) return null;
        if (objSource.getClass().isEnum()) return objSource;
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
            if (isStaticFinal(field)){
                continue;
            }
            try {

                //遍历集合属性
                if (type.isArray()) {//对数组类型的字段进行递归过滤
                    int len = Array.getLength(value);
                    if (len<1) continue;
                    Class<?> c = value.getClass().getComponentType();
                    Array newArray=(Array)Array.newInstance(c,len);
                    for (int i = 0; i < len; i++) {
                        Object arrayObject = Array.get(value, i);
                        Array.set(newArray,i,deepClone(arrayObject));
                    }
                } else if (value instanceof Collection<?>) {
                    Collection newCollection= (Collection) value.getClass().newInstance();
                    Collection<?> c = (Collection<?>) value;
                    Iterator<?> it = c.iterator();
                    while (it.hasNext()) {
                        Object collectionObj = it.next();
                        newCollection.add(deepClone(collectionObj));
                    }
                    field.set(objDes, newCollection);
                    continue;
                } else if (value instanceof Map<?, ?>) {
                    Map newMap=(Map)value.getClass().newInstance();
                    Map<?, ?> m = (Map<?, ?>) value;
                    Set<?> set = m.entrySet();
                    for (Object o : set) {
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                        Object mapVal = entry.getValue();
                        newMap.put(entry.getKey(),deepClone(mapVal));
                    }
                    field.set(objDes, newMap);
                    continue;
                }

                //是否jdk类型或基础类型
                if (isJDKType(field, objSource)
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
                if (value.getClass().isEnum()){
                    field.set(objDes, field.get(objSource));
                    continue;
                }

                //是否自定义类
                if (isUserDefinedType(value.getClass())){
                    field.set(objDes,deepClone(value));
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

    private static boolean isStaticFinal(Field field){
        return Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers());
    }

    /**
     * 是否jdk类型变量
     *
     * @param field
     * @param objSource
     * @return
     * @throws IllegalAccessException
     */
    private static boolean isJDKType(Field field, Object objSource) throws IllegalAccessException {
        Class clazz = field.get(objSource).getClass();
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


    public static BaseUserInfo getUserInfo() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("danny");
        stringList.add("hoo");
        stringList.add("song");
        Map<String, UserTypeEnum> map = new HashMap<String, UserTypeEnum>();
        map.put("dannymap", UserTypeEnum.ADMINISTRATOR);

        List<UserService> userServiceList=new ArrayList<>();
        userServiceList.add(new UserServiceImpl("UserServiceImpl1"));
        userServiceList.add(new UserServiceImpl("UserServiceImpl2"));

        /*单个实体*/
        BaseUserInfo baseUserInfo = new BaseUserInfo()
                .setRealName("胡丹尼")
                .setIdCardNo("158199199013141120")
                .setMobileNo("13579246810")
                .setAccount("dannyhoo123456")
                .setPassword("123456")
                .setBankCardNo("6227000212090659057")
                .setEmail("hudanni6688@126.com")
                .setUserType(UserTypeEnum.ADMINISTRATOR)
                .setUserService(new UserServiceImpl("UserServiceImpl_Danny"))
                .setStrList(stringList)
                .setMap(map)
                .setiLimitKey(LimitFrequencyKeyEnum.SMSCODE_MOBILE_DAY_LIMIT)
                .setUserServiceList(userServiceList);

        /*父类属性*/
        baseUserInfo.setId(101202L)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());

        return baseUserInfo;
    }

    /**
     * COPY对象(毛病还是很多的。。)
     * 对基本类型的过滤
     *
     * @author Lv9
     * @since 2010.03.09
     * baseObject 要拷贝的对象
     * noCopyClassNames 不深度拷贝的对象属性
     */
    public static Object coloneByRef(Object baseObject,
                                     String... noCopyClassNames) throws Exception {
        Object copyObject = baseObject.getClass().newInstance();
        Field[] fields = baseObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (checkClassType(field.getType().getName(), noCopyClassNames)) {
                field.set(copyObject, field.get(baseObject));
            } else {
                field.set(copyObject, coloneByRef(field.get(baseObject),
                        noCopyClassNames));
            }
        }
        return copyObject;
    }

    public static boolean checkClassType(String className,
                                         String[] noCopyClassNames) {
        for (String noCopyClassName : noCopyClassNames) {
            if (className.equals(noCopyClassName)) {
                return true;
            }
        }
        return false;
    }
}
