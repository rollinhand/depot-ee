# depotee
simple depot analysis application based on JSF

## Background information
This is a small JSF 2 application based on [Apache Myfaces](https://myfaces.apache.org) and [BootsFaces](http://bootsfaces.org). The dashboard is implemented with [C3Faces](http://c3faces.kivio.org) and for security concerns like a login page, [Apache Shiro](http://shiro.apache.org) is used.

The application can import depot valuations from ING DiBa which uses a special CSV file format. Extracted information about instruments, market value are stored to an H2 file based storage. For a single user application there is no need for a server based database like MySQL or PostgreSQL.

**Please note:** This is my personal playground to test several JSF features and do some JEE testing with TomEE. It is a non-productive application and you have to change username and password in [shiro.ini](https://github.com/rollinhand/depot-ee/blob/master/src/main/webapp/WEB-INF/shiro.ini).

## Build information
This repository contains all to get a runnable application including server with one simple Maven command. Do a `clean package tomee:run` and TomEE will start and will be reachable at `http://localhost:8080`.

Or if you like it more the Docker way, just call `clean package docker:build`. You get a docker build file for further usage.
