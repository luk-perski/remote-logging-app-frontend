Remote Logging Application
=====================================

This is an application used for logging works during remote work.

Requirements
============
You need to have the following installed:
* node 14.15.0
* npm 6.14.8

What To Do After Cloning the Repo
=================================

These are the steps you must perform before running the application.

Install npm packages
--------------------
Run this command in the terminal to install npm dependencies

    npm install

Run the Application
-------------------


Run this command in the terminal in the base directory of application to run frontend:

    npm rum start:dev

First Time Running
------------------

The first time the app runs, it will say that the DB is in an inconsistent state (because it is empty at this point). You just click the option to run the evolution script and it will create the necessary tables on the MySQL example DB you created earlier.

Frontend
-------------
Available scripts:
* start
* build
* test
* eject

E.g. to run:

    npm rum start

The application is now running in your server on port 3000 (which you can set to whatever you like).

