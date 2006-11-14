Als Datenbank kommt z.Zt. MySQL zum Einsatz. Zu beachten:
> Überprüfen der Mapping-Dateien, ob der Doctype für aktuelle Hibernate-Version 3.0 ist
	<!DOCTYPE hibernate-mapping PUBLIC
	    "-//Hibernate/Hibernate Mapping DTD 2.0//EN"
	    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd" >

> AutoInc durch Anwendung folgender Generator-Klasse:
	<generator class="native" />

>   <!-- uni-directional one-to-many association to Roam -->
    <set
        name="roams"
        lazy="true"
		cascade="none"
		table="roam"
    >
        <key column="impi_id" />
        <many-to-many column="nw_id"
            class="de.fhg.fokus.hss.model.Network"
        />
    </set>
    
      <!-- bi-directional one-to-many association to Impu2svpr -->
    <set
        name="impu2svprs"
        lazy="true"
        inverse="true"
		cascade="none"
		table="impu2svpr"
    >
        <key column="svpr_id" />
        <many-to-many column="impu_id"
            class="de.fhg.fokus.hss.model.Impu"
        />
    </set>
    <!-- bi-directional one-to-many association to Ifc2svpr -->
    <set
        name="ifc2svprs"
        lazy="true"
        inverse="true"
		cascade="none"
		table="ifc2svpr"
    >
        <key column="svpr_id" />
        <many-to-many column="ifc_id"
            class="de.fhg.fokus.hss.model.Ifc2svpr"
        />
    </set>