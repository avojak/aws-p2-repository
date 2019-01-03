package com.avojak.webapp.aws.p2.repository.webapp.configuration;

/**
 * The web application properties.
 */
public class WebappProperties {

	private final String brandName;
	private final String brandIcon;
	private final String brandFavicon;
	private final String customDomain;
	private final String welcomeMessage;
	private final String bucketName;

	/**
	 * Constructor.
	 *
	 * @param brandName
	 * 		The brand name.
	 * @param brandIcon
	 * 		The brand icon URL.
	 * @param brandFavicon
	 * 		The branch favicon URL.
	 * @param customDomain
	 * 		The custom domain name.
	 * @param welcomeMessage
	 * 		The welcome message.
	 * @param bucketName
	 * 		The S3 bucket name.
	 */
	WebappProperties(final String brandName, final String brandIcon, final String brandFavicon,
					 final String customDomain, final String welcomeMessage, final String bucketName) {
		this.brandName = brandName;
		this.brandIcon = brandIcon;
		this.brandFavicon = brandFavicon;
		this.customDomain = customDomain;
		this.welcomeMessage = welcomeMessage;
		this.bucketName = bucketName;
	}

	/**
	 * @return The brand name.
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * @return The brand icon URL.
	 */
	public String getBrandIcon() {
		return brandIcon;
	}

	/**
	 * @return The brand favicon URL.
	 */
	public String getBrandFavicon() {
		return brandFavicon;
	}

	/**
	 * @return The custom domain name.
	 */
	public String getCustomDomain() {
		return customDomain;
	}

	/**
	 * @return The welcome message.
	 */
	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	/**
	 * @return The S3 bucket name.
	 */
	public String getBucketName() {
		return bucketName;
	}

}
