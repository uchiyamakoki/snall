snall learning
## 每日笔记

2018.8.27：

今天主要是进行用户模块功能的测试.然后遇到了tomcat没啥问题，但是测试接口确实404的bug。

emmm，起初认为是springmvc拦截器出问题了。但是检查了一遍，感觉都没错。然后打断点，竟然没用。因为是idea初学者，debug能力很弱。
最后突发奇想，会不会是idea的锅。发现。。。原来是打的包不对，即tomcat的deployment配置的包要选择：war。还是idea用的太少了。

然后稍微复习一下。

2018.8.29：

今天主要是添加分类和更新分类名字。

因为是新的功能。所以首先还是先新建一个CategoryManageController。然后在Controller里写相应的接口.

第一个是增加分类接口.
``` HTML
  @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //检验一下是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
```
传入参数为session，新增分类名称，以及分类的父节点，并通过@RequestParam(value = "parentId",defaultValue = "0")
对父节点进行限制（0为根节点）

然后常规操作，先通过session获取当前用户,然后顺便判断下用户是否为空，之后再判断是否有权限，即是不是管理员。


接下来就是在UserService里新增功能。

第一个就是校验用户是否是管理员。
``` HTML
  //校验是否是管理员
      public ServerResponse checkAdminRole(User user) {
          if(user!=null&&user.getRole().intValue()==Const.Role.ROLE_ADMIN){
              return ServerResponse.createBySuccess();
          }
          return ServerResponse.createByError();
      }
```
基本也就是判断用户存在和登录时权限是否为管理员

接下来就是在新建一个ICategoryService，然后新建一个借口实现类CategoryServiceImpl，并在实现类中实现相应的功能.
同时注意@Service（"""）注解

第一个就是新增分类
``` HTML
  public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
         Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
 
        int rowCount=categoryMapper.insert(category);
        if (rowCount>0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
         return ServerResponse.createByErrorMessage("添加品类失败");
     }
```
首先是判断父节点为null，至少也是0或者分类名为空

之后创建对象，将传入的categoryName，parentIdset进category里，setStatus则是说明分类可用
之后用@Autowired注入CategoryMapper,这样我们就能调用CategoryMapper的insert方法插入新的category
而这里的逆向工程生成的insert返回一个int值来进行返回判断。>0表示插入成功。

最后在Controller里检验管理员成功后调用Service的add分类方法即可。再加上映射和json注解。

接下来是更新分类接口.
``` HTML
 @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
```
同样是判断用户登录，然后判断是否是管理员。

再在Service实现类中新增更新功能
``` HTML
public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category=new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount>0){
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }
```
同样先是判断XX为空，分类id和名称，然后将id和name set一下
同样调用categorymapper的更新方法，一样会返回int，判断一下即可分辨成功失败。
最后在Controller中检验管理员成功后，调用Service的更新功能，之后加上mapping和body

2018.9.1：

2018.9.17

这次主要是更新和删除购物车相关

贴一下Service的

首先都是产品id和数量不能null
查询购物车所以需要用户id和产品id 还想了一会儿。。
更新购物车中产品的数量 就是你要买多少，豁然开朗。。
再更新购物车
最后返回反正都是返回CartVo
``` HTML
if (productId==null ||  count==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart=cartMapper.selectCartByUserIdProductId(userId,productId);
        if (cart!=null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        CartVo cartVo=this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
```

再理一理删除时候的逻辑,删除可能多个（这里是靠字符串逗号分割）
``` HTML
 List<String> productList= Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productList);
        CartVo cartVo=this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
```
List<String> productList= Splitter.on(",").splitToList(productIds);
emmm切割的还行。

然后练习一下delete语句嘿嘿
``` HTML
<delete id="deleteByUserIdProductIds">
    DELETE FROM mmall_cart
    WHERE user_id=#{userId}
    <if test="productIdList != null">
      and product_id in
      <foreach collection="productIdList" item="item" index="index" open="{" separator="," close="}">
        #{item}
      </foreach>
    </if>
  </delete>
```
今天先写到这了。








