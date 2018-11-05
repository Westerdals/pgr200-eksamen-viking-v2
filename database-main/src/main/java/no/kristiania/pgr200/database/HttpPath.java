package no.kristiania.pgr200.database;


public class HttpPath {
	
	private String requestUri;
	
	public HttpPath(String requestUri) {
		this.requestUri = requestUri;
	}
	
	public String getPath() {
		return getQuestionPos() != -1 ? requestUri.substring(0, getQuestionPos()) : requestUri;
	}
	
	private int getQuestionPos() {
		return requestUri.indexOf('?');
	}
	
	public HttpQuery getQuery() {
		return getQuestionPos() != -1 ? new HttpQuery(requestUri.substring(getQuestionPos()+1)) : null;
	}
	
	@Override
	public String toString() {
		HttpQuery query = getQuery();
		return getPath() + (query != null ? "?" + query : "");
	}

}
