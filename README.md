# jFoenixBugSSCCE

Bug SSCCE for the issue #964 on the JFoenix library

## How to launch the project :

From a command line interpreter in the project's root directory

``` Batchfile
mvn clean package

java --module-path "%PATH_TO_YOUR_JAVAFX_SDK%\lib" --add-modules=javafx.controls,javafx.fxml -cp target/jfoenixbugsscce-0.0.1-SNAPSHOT-jar-with-dependencies.jar org.test.jfoenixtest.MainDemo
```
