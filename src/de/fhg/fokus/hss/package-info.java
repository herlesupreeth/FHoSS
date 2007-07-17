/**
 * \package de.fhg.fokus.hss
 * Implementation of the server.
 * 
 * <H1>The implementation of the HSS server</H1>
 * The server implementation is based on open different open source
 * projects
 * <ul>
 * <li><a href="">Apache Struts</a> for the web based administration gui.</li>
 * <li><a href="">Hibernate</a> for the persistence layer</li>
 * <li><a href="">Apache Tomcat</a> as container for the administration site.</li> 
 * </ul>
 * 
 * <H2>Package and Layer overview</H2>
 * <p>
 * The implementation of the HSS is a multilayered implementation. This has been done 
 * to seperate the gui from the logic and to keep parts exchangable.
 * </p>
 * <h3>GUI Layer</h3>
 * <p>
 * The GUI Layer is realized using the Struts Web Framework. The classes for the GUI 
 * Layer are located in the <a href="web/package-summary.html">web</a> Package.
 * </p>
 * <p>
 * The form package contains the Struts form classes, which are holding the data page
 * data during the session.
 * The action package contains all the Struts actions which handle the Http requests.
 * </p>
 * 
 * <h3>Business Logic Layer</h3>
 * <p>
 * The classes from the <a href="db/op/package-summary.html">op</a> package
 * implement the business logic for the <a href="db/model/package-summary.html">model</a> classes. These are used by the Server and the GUI layer
 * to present data and to handle the user input. 
 * </p>
 * <p>
 * 
 * <h3>Persistence Layer</h3>
 * <p>
 * The persistence layer was implemented using the Hibernate persistence framework.
 * There are POJO's in the <a href="db/model/package-summary.html">model</a> package which
 * were mapped to database tables. The use of Hibernate enables you to change 
 * the database for the HSS.
 * </p>
 * <p>
 * The data objects are referenced by the GUI Layer to present the data, and by the 
 * Business Logic Layer to perform the business logic.
 * </p>
 * 
 * <h3>HSS Layer</h3>
 * <p>
 * This layer provides the implementation of the HSS interfaces. It contains
 * the implementation of the Cx, Sh and Zh interfaces.
 * This implementation is directly referenced by the business logic to
 * dispatch the user interactions to the diameter peer.
 * </p>
 */
package de.fhg.fokus.hss;