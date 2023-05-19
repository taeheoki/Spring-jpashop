package taeheoki.jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!!");
        return "hello";
    }

    @GetMapping("/hello1")
    public String helloRedirect(Model model, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("data", "hello!!!");
        return "redirect:/hello";
    }
}
