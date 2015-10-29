# innovationdashboard

## Set up MySQL
- create db, e.g. _lportal_
- create user, e.g. _liferay_
- grant to _liferay_ all permissions on _lportal_

## Liferay installation
- download Liferay bundled with Tomcat (last known: `liferay-portal-tomcat-6.2-ce-ga4-20150416163831865.zip`)
- unzip it where you need it
- (optional) change Tomcat ports in file `liferay/tomcat/conf/server.xml`
- create or edit file `liferay/tomcat/webapps/ROOT/WEB-INF/classes/portal-ext.properties`
  - add this content:

    ```
    jdbc.default.driverClassName=com.mysql.jdbc.Driver
    jdbc.default.url=jdbc:mysql://localhost/<db>?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false
    jdbc.default.username=<user>
    jdbc.default.password=<password>
    velocity.engine.restricted.classes=
    velocity.engine.restricted.variables=
    freemarker.engine.restricted.classes=
    freemarker.engine.restricted.variables=
    ```
- (assuming we're using Apache2) create entry with **ProxyPass** and **ProxyPassReverse** and **AJP** to access Liferay instance
- run it! `liferay/tomcat/bin/startup.sh`
- download `marketplace-portlet-6.2.0.3.war` and copy it on `liferay/deploy` folder, it will be deployed

## Structures and templates
- create structures and templates copying from the files you can find under theme's `resources-importer`
  - TSC Category template
  - TSC Category structure
  - TSC Project template
  - TSC Project structure

## tsc-theme and innoboard-portlet installation
...
- column "content" on table "JournalArticle" needs to have collation "utf8mb4 - default collation"
...

## Creazione pagine
- create page "category", hide from menu
- create page "project", hide from menu
- configure page "category":
  - add Asset Publisher:
    - custom title empty
    - link to Current page
    - show borders no
    - filter by Web Content Article - TSC Category structure
    - Full Content
    - Set as the Default Asset Publisher for This Page
  - add Asset Publisher:
    - custom title empty
    - link to page "project"
    - show borders no
    - filter by Web Content Article - TSC Project structure
    - TSC Project preview
- configure page "project":
  - edit page, "Copy applications" from page "category"
  - remove the second Asset Publisher
