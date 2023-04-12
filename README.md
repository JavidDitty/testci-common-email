# Assignment 3: Testing Commons Email Using JUnit
## Introduction
Commons Email is an Apache Java API for sending emails.
EmailTest and EmailDummy are non-Apache, custom classes in the org.apache.commons.mail package of Commons Email.
These classes use JUnit to test the following methods in the Email class in the same package:
- addBcc(String... emails)
- addCc(String email)
- addHeader(String name, String value)
- addReplyTo(String email, String name)
- buildMimeMessage()
- getHostName()
- getMailSession()
- getSentDate()
- getSocketConnectionTimeout()
- setFrom(String email)<br>

Together, tests achieve over 70% coverage on all these methods.
Test environments are generated using setup and teardown methods.
Tests are conducted using JUnit assertions.
Test classes can be found in src/test/java in the project.

## Getting Started
### Requirements
- Java JDK 11.09 (https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)<br>
- Eclipse IDE 2020-09-R (https://www.eclipse.org/downloads/packages/release/2020-09/r/eclipse-ide-enterprise-java-developers)<br>

### Running the Application
1. Download the project (i.e. "commons-email").
2. Open Eclipse IDE 2020-09-R.
3. Import the project as a maven project.
4. Configure the project to use jdk-11.09 (i.e. JavaSE-11).
5. In the toolbar, select Run > Coverage As > JUnit Test.

## Authors
Javid Ditty