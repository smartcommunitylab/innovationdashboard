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
- edit maven settings at `.m2/settings.xml` and add profile (edit for you needs):

  ```
  [...]
  <profile>
    <id>tsc</id>
    <properties>
      <liferay.version>6.2.3</liferay.version>
      <liferay.auto.deploy.dir>/home/dev/liferay/deploy</liferay.auto.deploy.dir>
      <liferay.app.server.dir>/home/dev/liferay/tomcat</liferay.app.server.dir>
      <liferay.app.server.deploy.dir>/home/dev/liferay/tomcat/webapps</liferay.app.server.deploy.dir>
      <liferay.app.server.lib.global.dir>/home/dev/liferay/tomcat/lib/ext</liferay.app.server.lib.global.dir>
      <liferay.app.server.portal.dir>/home/dev/liferay/tomcat/webapps/ROOT</liferay.app.server.portal.dir>
      <liferay.maven.plugin.version>6.2.2</liferay.maven.plugin.version>
    </properties>
  </profile>
  [...]
  ```
- clone this github repository
- do this commands (or create scripts):

  ```
  cd innovationdashboard
  git pull
  cd innoboard-portlet      # or tsc_theme
  mvn clean
  mvn -P tsc install
  mvn -P tsc liferay:direct-deploy
  ```
- for the utils lib:

```
cd innovationdashboard
git pull
cd tsc_utils
mvn clean
mvn install
cp target/tsc_utils-0.1.jar ~/liferay/tomcat/webapps/tsc/WEB-INF/lib
```
- restart tomcat
- remebmerb to change the column _content_ on table _JournalArticle_: it needs to have collation "utf8mb4 - default collation"

## Create pages
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

## Contents
- create categories under a vocabulary
- create WebContent using _TSC Category Structure_ with right names, images, square-images and colors; then use the right category
- use the import utility to import xml file and create some data

## Design
...
