# AEM Demo
Demonstration Project

## Environment Details:
- Apache Maven v3.8.1
- Java version: 11.0.19
- AEM v6.5.18
- Sling Dynamic Include v3.3.0

## Prerequisite:
Install SDI 3.3.0 bundle on the Publish environment.

## Installation Package:
`mvn clean install -Padobe-public -PautoInstallPackage -PautoInstallPackagePublish`

## Modules

The main parts of the template are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, and templates
* ui.content: contains sample content using the components from the ui.apps
* ui.config: contains runmode specific OSGi configs for the project
* ui.frontend: an optional dedicated front-end build mechanism (Angular, React or general Webpack project)
* all: a single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies
