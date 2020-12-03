Base Play Framework Application
=====================================

This is a basic skeleton for a Java-based Play Framework (version 2.8.0) application with some extra goodies:

* Bootstrap (SASS CSS) and some extra utils (git submodule)
* Font Awesome
* jQuery
* Java Utils
* MySQL connectivity and example DB
* Localization (pt, en)
* Users and Roles Management
* Background Jobs Example (to schedule background tasks)
* Mailer Plugin (to send mails)
* iTextPDF Library (to generate PDF documents)
* Apache POI (to generate Word and Excel documents)
* Location based on client IP (GeoLite2 city database)
* humans.txt and robots.txt

Requirements
============
You need to have the following installed:
* Java 8
* SBT 2.13.1
* SCSS (SASS CSS)

What To Do After Cloning the Repo
=================================

These are the steps you must perform before running the application.

Set up the example DB
---------------------

Run the following as `root` in MySQL:

    CREATE DATABASE base_play_app_db;
    CREATE USER 'base_play_app'@'localhost' IDENTIFIED BY 'example_password';
    GRANT ALL PRIVILEGES ON example_db.* TO 'base_play_app'@'localhost' WITH GRANT OPTION;

Set up configuration files
--------------------------

* Create the `conf/secure.conf` file
* You must add a parameter `play.http.secret.key="..."` in this file. In order to generate a secret to be added to this parameter, you can run the command `sbt playGenerateSecret`. Place the result of that command into `...`
* Also add a parameter `db.default.password="..."` in this file, where `...` should have the password you have defined in the previous step (see Set up the example DB)
* The DB details on `conf/application.conf` file are set for the example DB, but you can set them to whatever you want.
* Review other parameters (e.g. mailer configuration) at the `conf/application.conf` file.
* Set the name and version of your application in the `build.sbt` file.

Install `scss` (SASS CSS)
------------------------------

You need to install `scss` in order to compile the `public/css/main.scss` file into `public/css/main.css`

Build the Application
---------------------

Run this command in the terminal in the base directory of the application:

    sbt clean update compile

Using Eclipse IDE
-----------------
If you are planning to use Eclipse as your development IDE, simply run the following command to generate the necessary project files:

    sbt eclipse

Then you can import the project in Eclipse and you will be ready to go.

Run the Application
-------------------

Run this command in the terminal in the base directory of the application:

    sbt run

The app is now accessible at [http://localhost:9292](http://localhost:9292/ "Base Application URL"). Note: this example app is set to run automatically at port 9292. You can change this in the `build.sbt` file, in the `play.server.http.port` parameter.

First Time Running
------------------

The first time the app runs, it will say that the DB is in an inconsistent state (because it is empty at this point). You just click the option to run the evolution script and it will create the necessary tables on the MySQL example DB you created earlier.

You can then use the `utils/example_db.sql` file to generate some dummy data in your example DB.

Build Distribution Package
--------------------------

Run this command in the terminal in the base directory of the application:

    sbt dist

This will build a self-contained package (inside the `target/universal` directory) that you can run in any JVM-based server. No need to have Play installed in the server.

To deploy a production server, run this set of commands:

    cd target/universal/
    unzip base-play-app-2.7.3.zip
    cd base-play-app-2.7.3
    ./bin/base-play-app -Dhttp.port=8888 -Dconfig.resource=production.conf -Dfile.encoding=UTF8

The application is now running in your server on port 8888 (which you can set to whatever you like).

**Note**: for a production server you should use a different configuration file (in this case `production.conf` which should also be on the `conf` directory) with the specific configuration of your production database and environment. In this example app, the `production.conf` file just imports the `application.conf` but this is not advised for a real production environment. You should also create a separate `secure-production.conf` file that holds the necessary passwords the same way that `secure.conf` does for `application.conf`.
