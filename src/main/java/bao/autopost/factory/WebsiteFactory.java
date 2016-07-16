package bao.autopost.factory;

import bao.autopost.model.ClassicWebsite;
import bao.autopost.model.ModernWebsite;
import bao.autopost.model.Website;

public class WebsiteFactory {

	public static Website createWebsite(String type) throws Exception {
		switch (type) {
		case "Classic":
			return new ClassicWebsite();
		case "Modern":
			return new ModernWebsite();
		default:
			throw new Exception("Type not supported: " + type);
		}
	}
}
