package com.study.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.study.tmall.comparator.*;
import com.study.tmall.pojo.*;
import com.study.tmall.service.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ForeController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyValueService propertyValueService;

    @RequestMapping("forehome")
    public String home(Model model){
        List<Category> cs = categoryService.list();
        productService.fillByRow(cs);

        model.addAttribute("cs",cs);

        return "fore/home";
    }

    @RequestMapping("foreregister")
    public String register(User user, Model model){
        String name = user.getName();
        //把账号里的特殊符号进行转义,防止恶意注册名称
        name = HtmlUtils.htmlEscape(name);
        //判断用户名是否存在
        boolean exist = userService.isExist(name);
        if(exist){
            String msg = "用户名已经被使用，请重新输入！";
            model.addAttribute("msg",msg);
            //参数user会自动注入到作用域里。注册失败时，需使导航栏为未登录状态，不显示姓名
            model.addAttribute("user",user);
            return "fore/register";
        }
        userService.add(user);

        return "redirect:registerSuccessPage";
    }

    @ResponseBody
    @RequestMapping("foreisExist")
    public String isExist(User user){
        String name = user.getName();
        //判断用户名是否存在
        String flag = null;
        boolean exist = userService.isExist(name);
        if(exist){
            flag = "exist";
        }
        return flag;
    }

    @RequestMapping("forelogin")
    public String login(User user, Model model, HttpSession session){
        //注册的时候用了转义，故这里也该转义
        String name = HtmlUtils.htmlEscape(user.getName());
        User u = userService.get(name, user.getPassword());
        if(u == null){
            model.addAttribute("msg","账号或密码错误，请重新输入！");
            return "fore/login";
        }
        //将用户存入session域方便页头显示登陆状态信息
        session.setAttribute("user",u);
        String uri = (String) session.getAttribute("uri");
        System.out.println(uri);
        return "redirect:" + uri;
    }

    @RequestMapping("forelogout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product( int pid, Model model) {
        //获取产品，默认注入了第一张预览图和分类属性
        Product p = productService.get(pid);

        //获取产品对应的单个图片和详细图片集合
        List<ProductImage> productSingleImage = productImageService.list(pid,ProductImageService.type_single);
        List<ProductImage> productDetailImage = productImageService.list(pid,ProductImageService.type_detail);

        //为当前产品注入单个图片和详细图片属性
        p.setProductSingleImages(productSingleImage);
        p.setProductDetailImages(productDetailImage);

        //为当前产品注入销量和评价属性
        productService.setSaleAndReviewNumber(p);

        //考虑到查询产品时，不经常会查询注入销量和评价，因此不将它们设为产品类的属性
        List<PropertyValue> pvs = propertyValueService.list(pid);
        List<Review> reviews = reviewService.list(pid);

        model.addAttribute("pvs",pvs);
        model.addAttribute("reviews",reviews);
        model.addAttribute("p",p);

        return "fore/product";
    }

    @ResponseBody
    @RequestMapping("forecheckLogin")
    public String checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");

        return user==null ? "fail":"success";
    }

    @ResponseBody
    @RequestMapping("foreloginAjax")
    public String loginAjax(User user, Model model, HttpSession session){
        //注册的时候用了转义，故这里也该转义
        String name = HtmlUtils.htmlEscape(user.getName());
        User u = userService.get(name, user.getPassword());
        if(u == null){
            return "fail";
        }
        //将用户存入session域方便页头显示登陆状态信息
        session.setAttribute("user",u);
        return "success";
    }

    @RequestMapping("forecategory")
    public String category(int cid, String sort, Model model){
        Category c = categoryService.get(cid);
        //为分类注入产品,且产品已自动注入首图
        productService.fill(c);
        //为产品注入销量和评价量
        productService.setSaleAndReviewNumber(c.getProducts());

        if(null != sort){
            //判断分类规则，使用比较器分类
            switch (sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;
                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
        }
        model.addAttribute("c",c);

        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search(String keyword, Model model){
        //只显示前20条数据
        PageHelper.offsetPage(0,20);
        //获取模糊查询产品结果集
        List<Product> pds = productService.search(keyword);
        //为产品结果集注入销量和评论
        productService.setSaleAndReviewNumber(pds);
        model.addAttribute("pds",pds);

        return "fore/searchResult";
    }

    @RequestMapping("forebuyone")
    public String buyone(int pid, int num, HttpSession session){
        User u = (User) session.getAttribute("user");
        //查询还未生成订单的订单项，已为所有订单项注入产品属性
        List<OrderItem> ois = orderItemService.listByUser(u.getId());
        //查看当前产品是否已存在订单项且还未生成订单
        int oiid = 0;   //当前订单项的id
        boolean found = false;  //产品是否已加入订单项的标志
        for(OrderItem oi : ois){
            if(oi.getProduct().getId().intValue() == pid){
                //有订单项里存在该产品
                //更新订单项里的产品数量
                oi.setNumber(oi.getNumber() + num);
                orderItemService.update(oi);
                //标志产品已存在订单项中
                found = true;
                //获取订单项目id用于给页面传递数据
                oiid = oi.getId();
                break;
            }
        }

        if(!found){
            //订单项中未加入该产品，生成新的的订单项
            OrderItem oi = new OrderItem();
            //设置用户、购买数量、产品
            oi.setUid(u.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            //将订单项数据插入到数据库中
            orderItemService.add(oi);
            //获取订单id用于给页面传递数据
            oiid = oi.getId();
        }
        //跳到结算页面
        return "redirect:forebuy?oiid=" + oiid;
    }

    @RequestMapping("forebuy")
    public String buy(String[] oiid, Model model, HttpSession session){
        //为了兼容从购物车页面跳转过来的需求，用字符串数组获取多个oiid
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;

        if(oiid != null){
            for(String strid : oiid){
                int id = Integer.parseInt(strid);
                //获取订单项，产品属性已注入
                OrderItem oi = orderItemService.get(id);
                total += oi.getProduct().getPromotePrice()*oi.getNumber();
                ois.add(oi);
            }
        }
        //放到session域，生成订单时需取出设置oid
        session.setAttribute("ois",ois);
        model.addAttribute("total",total);

        return "fore/buy";
    }

    @ResponseBody
    @RequestMapping("foreaddCart")
    public String addCart(int pid, int num, HttpSession session){
        User u = (User) session.getAttribute("user");
        //查询还未生成订单的订单项，已为所有订单项注入产品属性
        List<OrderItem> ois = orderItemService.listByUser(u.getId());
        //查看当前产品是否已存在订单项且还未生成订单
        boolean found = false;  //产品是否已加入订单项的标志
        for(OrderItem oi : ois){
            if(oi.getProduct().getId().intValue() == pid){
                //有订单项里存在该产品
                //更新订单项里的产品数量
                oi.setNumber(oi.getNumber() + num);
                orderItemService.update(oi);
                //标志产品已存在订单项中
                found = true;
                break;
            }
        }

        if(!found){
            //订单项中未加入该产品，生成新的的订单项
            OrderItem oi = new OrderItem();
            //设置用户、购买数量、产品
            oi.setUid(u.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            //将订单项数据插入到数据库中
            orderItemService.add(oi);
        }

        return "success";
    }

    @RequestMapping("forecart")
    public String cart( Model model,HttpSession session) {
        User user = (User)  session.getAttribute("user");
        //根据用户获取所有订单项，已注入产品属性
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        model.addAttribute("ois", ois);

        return "fore/cart";
    }

    @ResponseBody
    @RequestMapping("foreUpdateTopCartNum")
    public String UpdateTopCartNum(HttpSession session){
        //获取当前用户所有订单项
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        //获取购物车总数
        int cartTotalItemNumber = 0;
        for (OrderItem oi : ois) {
            cartTotalItemNumber += oi.getNumber();
        }
        //转换为字符串
        return cartTotalItemNumber + "";
    }

    @ResponseBody
    @RequestMapping("forechangeOrderItem")
    public String changeOrderItem(HttpSession session, int pid, int number){
        User user = (User) session.getAttribute("user");

        //遍历出所有未生成订单的订单项
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        //购物车界面能改变数量时，此处获取的ois就不会为空，不会出现空指针异常
        for(OrderItem oi : ois){
            if(oi.getPid() == pid){
                oi.setNumber(number);
                orderItemService.update(oi);
                break;
            }
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping("foredeleteOrderItem")
    public String deleteOrderItem(int oiid){
        //删除对应id的订单项
        orderItemService.delete(oiid);

        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String forecreateOrder(HttpSession session, Order order ,Model model){
        //获取用户对象
        User user = (User) session.getAttribute("user");
        //为订单注入信息
        //根据当前时间加上一个4位随机数生成订单号
        String orderCode = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setUid(user.getId());
        order.setCreateDate(new Date());
        //把订单状态设置为等待支付
        order.setStatus(OrderService.waitPay);
        //去除上一步放入session的订单项
        List<OrderItem> ois = (List<OrderItem>) session.getAttribute("ois");
        //添加订单和关联订单项
        float total = orderService.add(order,ois);

        model.addAttribute("total",total);
        model.addAttribute("order",order);
        return "fore/alipay";
    }

    @RequestMapping("forepayed")
    public String payed(int oid, float total, Model model){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);

        model.addAttribute("o",order);
        model.addAttribute("total",total);
        return "fore/payed";
    }

    @RequestMapping("forebought")
    public String bought(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");

        //查询该用户下所有状态不是"delete"的订单集合
        List<Order> os = orderService.list(user.getId(),OrderService.delete);
        //为订单填充订单项
        orderItemService.fill(os);

        model.addAttribute("os",os);
        return "fore/bought";
    }

    @RequestMapping("foreconfirmPay")
    public String confirmPay(int oid, Model model){
        //获取订单对象，已为订单填充订单项,且加上了事务管理
        Order o = orderService.get(oid);

        model.addAttribute("o",o);
        return "fore/confirmPay";
    }

    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(int oid){
        //获取订单对象，已为订单填充订单项,且加上了事务管理
        Order o = orderService.get(oid);
        //设置订单确认时间，为待评价状态
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);

        return "fore/orderConfirmed";
    }

    @ResponseBody
    @RequestMapping("foredeleteOrder")
    public String deleteOrder(int oid){
        //获取订单对象，已为订单填充订单项,且加上了事务管理
        Order o = orderService.get(oid);
        //修改订单状态为"delete"
        o.setStatus(OrderService.delete);
        orderService.update(o);

        return "success";
    }

    @RequestMapping("forereview")
    public String review(int oid, Model model){
        //获取订单对象，已为订单填充订单项,且加上了事务管理
        Order o = orderService.get(oid);
        //获取产品,已注入首图
        Product p = productService.get(o.getOrderItems().get(0).getPid());
        //为产品设置销量和评价数
        productService.setSaleAndReviewNumber(p);
        //获取该产品评价集合
        List<Review> rs = reviewService.list(p.getId());

        model.addAttribute("o",o);
        model.addAttribute("p",p);
        model.addAttribute("rs",rs);
        return "fore/review";
    }

    @RequestMapping("foredoreview")
    public String doreview(int oid, int pid,String content, HttpSession session){
        //获取订单对象，已为订单填充订单项,且加上了事务管理
        Order o = orderService.get(oid);
        //更新订单状态
        o.setStatus(OrderService.finish);
        orderService.update(o);
        //对评价信息进行转义
        content = HtmlUtils.htmlEscape(content);
        //获取用户对象
        User user = (User) session.getAttribute("user");
        //创建评价对象，更新其评价信息，产品，时间，用户
        Review review = new Review();
        review.setUid(user.getId());
        review.setPid(pid);
        review.setCreateDate(new Date());
        review.setContent(content);
        reviewService.add(review);

        //当参数showonly==true，那么就显示当前产品的所有评价信息
        return "redirect:forereview?oid=" + oid + "&showonly=true";
    }
}
