package contactpa

import sohonotes.pojo.UserInfo
import com.google.step2.AuthRequestHelper
import com.google.step2.ConsumerHelper
import javax.servlet.http.HttpServletRequest
import org.openid4java.OpenIDException
import org.openid4java.message.AuthRequest
import com.google.step2.discovery.IdpIdentifier;
import javax.servlet.http.HttpSession;
import com.google.step2.openid.ui.UiMessageRequest;
import contactpa.pojo.UserInfo
import org.openid4java.message.ParameterList;
import org.openid4java.discovery.DiscoveryInformation;
import com.google.step2.AuthResponseHelper;
import com.google.step2.Step2;

class GoogleOpenIdAuthService {

	static transactional = true
	
	def grailsApplication

	boolean gmailGadgetAuthentication = false
	String token = ""
	
	def consumerHelper
	
	public String getRealm(){
		grailsApplication.config.openid.realm
	}
	public String getReturnToPath(){
		grailsApplication.config.openid.returnToPath
	}
	public String getHomePath(){
		grailsApplication.config.openid.returnToPath
	}
	
	
	def startAuthentication(String domain, HttpServletRequest request) {
		AuthRequest ar = getAuthRequest(domain, request)
		String url = ar.getDestinationUrl(true)
		log.info("method: startAuthentication(domain:${domain})")
		log.info("getDestinationUrl: ${url}")
		return url
	}

	private AuthRequest getAuthRequest(String op, HttpServletRequest request){
		IdpIdentifier openId = new IdpIdentifier(op);

		String realm = this.getRealm();
		String returnToUrl = this.getReturnToUrl(request);
		
		log.info("method: getAuthRequest(op:${op})")
		log.info("realm: ${realm}")
		log.info("returnToUrl: ${returnToUrl}")
		
		AuthRequestHelper helper = consumerHelper.getAuthRequestHelper(openId,
				returnToUrl);
		addAttributes(helper);

		HttpSession session = request.getSession();
		AuthRequest authReq = helper.generateRequest();
		authReq.setRealm(realm);

		UiMessageRequest uiExtension = new UiMessageRequest();
		uiExtension.setIconRequest(true);
		authReq.addExtension(uiExtension);

		session.setAttribute("discovered", helper.getDiscoveryInformation());
		session.setAttribute("gmail", gmailGadgetAuthentication);
		session.setAttribute("token", token);
		return authReq;
	}

	public UserInfo completeAuthentication(HttpServletRequest request)
	throws OpenIDException {
		HttpSession session = request.getSession();
		ParameterList openidResp = Step2.getParameterList(request);
		String receivingUrl = this.getReceivingUrl(request);
		DiscoveryInformation discovered =
				(DiscoveryInformation) session.getAttribute("discovered");
		
		
		log.info("method: completeAuthentication()")
		log.info("openidResp: ${openidResp}")
		log.info("discovered: ${discovered}")
		log.info("receivingUrl: ${receivingUrl}")
		
		AuthResponseHelper authResponse = consumerHelper.verify(receivingUrl, openidResp, discovered);
		if (authResponse.getAuthResultType() == AuthResponseHelper.ResultType.AUTH_SUCCESS) {
			return onSuccess(authResponse, request);
		}
		return onFail(authResponse, request);
	}

	/**
	 * Map the OpenID response into a UserAccount for our app.
	 *
	 * @param helper
	 *            Auth response
	 * @param request
	 *            Current servlet request
	 * @return UserAccount representation
	 */
	UserInfo onSuccess(AuthResponseHelper helper, HttpServletRequest request) {
		return new UserInfo(claimedId:helper.getClaimedId().toString(),
		email:helper.getAxFetchAttributeValue(Step2.AxSchema.EMAIL),
		firstName:helper.getAxFetchAttributeValue(Step2.AxSchema.FIRST_NAME),
		lastName:helper.getAxFetchAttributeValue(Step2.AxSchema.LAST_NAME),
		language:helper.getAxFetchAttributeValue(Step2.AxSchema.LANGUAGE),
		country:helper.getAxFetchAttributeValue(Step2.AxSchema.COUNTRY));
	}

	/**
	 * Handles the case where authentication failed or was canceled. Just a
	 * no-op here.
	 *
	 * @param helper
	 *            Auth response
	 * @param request
	 *            Current servlet request
	 * @return UserAccount representation
	 */
	UserInfo onFail(AuthResponseHelper helper, HttpServletRequest request) {
		return null;
	}
	

	
	private String getBaseUrl(HttpServletRequest request) {
		String strScheme = request.getScheme()
		String strServerName = request.getServerName()
		String strServerPort = request.getServerPort()
		
		StringBuffer url = new StringBuffer(strScheme).append("://")
				.append(strServerName);
		
				/**
		if ((strScheme.equalsIgnoreCase("http") && strServerPort != 80)
				|| (strScheme.equalsIgnoreCase("https") && strServerPort != 443)) {
			url.append(":").append(strServerPort);
		}
		**/
				
		return url.toString();
	}
	
	void addAttributes(AuthRequestHelper helper) {
		helper.requestAxAttribute(Step2.AxSchema.EMAIL, true)
				.requestAxAttribute(Step2.AxSchema.FIRST_NAME, true)
				.requestAxAttribute(Step2.AxSchema.LAST_NAME, true)
				.requestAxAttribute(Step2.AxSchema.LANGUAGE, true)
				.requestAxAttribute(Step2.AxSchema.COUNTRY, true);

	}
	
	String getReceivingUrl(HttpServletRequest request) {
		//grails maps the reciving URL from a dispatch to
		// http://contactpa.cloudfoundry.com/grails/GAMOpenId/openid.dispatch
		//it should be
		//http://contactpa.cloudfoundry.com/openid
		String currUrl = getCurrentUrl(request)
		return currUrl.replace("grails/GAMOpenId/openid.dispatch", "openid")
	}
	
	String getCurrentUrl(HttpServletRequest request){
		return Step2.getUrlWithQueryString(request);
	}
	
	String getReturnToUrl(HttpServletRequest request) {
		String strBaseUrl = this.getBaseUrl(request)
		String strContextPath = request.getContextPath()
		String strReturnPath = this.getReturnToPath()
		
		return new StringBuffer(strBaseUrl)
				.append(strContextPath).append(strReturnPath)
				.toString();
	}
	
}
