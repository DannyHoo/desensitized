package com.danny.log.desensitized.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.danny.log.desensitized.DeepCloneUtil;
import com.danny.log.desensitized.entity.*;
import org.junit.Test;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;
import net.sf.cglib.beans.BeanCopier;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author huyuyang@lxfintech.com
 * @Title: DesensitizedUtilsTest
 * @Copyright: Copyright (c) 2016
 * @Description:
 * @Company: lxjr.com
 * @Created on 2017-07-04 22:40:57
 * 待完善日志脱敏问题 bg项目查询菜单时报错 由org.springframework.validation.support.BindingAwareModelMap引起
 */
public class DesensitizedUtilsTest {

    /**
     * 实体脱敏序列化测试
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public void testUserInfo() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

        Integer i1=1222;
        Integer i2=1222;
        boolean e=i1.equals(i2);

        List<String> stringList = new ArrayList<String>();
        stringList.add("danny");
        stringList.add("hoo");
        stringList.add("song");
        Map<String,UserTypeEnum> map=new HashMap<String, UserTypeEnum>();
        map.put("dannymap",UserTypeEnum.ADMINISTRATOR);

        Map<String,UserTypeEnum> map1=new HashMap<String, UserTypeEnum>();
        map.put("dannymap",UserTypeEnum.ADMINISTRATOR);
        Map<String,UserTypeEnum> map2=new HashMap<String, UserTypeEnum>();
        map.put("dannymap",UserTypeEnum.ADMINISTRATOR);
        List<Map> mapList= new ArrayList<>();
        mapList.add(map1);
        mapList.add(map2);

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
                .setUserService(new UserServiceImpl())
                .setStrList(stringList)
                .setMap(map)
                .setiLimitKey(LimitFrequencyKeyEnum.SMSCODE_MOBILE_DAY_LIMIT)
                .setMapList(mapList);

        /*父类属性*/
        baseUserInfo.setId(101202L)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());

        /*嵌套实体*/
        UserPackage userPackage = new UserPackage()
                .setFlag(true)
                .setBaseUserInfo(baseUserInfo)
                .setUserPackageName("UserPackageName_Danny");


        //System.out.println("脱敏前：" + JSON.toJSONString(baseUserInfo, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
        System.out.println("脱敏后：" + DesensitizedUtils.getJson(baseUserInfo));
        System.out.println("脱敏后：" + DesensitizedUtils.getJson(userPackage));
        System.out.println("原对象：" + JSON.toJSONString(baseUserInfo));

    }


    /**
     * 中文姓名脱敏测试：李先生 → 李**
     */
    @Test
    public void testChineseNameString() {
        System.out.println(DesensitizedUtils.chineseName("李先生"));
    }

    /**
     * 身份证号脱敏测试：130421198807120516 → **************0516
     */
    @Test
    public void testIdCardNum() {
        System.out.println(DesensitizedUtils.idCardNum("130421198807120516"));
    }

    /**
     * 固定电话脱敏测试：01086323500 → *******3500
     */
    @Test
    public void testFixedPhone() {
        System.out.println(DesensitizedUtils.fixedPhone("01086323500"));
    }

    /**
     * 手机号码脱敏测试：13579246810 → 135****6810
     */
    @Test
    public void testMobilePhone() {
        System.out.println(DesensitizedUtils.mobilePhone("13579246810"));
    }

    /**
     * 地址脱敏测试：北京市海淀区丹棱街6号丹棱SOHO → 北京市海淀区丹棱街********
     */
    @Test
    public void testAddress() {
        System.out.println(DesensitizedUtils.address("北京市海淀区丹棱街6号丹棱SOHO", 8));
    }

    /**
     * 电子邮箱脱敏测试：danny@126.com → d****@126.com
     */
    @Test
    public void testEmail() {
        System.out.println(DesensitizedUtils.email("danny@126.com"));
    }

    /**
     * 银行卡号脱敏测试：6224121206590423059 → 622412*********3059
     */
    @Test
    public void testBankCard() {
        System.out.println(DesensitizedUtils.bankCard("6224121206590423059"));
    }

    /**
     * 密码脱敏测试：123456 → ******
     */
    @Test
    public void testPassword() {
        System.out.println(DesensitizedUtils.password("123456"));
    }


}
