package inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InventoryWebController {

    /**
     *
     * @return for retrieving testing GUI
     */

    @RequestMapping(value = "/web")
    public String inventory(){
        return "inventory";
    }
}
