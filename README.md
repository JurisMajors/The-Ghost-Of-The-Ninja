# DBL-IIS (Platformer)

Welcome to the project page, obviously this is work in progress.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

1. Java SE 1.8
2. IDE with Maven support (or Maven standalone)
3. A clone of this project

Note that this readme will focus on using IntelliJ IDEA as the IDE with Maven support.

### Installing

A step by step series of examples that tell you how to get a development env running

####Java 1.8 on Windows
```
Get and install the official latest Java SE 1.8 release from Oracle. (Sign in required)

or

Get and install version 8 of the OpenJDK project.
```

####IDE / Maven

```
When in doubt, IDEA it out. Seriously, the IDE is pretty nice and comes with Maven support.
```

#### Clone of the project

```
Simply use your favourite git tools to get a clone of this project on your local machine.
```

## IDEA Maven instructions
The steps are approximately as follows:
**Note that this may be partially incorrect / incomplete, please let me know or update**
```
1. Open IDEA and import the project
2. Indicate this is a Maven project
3. On the right hand side of IDEA there is a vertical Maven tab, click and open
4. Select the appropriate profile in the newly opened tab:
    4a. You choose lwjgl-natives-linux
    4b. Or you choose lwjgl-natives-windows
5. Click on the download arrow and choose sources (you could include documentation as well)
6. Go to "Edit Configurations" in IDEA
7. Click the "+" icon and add an "Application"
8. Select the "HelloWorld.java" as the Main class
9. Run and see a test window
```
If you see a window pop up with a red background, then your configuration has been successful! You can close the window by pressing ESCAPE. Which should trigger an example key callback to close the window.

![Hello World example window][logo]

[logo]: images/helloworld.png "Hello WOrld example window"

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* Others (?)
## Authors

* **Nico Klaassen** - *Some work* - [NicoKNL](https://github.com/NicoKNL)
* **Add yourself** - *What you did* - link to your git

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.
**!!!Url to be udated!!!**
## License

This project is licensed under the ... License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc

