package com.avojak.webapp.aws.p2.repository.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Setup redirects from one endpoint to another.
 */
@Controller
@RequestMapping("/")
public class RedirectController {

	/**
	 * Redirects requests from the root ("/") to the "/browse" endpoint.
	 *
	 * @return The redirect location.
	 */
	@GetMapping("")
	public String redirectToBrowse() {
		return "redirect:/browse";
	}

}
