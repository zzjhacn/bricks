package com.bricks.facade;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author bricks <long1795@gmail.com>
 */
@Controller
public class DefaultController {

	@RequestMapping({ "/", "/index" })
	public String showSeedstarters() {
		return "tables";
	}

}
