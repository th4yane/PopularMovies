# PopularMovies
Android application which shows information about movies. It uses the api provided by the [themoviedb](https://api.themoviedb.org/). Movies can be seen by **Most Popular**, **Top Rated** and **Favorites**.

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

##Libraries Used

- [Picasso](http://square.github.io/picasso/)
- [Butter Knife](http://jakewharton.github.io/butterknife/)

###Dependencies Used

```
compile 'com.android.support:appcompat-v7:25.1.1'
compile 'com.squareup.picasso:picasso:2.5.2'
compile 'com.android.support:recyclerview-v7:25.0.1'
compile 'com.jakewharton:butterknife:8.5.1'
annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'

```
##ScreenShots

<img src="https://user-images.githubusercontent.com/14139700/30244152-bb4b314c-958e-11e7-9e22-0e50ed358504.jpg" width="500">

<img src="https://user-images.githubusercontent.com/14139700/30244153-bb500f50-958e-11e7-8e6c-22926d339e46.jpg" width="500">

Tablet's layout:
<img src="https://user-images.githubusercontent.com/14139700/30244161-f4940582-958e-11e7-9215-f4e75ef16c0f.png" width="500">
