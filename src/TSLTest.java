import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.conn.ssl.SSLSocketFactory;

import com.onmobile.ump.thirdparty.EasySSLProtocolSocketFactory;

public class TSLTest {
	private static int window = 10;
	private static int timeout = 10000;

	
	public static void main(String[] args) {
		
		try {

			String responseBody = null;
			int responseCode = -1;
			String url = "https://google.com";


			String sslCertificateVersion = "TLSv1.3";
			InputStream is = new FileInputStream("/workspace/eclipse_workspace/TSL1.3/src/mykeystore.jks");
			// You could get a resource as a stream instead.

			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);
			TrustManagerFactory tmf = TrustManagerFactory
				    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				ks.load(null); // You don't need the KeyStore instance to come from a file.
				ks.setCertificateEntry("caCert", caCert);

				tmf.init(ks);
			
			SSLContext sslcontext = SSLContext.getInstance(sslCertificateVersion);
			sslcontext.init(null, tmf.getTrustManagers(), null);

			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			
		      HttpsURL httpsoSrc = new HttpsURL(url);
				String host = httpsoSrc.getHost();
				int port = httpsoSrc.getPort();
				Protocol myhttps = new Protocol("https",
						new EasySSLProtocolSocketFactory(), port);
				Protocol.registerProtocol("https", myhttps);
			
			HttpMethod method = new GetMethod("https://google.com");
			method.getParams().setCookiePolicy(
					CookiePolicy.IGNORE_COOKIES);
			MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
			connectionManager.getParams().setStaleCheckingEnabled(true);
			connectionManager.getParams().setDefaultMaxConnectionsPerHost(
					window);
			connectionManager.getParams().setMaxTotalConnections(window);
			HttpClient httpClient = new HttpClient(connectionManager);
			httpClient.getHostConfiguration().setHost(host, port,
					myhttps);
			httpClient = new HttpClient(connectionManager);
			DefaultHttpMethodRetryHandler retryhandler = new DefaultHttpMethodRetryHandler(
					0, false);
			httpClient.getParams().setParameter("http.method.retry-handler",
					retryhandler);
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(timeout);
			httpClient.getParams().setSoTimeout(timeout);
			long respTime = System.currentTimeMillis();
			responseCode = httpClient.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
			respTime = System.currentTimeMillis() - respTime;
			System.out.println("Response Code =  " + responseCode
					+ ",Response Code = " + responseBody
					+ ",response time = " + respTime + " ms");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
