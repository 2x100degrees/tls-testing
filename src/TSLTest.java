import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class TSLTest {
	private static int window = 10;
	private static int timeout = 10000;

	
	public static void main(String[] args) {
		
		try {
			TrustManager easyTrustManager = new X509TrustManager() { 
			    public void checkClientTrusted(
			            X509Certificate[] chain,
			            String authType) throws CertificateException {
			        // Oh, I am easy!
			    }
			    public void checkServerTrusted(
			            X509Certificate[] chain,
			            String authType) throws CertificateException {
			        // Oh, I am easy!
			    }
			    public X509Certificate[] getAcceptedIssuers() {
			        return null;
			    }
			};
			
			
			//Creating SSLContextBuilder object
		      SSLContextBuilder SSLBuilder = SSLContexts.custom();
		  
		      //Loading the Keystore file
		      File file = new File("/workspace/eclipse_workspace/TSL1.3/src/mykeystore.jks");
		     //SSLBuilder = SSLBuilder.loadTrustMaterial(file,"changeit".toCharArray());

		      //Building the SSLContext usiong the build() method
		      String sslCertificateVersion = System.getProperty("https.protocols","TLSv1.3" );
		      SSLContext sslcontext = SSLContext.getInstance(sslCertificateVersion);
		      sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);
		 
		      //Creating SSLConnectionSocketFactory object
		      SSLConnectionSocketFactory sslConSocFactory = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.3"}, null,    
		    		   SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		 
		      //Creating HttpClientBuilder
		      HttpClientBuilder clientbuilder = HttpClients.custom();

		      //Setting the SSLConnectionSocketFactory
		      clientbuilder = clientbuilder.setSSLSocketFactory(sslConSocFactory);

		      //Building the CloseableHttpClient
		      CloseableHttpClient httpclient = clientbuilder.build();
		      
		      //Creating the HttpGet request
		      HttpGet httpget = new HttpGet("https://google.com");
		 
		      //Executing the request
		      HttpResponse httpresponse = httpclient.execute(httpget);

		      //printing the status line
		      System.out.println(httpresponse.getStatusLine());

		      //Retrieving the HttpEntity and displaying the no.of bytes read
		      HttpEntity entity = httpresponse.getEntity();
		      if (entity != null) {
		         System.out.println(EntityUtils.toByteArray(entity).length);
		      } 
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
