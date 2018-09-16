package com.snall.controller.portal;

import com.snall.common.Const;
import com.snall.common.ResponseCode;
import com.snall.common.ServerResponse;
import com.snall.pojo.User;
import com.snall.service.ICartService;
import com.snall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

       return iCartService.add(user.getId(),productId,count);
    }
}
