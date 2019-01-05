package com.avojak.webapp.aws.p2.repository.webapp.controller;

import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.service.DataService;
import com.avojak.webapp.aws.p2.repository.webapp.configuration.WebappProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides methods to retrieve view template data.
 */
@Controller
@RequestMapping("browse")
public class ViewController {

	private final DataService service;
	private final WebappProperties properties;

	/**
	 * Constructor.
	 *
	 * @param service
	 * 		The {@link DataService}.
	 */
	@Autowired
	public ViewController(final DataService service, final WebappProperties properties) {
		this.service = checkNotNull(service, "service cannot be null");
		this.properties = checkNotNull(properties, "properties cannot be null");
	}

	/**
	 * Provides the endpoint for retrieving the base view for browsing the repositories.
	 *
	 * @param model
	 * 		The {@link Model}.
	 *
	 * @return The view template name.
	 */
	@GetMapping("")
	public String index(final Model model) {
		model.addAttribute("title", properties.getBrandName());
		model.addAttribute("brandIcon", properties.getBrandIcon());
		model.addAttribute("brandFavicon", properties.getBrandFavicon());
		model.addAttribute("welcomeMessage", properties.getWelcomeMessage());
		model.addAttribute("projectCount", service.getProjects().size());
		return "browse";
	}

	/**
	 * Provides the endpoint for retrieving the project view template.
	 *
	 * @param name
	 * 		The project name to view.
	 * @param model
	 * 		The {@link Model}.
	 *
	 * @return The view template name.
	 */
	@GetMapping("/project/{name}")
	public String showProject(@PathVariable("name") final String name, final Model model) {
		final Optional<Project> project = service.getProject(name);
		if (!project.isPresent()) {
			throw new IllegalArgumentException("Project does not exist: " + name);
		}
		model.addAttribute("project", project.get());
		model.addAttribute("domain", properties.getCustomDomain());
		// TODO: Pass project description
		return "project";
	}

	/**
	 * Provides the endpoint for retrieving the dashboard fragment view template.
	 *
	 * @param model
	 * 		The {@link Model}.
	 *
	 * @return The view template fragment name.
	 */
	@GetMapping("/dashboard")
	public String showDashboard(final Model model) {
		model.addAttribute("projects", service.getProjects());
		model.addAttribute("domain", properties.getCustomDomain());
		model.addAttribute("uptime", service.getUptime());
		model.addAttribute("bucket", properties.getBucketName());
		return "fragments/dashboard :: dashboard";
	}

	/**
	 * Provides the endpoint for retrieving the project list fragment view template.
	 *
	 * @param model
	 * 		The {@link Model}.
	 *
	 * @return The view template fragment name.
	 */
	@GetMapping("/projects/all")
	public String showRecentUpdates(final Model model) {
		model.addAttribute("projects", service.getProjects());
		return "fragments/projects :: projectList";
	}

}
