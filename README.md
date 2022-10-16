# SE206 - 2022 - Beta & Final Releases

# Quick, Draw! 

## Attributions:

### Images:

All game icons were made by Freepik (https://www.freepik.com) from Flaticon (https://www.flaticon.com/), using the Flaticon License, which is a free for commercial use WITH ATTRIBUTION license.

Flaticon License:
Where you can use Flaticon's content*:
Website.
Software, applications, mobile, Multimedia.
Printed and digital media (magazines, newspapers, books, cards, labels, CD, television, video, e-mail).
Advertisement and promotional items.
Presentation of products and public events.

What you CAN DO:
You have the non-exclusive, non-transferable, non-sublicensable right to use the licensed material an unlimited number of times in any and all media for the commercial or personal purposes listed above.
You may alter and create derivative works.
You can use Flaticon's Contents during the rights period world widely.

What you CANNOT DO:
Sublicense, sell or rent any contents (or a modified version of them)
Distribute Flaticon Contents unless it has been expressly authorized by Flaticon
Offering Flaticon Contents designs (or modified Flaticon Contents versions) for download

* The complete content of licenses can be consulted in the Terms of Use, that will prevail over the content of this document.
www.flaticon.com/terms-of-use

### Music

All game music and sound effects were sourced from Mixkit (https://mixkit.co/).

Mixkit License:

Items under the Mixkit Sound Effects Free License can be used in your commercial and non-commercial projects for free.

You are licensed to use the Item to create an End Product that incorporates the Item as well as other things, so that it is larger in scope and different in nature than the Item. You’re permitted to download, copy, modify, distribute and publicly perform the Sound Effect Items on any web or social media platform, in podcasts and in video games, as well as in films and presentations distributed on CDs, DVDs, via TV or radio broadcast or internet based video on demand services.

You can’t redistribute the Item on its own, as stock, in a tool or template, or with source files. You’re also not allowed to claim them as your own or register them on any rights management service.

There are some important limits to these rights, described in our User Terms.

## For Developer Use:

**Requirements**

- Java JDK 17.0.2 (download
  it [https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) )
  and JAVA_HOME env variable properly set
- Scene Builder (download it
  here [https://gluonhq.com/products/scene-builder/#download](https://gluonhq.com/products/scene-builder/#download))


**What to do first?**

Make sure that the provided tests pass.

Unix/MacOsX:  
`./mvnw clean test`

Windows:  
`.\mvnw.cmd clean test`

This will also install the GIT pre-commit hooks to activate the auto-formatting at every GIT commit.

**How to run the game?**

Unix/MacOsX:  
`./mvnw clean javafx:run`

Windows:  
`.\mvnw.cmd clean javafx:run`

**How to format the Java code?**

You can format the code at any time by running the command:

Unix/MacOsX:  
`./mvnw git-code-format:format-code `

Windows:  
`.\mvnw.cmd git-code-format:format-code `
