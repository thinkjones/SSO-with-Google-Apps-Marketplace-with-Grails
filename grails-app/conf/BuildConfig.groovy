grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
		
		runtime 'mysql:mysql-connector-java:5.1.13'
		compile group:'org.openid4java', name:'openid4java-consumer', version:'0.9.6'
		compile group:'com.google', name:'step2-common', version:'1-SNAPSHOT'
		compile group:'com.google', name:'step2-consumer', version:'1-SNAPSHOT'
		compile group:'com.google.inject', name:'guice', version:'3.0'
		compile group:'javax.servlet', name:'jstl', version:'1.2'
		compile group:'org.apache.httpcomponents', name:'httpclient', version:'4.1'
		compile (group:'org.openxri', name:'openxri-client', version:'1.2.1') { excludes([ group: 'org.slf4j', name: 'slf4j-jcl']) }
		compile group:'org.jdom', name:'jdom', version:'1.1'
		compile group:'com.google.collections', name:'google-collections', version:'1.0'
    }
}
