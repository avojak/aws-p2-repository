package com.avojak.webapp.aws.p2.repository.service.cache;

import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectCache {

	private final LoadingCache<String, Project> cache;

	@Autowired
	public ProjectCache(final ProjectCacheLoader cacheLoader) {
		cache = CacheBuilder.newBuilder().build(cacheLoader);
	}

}
