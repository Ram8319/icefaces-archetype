
Why you should use this project as startup:

- set of key technologies integrated and adjusted to play nice with each other
- ICEFaces as the best JSF implementation that works just out of the box
- Spring beans seamlessly integrated with JSF managed beans
- Spring Security integrated with log on/off and redirects for protected resources
- MyFaces instead of default Mojarra - for better error reporting (also custom LoggingExceptionHandler)
- Jetty to start up as development
- Selenium for black-box functional testing
- JRebel for zero-turnaround redeployment (this one comes with commercial license but believe me it worth its price!)


To build and launch:

$ mvn jetty:run-exploded

Open http://localhost:8080/ in the browser.

***

To run Selenium integration test (runs its own Jetty, watch out for port conflicts):

$ mvn integration-test

Selenium tests require Firefox to be locally installed.
On headless CI Linux servers, install xvfb to enable testing.

***

For production build (replaces a placeholder in web.xml):

$ mvn -Pproduction package

