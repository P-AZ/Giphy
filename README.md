# Giphy

My Giphy app includes:
* Pagination of gifs
* 3 screens - home,favorites (Fragments) and single Gif Dialog (navigating using nav graph + bottom nav bar + safeargs)
* Search option (both for home screen - pagination based using Flow and stateFlow, and for favorites screen - RoomDB using Flow)
* On the single Gif screen - click on a Gif to share it (using implicit Intent)
* Single Activity architecture, MVVM, DI (Dagger-Hilt), Nav Graph, Pagination, RoomDB (using SQL), VM, Repo, Livedata, Flow, stateFlow, Coroutines, ConstraintLayout
* Nice background music :)


The app contains best practices such as:
* Kotlin scope functions
* Kotlin extension functions
* Coroutines (includes lifecycleScope, viewModelScope, Dispatchers, cachedIn, flow)
* XML Localization (strings, colors, dimens)
* Lambda functions for adapters (instead of Interfaces)


* NOTE - There's a bug in GiphyAPI Pagination call, it returns each gif twice (I tried figuring it ouw using Postman but the same problem occoured).

Thank you for your time !
Paz.
