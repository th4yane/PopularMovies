# Popular_Movies
Android application which shows information about movies. It uses the api provided by the [themoviedb](https://api.themoviedb.org/). Movies can be seen by **Most Popular** and **Top Rated**.

## How to use this application?
For running this application you have to keep your api key provided by the themoviedb in a XML file.
Create and put xml file "api_keys.xml" in the directory "res/values/".

"api_keys.xml" must be like:
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="MOVIEDB_API_KEY">YOUR API KEY HERE</string>
</resources>
```

##Library Used

- [Picasso](http://square.github.io/picasso/)

###Dependencies Used

```
compile 'com.android.support:appcompat-v7:25.1.1'
compile 'com.squareup.picasso:picasso:2.5.2'
compile 'com.android.support:recyclerview-v7:25.0.1'
```

##ScreenShots
