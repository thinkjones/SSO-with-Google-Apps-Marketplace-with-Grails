class BootStrap {

    def init = { servletContext ->
		
		System.setProperty("file.encoding","UTF-8")
		
    }
    def destroy = {
    }
}
