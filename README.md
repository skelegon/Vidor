<h1>Video organiser Vidor</h1>
<hr />
<h2>Overview</h2>
<p align="justify">
The purpose of this project is mainly to learn different java and database features by creating a useful program. Video organiser is an ideal goal as it requires both the basic java capabilities as well as integration with external applications, media platforms and a database. This program will be useful for people working with video editing, or anyone with a large video collection, looking for a quick way to browse and find the correct video.
</p>

<h2>Features</h2>
<hr />
<ol>
<li>Interface to change application propertis and video folder locations.</li>
<li>Automatic generation of video thumbnails.</li>
<li>Interface to browse videos.</li>
<li>Interface for tag management</li>
<li>Tagging videos for tag based search.</li>
<li>Database integrity check to keep files on disk and in database the same.</li>
</ol>

<h2>Dependencies</h2>
<hr />
<ol>
<li><b>SQLite</b>
        <ul>
            <li>Lightweight Object Relational Mapping (ORM) Java Package that provides some simple, lightweight functionality for persisting Java objects to SQL databases while avoiding the complexity and overhead of more standard ORM packages.</li>
			<li>Use in application: Storage for file related information. (Including image files for thumbnails)</li>
            <li>Website: https://www.sqlite.org/</li>
        </ul>
</li><br />
<li><b>SQLite JDBC Driver</b>
        <ul>
            <li>SQLite JDBC, developed by Taro L. Saito, is a library for accessing and creating SQLite database files in Java.</li>
            <li>Website: https://bitbucket.org/xerial/sqlite-jdbc</li>
        </ul>
</li><br />
<li><b>ffmpeg</b>
	<ul>
		<li>A complete, cross-platform solution to record, convert and stream audio and video.</li>
		<li>Use in application: For generating video thumbnails and timestamps.</li>
		<li>Website: https://www.ffmpeg.org/</li>
	</ul>
</li><br />
<li><b>VLC Media player</b>
	<ul>
		<li>VLC is a free and open source cross-platform multimedia player and framework that plays most multimedia files as well as DVDs, Audio CDs, VCDs, and various streaming protocols.</li>
		<li>Use in application: provides command-line interface to open video files from certain timestamp</li>
		<li>Website: http://www.videolan.org/vlc/index.html</li>
	</ul>
</li><br />
<li><b>OrmLite</b>
	<ul>
		<li>Lightweight Object Relational Mapping (ORM) Java Package that provides some simple, lightweight functionality for persisting Java objects to SQL databases while avoiding the complexity and overhead of more standard ORM packages.</li>
		<li>Website: http://ormlite.com/</li>
	</ul>
</li><br />
<li><b>Apache Commons Configuration</b>
	<ul>
		<li>The Commons Configuration software library provides a generic configuration interface which enables a Java application to read configuration data from a variety of sources.</li>
		<li>Website: https://commons.apache.org/proper/commons-configuration/</li>
	</ul>
</li><br />
<li><b>Apache Commons Lang</b>
	<ul>
		<li>Lang provides a host of helper utilities for the java.lang API, notably String manipulation methods, basic numerical methods, object reflection, concurrency, creation and serialization and System properties. Additionally it contains basic enhancements to java.util.Date and a series of utilities dedicated to help with building methods, such as hashCode, toString and equals.</li>
		<li>Website: https://commons.apache.org/proper/commons-lang/</li>
	</ul>
</li><br />
<li><b>Apache Commons Logging</b>
	<ul>
		<li>The Logging package is an ultra-thin bridge between different logging implementations.</li>
		<li>Website: https://commons.apache.org/proper/commons-logging/</li>
	</ul>
</li>
<li><b>Maven</b>
	<ul>
		<li>Apache Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information. </li>
		<li>Use in application: dependency management and build automation.</li>
		<li>Website: https://maven.apache.org/</li>
	</ul>
</li>
</ol>

