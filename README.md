# Ripoti App

This is an android app to report emergencies, accidents or any other thing that one feels like the public should know.

> **Ripoti** is a swahili name meaning; _to report_.

> The app is continuously receiving new features and UI changes. The is the first version of the app.

## Demo/screenshots

<p align="center">
<img src="https://user-images.githubusercontent.com/87479198/210562967-4944f653-4b0b-460e-8ac4-80c68930c91e.jpeg" width=30% height=30% >
<img src="https://user-images.githubusercontent.com/87479198/210563008-bd338201-5ac0-404e-8e56-655f3c14779b.jpeg" width=30% height=30% >
<img src="https://user-images.githubusercontent.com/87479198/210563055-cfffb31b-0189-45ae-9ea7-4c17c4d9519b.jpeg" width=30% height=30% >
<img src="https://user-images.githubusercontent.com/87479198/210563099-eb537836-884b-409a-af27-e31606e5d82b.jpeg" width=30% height=30% >
<img src="https://user-images.githubusercontent.com/87479198/210563122-830aeeeb-0454-47e6-8031-f462ccee78b0.jpeg" width=30% height=30% >
<img src="https://user-images.githubusercontent.com/87479198/210563154-14560bc6-2ee0-4956-9605-b0e012d4b524.jpeg" width=30% height=30% >
</p>


## App Architecture

The app uses mvvm clean architecture. This includes;

- repositories
- use cases
- views
- viewModels

The app has two APIs. One for the app (ripoti API) and the other is a news API.

> The ripoti API is built with **Nodejs**. For authentication, it uses _JWT_ and _Postgresql_ as its database.

> The News API is also built with **NodeJs**. The news data is scraped from a Kenyan media station (nation.africa).

## Technologies

The app uses these technologies;

- Kotlin - App is built with the language.
- XML - UI uses xml and fragments.
- Retrofit- For networking.
- Hilt - For dependency injection.
- Jetpack components;
  - navigation component - Navigating through different screens in the app.
  - Room - For local storage of the app.
- Glide - To display images from the api's used.
- Coroutines - Used to make asynchronous calls.
- Flow - Used to get data from network requests asynchronously

## Known Bugs

If the app has any bug. Please make contact below.

## Contacts

Have anything to contribute to the app, send an email to:

> lsheldon645@gmail.com

## Licence

Copyright 2023 Sheldon Okware

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
