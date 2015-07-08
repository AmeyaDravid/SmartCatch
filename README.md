# SmartCatch
step 1:
take the files database1,database2 and create databases SmartCatch and smartcatch respectively for them in mysql.
following are the steps:
>mysql -u <username> -p 
>Smartcatch < database1.sql

step 2:
installation of ANT :
make sure that you have this installed because without this,we cannot generate opencv.jar file.
following are instructions for installation of ANT in linux:
	type the following command in command line:
	>sudo apt-get install ant
		
following are instructions for installation of ANT in windows:
	http://www.mkyong.com/ant/how-to-install-apache-ant-on-windows/

check the installation by typing command in terminal
>ant -version

step 3:
installation of opencv and setup in IDE(I have used eclipse,but I am attaching for Netbeans too,although they are basically the same)
for installation of opencv , i would suggest you to go through the following documentation:
http://docs.opencv.org/doc/tutorials/introduction/desktop_java/java_dev_intro.html

for configuring it with Eclipse IDE:
http://docs.opencv.org/doc/tutorials/introduction/java_eclipse/java_eclipse.html#java-eclipse

for configuring NetBeans IDE:
http://www.codeproject.com/Tips/717283/How-to-Use-OpenCV-with-Java-under-NetBeans-IDE

step 4:
following are a few minor changes you should make in the code to run it in your desktop:

->class Recorder.java :line 162,line 178:change the name from "/home/darksnake/Desktop/camera" to your prefered location.this is where we store images and are later deleted.

->class VideoCap.java :line 61:change the string from "/home/darksnake/Desktop/camera" to where you want to store the images (temporarily).these images will be deleted later to save on the disk space.

->class RightPanel.java:line 181:change the string from "/home/darksnake/Desktop/Videos" to another directory where you want to store your videos:these videos will be displayed later in the "playback" tab,so be sure to move any other videos present.

->class convertImageVideo:line 125:change the "~/Videos" directory to where you have store the videos in the class RightPanel.java
class convertImageVideo:line 113,line 120,line 133: change the "~/Desktop/camera" directory to where you have store the images

->class mySmartTable.java:line 30,line 35:change directory to preferred video directory

also change the password for database connection in class SmartCatch:line 311


step 5:
run class SmartCatch.java
maximise the window:this seems to be one of the issues with the program-it needs a kind of "refreshment"
the temporary user ID and password are 'ameya.dravid@gmail.com' and 'helloworld' respectively 

