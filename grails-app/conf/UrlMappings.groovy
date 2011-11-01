class UrlMappings {

	static mappings = {


		"/$controller/$action?/$id?"{
			constraints {
			}
		}
		
		"/openid"(controller:"GAMOpenId", action:"openid")
		
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
