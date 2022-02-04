package org.learn.i18n;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;


@Vetoed
public class LearnResourceBundle extends ResourceBundle {

	private final ResourceBundle customBundle;
	private final ResourceBundle learnBundle;

	/**
	 * @deprecated CDI eyes only
-	 */
	protected LearnResourceBundle() {
		this(null, null);
	}
	
	@Inject
	public LearnResourceBundle(ResourceBundle customBundle, ResourceBundle learnBundle) {
		this.customBundle = customBundle;
		this.learnBundle = learnBundle;
	}

	@Override
	protected Object handleGetObject(String key) {
		if(customBundle.containsKey(key)) return customBundle.getObject(key);
		if(learnBundle.containsKey(key)) return learnBundle.getObject(key);
		return "???" + key + "???";
	}

	@Override
	public Enumeration<String> getKeys() {
		Enumeration<String> defaultKeys = customBundle.getKeys();
		Enumeration<String> learnKeys = learnBundle.getKeys();
		Vector<String> allKeys = new Vector<>();

		while (defaultKeys.hasMoreElements()) {
			allKeys.add(defaultKeys.nextElement());
		}
		
		while (learnKeys.hasMoreElements()) {
			allKeys.add(learnKeys.nextElement());
		}
		
		return allKeys.elements();
	}

}
