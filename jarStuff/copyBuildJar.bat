del .\*.jar
del .\*.java
del .\*.class
del .\*.mf
copy ..\*.java .\
copy .\config\*.* .\
javac *.java
jar cvfm MovieList.jar manifest.mf *.class
pause
