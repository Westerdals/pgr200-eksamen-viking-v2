package no.kristiania.pgr200.database;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpQuery {
	
	private Map<String, String> parameters = new LinkedHashMap<>();
	
	HttpQuery(String query) {
		if (!query.isEmpty()) {
			for (String parameter : query.split("&")) {
				int equalsPos = parameter.indexOf('=');
				String key = urlDecode(parameter.substring(0, equalsPos));
				String value = urlDecode(parameter.substring(equalsPos + 1));
				parameters.put(key, value);
			}
		}
	}

	private String urlEncode(String substring) {
		try {
			return URLEncoder.encode(substring, StandardCharsets.ISO_8859_1.name());
		}catch (UnsupportedEncodingException e) {
			throw new RuntimeException("This should never happen", e);
		}
	}

	private String urlDecode(String substring) {
		try {
			return URLDecoder.decode(substring, StandardCharsets.ISO_8859_1.name());
		}catch (UnsupportedEncodingException e) {
			throw new RuntimeException("This should never happen", e);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder query = new StringBuilder();
		for (String paramName : parameters.keySet()) {
			if (query.length() > 0) {
				query.append("&");
			}
			query.append(urlEncode(paramName))
				.append("=")
				.append(urlEncode(parameters.get(paramName)));
		}
		return query.toString();
	}

	String getParameter(String key) {
		return parameters.get(key);
	}

}
