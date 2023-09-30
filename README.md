# JSON-JavaParser-Wiki
Program Description
The program reads the user's search query from the console, and displays the search result on Wikipedia. After selecting the desired article, the program opens it in the browser. The program ensures operation even when entering incorrect information.

For the program to work , you need to add the appropriate ones .jar files as used libraries in the project.

The code is written in accordance with the following points:
1) Read the data entered by the user
2) Make a request to the server
3) Parse the response
4) Output the search result
5) Open the desired page in the browser

Generating a request
The API provides the ability to make search queries without authorization keys. In this way:
<<https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch ="LETI">>

You can open this link in the browser, and look at the result of the request.
However, in order for the request to be successful, you should remove invalid characters from the link, that is, make Percent-encoding, aka URL Encoding.
To do this, use the encode method in the URLEncoder class.

Preparing for response processing
The server returns data in JSON format.
Google's Gson library is used for parsing them. There are examples here:
https://github.com/google/gson

Opening an article in the browser
After receiving the search results from Wikipedia, we will know the pageid of each article. You can use it to get a link to the article as follows:
https://ru.wikipedia.org/w/index.php?curid=11099
The Java Desktop class is used to open the page in the browser.
