package taeheoki.jpabook.jpashop.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import taeheoki.jpabook.jpashop.domain.Member;
import taeheoki.jpabook.jpashop.domain.Order;
import taeheoki.jpabook.jpashop.domain.item.Item;
import taeheoki.jpabook.jpashop.repository.OrderSearch;
import taeheoki.jpabook.jpashop.service.ItemService;
import taeheoki.jpabook.jpashop.service.MemberService;
import taeheoki.jpabook.jpashop.service.OrderService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String create(
            @RequestParam Long memberId,
            @RequestParam Long itemId,
            @RequestParam Integer count
    ) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        for (Order item : orders) {
            System.out.println("test: " + item.getOrderItems().get(0).getItem().getName());
            System.out.println("test: " + item.getOrderItems().get(0).getOrderPrice());
            System.out.println("test: " + item.getOrderItems().get(0).getCount());
        }

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
