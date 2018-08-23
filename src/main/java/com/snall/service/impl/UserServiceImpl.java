package com.snall.service.impl;

import com.snall.common.Const;
import com.snall.common.ServerResponse;
import com.snall.dao.UserMapper;
import com.snall.pojo.User;
import com.snall.service.IUserService;
import com.snall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount=userMapper.checkUsername(username);

        if (resultCount==0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //todo 密码登陆MD5 现在据说有MD5碰撞破解
        String md5Password= MD5Util.MD5EncodeUtf8(password);
        User user=userMapper.selectLogin(username,md5Password);

        //User user=userMapper.selectLogin(username,password);
        if (user==null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
       /* int resultCount=userMapper.checkUsername(user.getUsername());
        if (resultCount>0){
            return ServerResponse.createByErrorMessage("用户名存在");
        }*/
        //this是调用本类中的其他方法
        ServerResponse validResponse=this.checkValid(user.getUsername(),Const.USERNAME);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse=this.checkValid(user.getEmail(),Const.EMAIL);
        if (!validResponse.isSuccess()){
            return validResponse;
        }

      /*  resultCount=userMapper.checkEmail(user.getEmail());
        if (resultCount>0){
            return ServerResponse.createByErrorMessage("email已存在");
        }*/
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount=userMapper.insert(user);
        if (resultCount==0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)){
            //开始校验
            if (Const.USERNAME.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if (resultCount>0){
                    return ServerResponse.createBySuccessMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)){
                int resultCount=userMapper.checkEmail(str);
                if (resultCount>0){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("检验成功");
    }

}
