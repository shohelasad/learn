package org.learn.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.learn.model.SanitizedText;
import org.learn.sanitizer.HtmlSanitizer;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.Converter;

@Convert(SanitizedText.class)
@ApplicationScoped
public class SanitizedTextConverter implements Converter<SanitizedText> {

	private final HtmlSanitizer sanitizer;

	/**
	 * @deprecated CDI eyes only
	 */
	protected SanitizedTextConverter() {
		this(null);
	}
	
	@Inject
	public SanitizedTextConverter(HtmlSanitizer sanitizer) {
		this.sanitizer = sanitizer;
	}
	
	@Override
	public SanitizedText convert(String value, Class<? extends SanitizedText> type) {
		return sanitizer.sanitize(value);
	}

}
