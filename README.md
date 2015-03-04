# Google Shopping alerts for Android

An Android app that sends you push notifications when the product you want hits the right price!


- User can add to a list of Google Shopping searches by entering keywords and applying filters which populate dynamically from the Google Shopping API
 - Filters can include category, condition, price, color, brand, etc.
- User can see a list of products that have matched the search criteria by tapping on an individual search item from the "home" list.
- The gist of this app happens via push notifications. The app runs a background service which searches Google Shopping periodically, and fetches product matches to the user via push notifs. Push notifications should ideally include a photo of the product and a clearly visible price
- From the push notification, the user should be able to tap to open the app, and to shop for the item on the website where Google Shopping links to for that item
- User should be able to manage alerts, archive, and delete searches, as well as edit the filters of existing searches
