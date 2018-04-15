README
=======



## PRODUCT DESCRIPTION

This project is being developed through the subject TDT4140 Software Development at NTNU.
The goal of this project is to learn how to use different coding-techniques, agile-development 
as well as obtaining experience in different development utilities (Gitlab, eclipse, 
NTNU-database etc.).

The application provides an alert and tracking system for people with location 
difficulties facilitated elderly, demented, mentally ill and others. Further on,
the application has the ability to monitor the position of users and notify 
relatives, guardians or healthcare personnel if the person has walked outside 
of a custom zone. The application holds as well the ability of the caretakers 
to add one or more people, set the mode of the zones and draw custom maps.

A more detailed description in norwegian is available [here](https://gitlab.stud.iie.ntnu.no/tdt4140-2018/14/wikis/home).




## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install of software and how to install them.

- Eclipse (Oxygen)
- Jetty, follow this https://eclipse-jetty.github.io/installation.html
- EclEmma, this is only if you want to be able to display codecoverage in your environment, if you do, follow this link http://www.eclemma.org/installation.html

### Installing

A step by step guide on how to get the development environment running (this assumes you are using Eclipse).

- Right-click on the Project Explorer pane and choose import... -> Git -> Projects from Git -> Clone URI
- In Location URL type: https://gitlab.stud.iie.ntnu.no/tdt4140-2018/14.git and enter your credentials, and finish the import.
- When the project has been imported, right-click on the parent file and choose maven -> update project and select all folders and press OK.
- It is recommended that you build the project the first time. Right-click on the parent folder once more and choose run as -> maven build
- If you are asked to enter a goal when building the project, type: clean verify


## How to run

1.   Right-click on the tdt4140-gr1814/webserver-folder and choose Run as -> Run with Jetty
2.   Go to tdt4140-gr1814/app.ui/src/main/java/tdt4140/gr1814/app/ui/
3.   Run ApplicationDemo.java



## Built With
* [Maven](https://maven.apache.org/) - Dependency Management


## Tools

- Eclipse (Oxygen)
- Scenebuilder
- Gitlab
- GMapsFx, imported module used in application (See manifest in module for Author/Owner).
- Jetty (Webserver)
- NTNU Database


## Authors

* **Marius Engen** - *Scrum master*
* **Svein Ole M. Sevle** - *Test responsible*
* **Sigve Svenkerud**
* **Oskar C. Vik**
* **Trym Erlend Eriksen Høgelid**
* **Vemund Fredriksen**
* **Harald Aarskog**

## Acknowledgments

* Google
* @Hal