package contactpa

import contactpa.GoogleOpenIdAuthService
import contactpa.pojo.UserInfo

class GAMOpenIdController {
	
	def googleOpenIdAuthService
	
	def index = {
		render "Hello".toString()
	}
	
	def openid = {
		if(request.get == true)
			openid_get()
		if(request.post == true)
			openid_post()
	}
	
	def openid_get = {
		String domain = params['hd']
		
		if (domain == null) {
			openid_post()
			return
		}
		
		//TODO Detect Gmail Gadget
		String token = params['token'];  //TODO store in session
		String url = googleOpenIdAuthService.startAuthentication(domain, request)
		response.sendRedirect(url)	
	}
	
	def openid_post = {
		UserInfo userInfo = googleOpenIdAuthService.completeAuthentication(request)
		session["user"] = userInfo.getClaimedId()
		render "User Authenticaed: " + session["user"] + "<br/> Receiving Url:" + userInfo.getEmail() + "<br/> openidResp Url:" + userInfo.getFirstName() +  "<br/> discovered Url:" + userInfo.getLastName() + "<br />"
	}
	
	def status = {
		if(session["user"]==null)
		 	render "No Session"
		else
			render session["user"]
	}
	
	def startSession = {
		session["user"] = "sessionStarted"
		render session["user"]
	}
	
	def info = {
		render googleOpenIdAuthService.getReturnToUrl(request)
	}
	
}
