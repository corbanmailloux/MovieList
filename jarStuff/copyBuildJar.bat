del .\*.jar
del .\*.java
del .\*.class
del .\*.mf
copy ..\*.java .\
copy .\config\*.* .\
copy ..\*.properties .\
javac *.java
jar cvfm MovieList.jar manifest.mf *.class
del .\*.java
del .\*.class
del .\*.mf
pause
