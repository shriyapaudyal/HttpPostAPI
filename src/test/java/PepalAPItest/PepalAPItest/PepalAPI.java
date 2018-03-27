package PepalAPItest.PepalAPItest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class PepalAPI {

	private HttpClient client = HttpClientBuilder.create().build();
	// private final String USER_AGENT = "Mozilla/5.0";
	public String tok;
	private void sendPost(String url, List<NameValuePair> postParams) throws Exception {
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setEntity(new UrlEncodedFormEntity(postParams));
		HttpResponse response = client.execute(post);
		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + postParams);
		System.out.println("Response Code : " + responseCode);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println("result" + result.toString());
		this.tok = result.toString();
	}

	public List<NameValuePair> getFormParams() throws UnsupportedEncodingException {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		String key = "login";
		String value = "{\"UserName\": \"admin@gobingoo.com\",\"Password\": \"gobingoo\",\"DeviceId\": \"012345678889\",\"PushToken\": \"push\"}";
		paramList.add(new BasicNameValuePair(key, value));
		return paramList;

	}

	public void extracttoken() {
		Pattern pattern = Pattern.compile("((\"T)\\w+(.*?),)");
		Matcher matcher = pattern.matcher(tok);
		String here;
		if (matcher.find()) {
			here = (matcher.group(1));
			String splittertok = here.split("\"")[3];
			System.out.println("Token is " + splittertok);
		}
	}

	public static void main(String[] args) throws Exception {
		PepalAPI s = new PepalAPI();
		String url = "http://172.18.11.116:8086//Modules/HRISPepalApi/Service/PepalApiService.asmx/login";
		List<NameValuePair> postParams = s.getFormParams();
		s.sendPost(url, postParams);
		s.extracttoken();
	}

}
