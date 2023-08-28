package com.fabrix.hotels.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Properties {

	private String msg;
	private String buildVersion;
	private Map<String, String> mailDetails;
	private List<String> activeLocations;

	public Properties(String msg, String buildVersion, Map<String, String> mailDetails, List<String> activeLocations) {
		this.msg = msg;
		this.buildVersion = buildVersion;
		this.mailDetails = mailDetails;
		this.activeLocations = activeLocations;
	}

}
