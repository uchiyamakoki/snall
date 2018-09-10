package com.snall.controller.backend;

import com.google.common.collect.Maps;
import com.snall.common.Const;
import com.snall.common.ResponseCode;
import com.snall.common.ServerResponse;
import com.snall.pojo.Product;
import com.snall.pojo.User;
import com.snall.service.IFileService;
import com.snall.service.IProductService;
import com.snall.service.IUserService;
import com.snall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;


    //ServerResponse 标准的code msg T data
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        //session检测登录
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //如果未登录
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        //判断用户是否拥有权限 数字确定权限，这里是简单的管理员用户区分
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
        //session检测登录
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //如果未登录
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        //判断用户是否拥有权限 数字确定权限，这里是简单的管理员用户区分
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId){
        //session检测登录
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //如果未登录
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        //判断用户是否拥有权限 数字确定权限，这里是简单的管理员用户区分
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iProductService.manageProductDetail(productId);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        //session检测登录
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //如果未登录
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        //判断用户是否拥有权限 数字确定权限，这里是简单的管理员用户区分
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            //return iProductService.manageProductDetail(productId);
            return iProductService.getProductList(pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,String productName ,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        //session检测登录
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //如果未登录
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        //判断用户是否拥有权限 数字确定权限，这里是简单的管理员用户区分
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        //session检测登录
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //如果未登录
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        //判断用户是否拥有权限 数字确定权限，这里是简单的管理员用户区分
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map fileMap= Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);

            return ServerResponse.createBySuccess(fileMap);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap=Maps.newHashMap();
        //session检测登录
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        //如果未登录
        if (user==null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        //判断用户是否拥有权限 数字确定权限，这里是简单的管理员用户区分
        //富文本对返回值有自己的要求
        if (iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            if (StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);

            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }

    }




}