<h2>User manual</h2>
<hr />
<p align = "justify">
On the first run, the program must first be configured. <br /> <br />
</p>
<ol>
<li><b>Setting up the program</b><br />
For this, open the "Settings" option under the "File" menu on the top left of the screen (See image 1)<br />
<img src="http://hex.ee/s/2015-12-31_17-38-48.png" /><br />
(Image 1: File menu)<br /><br />
The Settings Configuration window pops up. This window has tabs - "General" and "Video Locations". (See image 2)<br />
<img src="http://hex.ee/s/2015-12-31_17-42-59.png" /> <br />
(Image 2: General settings)<br /><br />
The General tab has the following options:<br />
<ol>
<li>VLC Media Player Location</li>
<li>Thumbnail Count - the quantity of thumbnails created for each video.</li>
<li>Thumbnail Max Width - The width of the created thumbnails in the main window.</li>
<li>Thumbnail Hightlight Color: The background color for when the mouse is hovered over the generated thumbnails.</li>
</ol>
The Video Locations tab allows adding and removing folders in which the program will look for videos:<br />
<img src="http://hex.ee/s/2015-12-31_17-45-49.png" /><br />
(Image 3: Video locations)<br /><br />
Currently the following file extensions are supported:
<ol>
<li>wmv</li>
<li>mp4</li>
<li>flv</li>
<li>mov</li>
<li>avi</li>
<li>mkv</li>
</ol>
Bottom left of the Settings Configuration window are the "Save" and "Close" (See image 4) <br />
<img src="http://hex.ee/s/2015-12-31_17-49-29.png" /><br />
(Image 4: Save and close settings)<br /><br /></li>
<li><b>Navigating in Vidor</b><br />
Adding folders to the Video Locations will force the program to find and list all video files with supported file extensions in these directories. Please note, that this will take some time as at the same time thumbnails are created for the found videos. The progress can be tracked by the loading bar: <br />
<img src="http://hex.ee/s/2015-12-31_17-52-29.png" /><br />
(Image 5: Process indication)<br /><br />
Once the files are processed, they will be listed on the left side of the main window with reference to the folder, file name and file size:<br />
<img src="http://hex.ee/s/adsf.png" /><br />
(Image 6: File tree)<br /><br />
Choosing a video file from the created list will display the thumbnails of the selected video on the right side of the window.<br />
Each thumbnail contains a timestamp, displaying the time from the beginning of the video: <br />
<img src="http://hex.ee/s/fdsghfghd.png" /><br />
(Image 7: Cool cat)<br /><br />
Clicking on a thumbnail will open the selected video at the displayed time using VLC media player, granted that the VLC media player executable is correctly defined in the Settings Configuration window.<br /><br /></li>
<li><b>Managing and using tags</b><br />
Bottom left of the main window there is the tag management system. <br />
<img src="http://hex.ee/s/zxcvzc.png" /><br />
(Image 8: Tag management 1)<br /><br />
Clicking the "Manage" button will display the Tag Manager tool, where users can add keywords or tags that can be used to describe the videos.<br />
To add a tag, type a word on the textfield left of the "Add" button and then press the "Add" button. This will add the word to the list of tags. <br />
<img src="http://hex.ee/s/765hgdf.png" /><br />
(Image 9: Tag management 2)<br /><br />
Remove button will delete the currently selected tag from the tags list.<br />
To assign a tag to a video, return to the main window, select a file from the file list, select a tag from the tag list from the drop down menu on bottom right of the main window, and press add.<br />
<img src="http://hex.ee/s/45ghfs.png" /><br />
(Image 10: Tag management 3)<br /><br />
Tags assigned to the selected video are displayed on the left of the tag management tools. Tags assigned to each video can be removed by pressing the cross mark on a specific tag.<br />
Writing tags on the textfield and pressing the “Search” button on top of the list of videos, will filter the list of videos to only show the videos that the specific tag is assigned to.<br />
<img src="http://hex.ee/s/asdfad57.png" /><br />
(Image 11: Search 1)<br /><br />
Search also works with multiple tags. By separating the tags with commas "," only videos with all the specified tags assigned are displayed.<br />
<img src="http://hex.ee/s/vbvxcbty.png" /><br />
(Image 12: Search 2)<br /><br />
</li>
</ul>
