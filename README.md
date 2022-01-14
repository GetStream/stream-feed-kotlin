# stream-feed-kotlin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.getstream/stream-feed-client/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.getstream/stream-feed-client) [![build](https://github.com/GetStream/stream-feed-kotlin/workflows/Build%20and%20test/badge.svg)](https://github.com/GetStream/stream-feed-kotlin/actions)

[stream-feed-client](https://github.com/GetStream/stream-feed-kotlin) is the official Kotlin Feed Client for [Stream](https://getstream.io/). It doesn't depend on any Android dependency, so it can be used on any JVM project.

You can sign up for a Stream account at https://getstream.io/get_started.

## 🛠 Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.getstream</groupId>
    <artifactId>stream-feed-client</artifactId>
    <version>$stream_version</version>
</dependency>
```

or in your `build.gradle`:

```gradle
implementation 'io.getstream:stream-feed-client:$stream_version'
```
##  🔌 Usage

### API client setup

If you want to use the API client directly on your app you need to generate a user token and pass it.
As you must use your `API_SECRET` to create this token, it is unsafe to generate this token outside of a secure server.

#### Client API init

```kotlin
// Instantiate new client with a user token
val API_KEY = "my-API-Key"
val USER_TOKEN = "my-API-Key"
val client: StreamClient = StreamClient.connect(API_KEY, USER_TOKEN)
```

### 🔮 Examples

```kotlin
// Instantiate a feed object
val user1 = client.flatFeed(FeedID("user", "1"))

// Get activities from 5 to 10 (slow pagination)
val activities = user1.getActivities { 
    limit = 5
    offset = 5
}
            
// Filter on an id before than a given UUID
val filteredActivities = user1.getActivities{
    limit = 5
    idSmallerThan = "e561de8f-00f1-11e4-b400-0cc47a024be0"
}

// All API calls return an Either with the result or an error.
user1.getActivities {
    limit = 5
    offset = 5
    idSmallerThan = "e561de8f-00f1-11e4-b400-0cc47a024be0"
}.fold(
    { println("There was an error: $it") },
    { println("It is the list of activities: $it") },
)

// Create a new activity
val activity = Activity(actor = "1", verb = "tweet", `object` = "1", foreignId = "tweet:1")
val addedActivity =  user1.addActivity(activity)

// Create a bit more complex activity
val complexActivity = Activity(
    actor = "1", 
    verb = "run", 
    `object` = "1", 
    foreignId = "run:1",
    extraData = mapOf(
        "course" to mapOf(
            "name" to "Golden Gate park",
            "distance" to 10.00,
        ),
        "participants" to listOf("Jc"),
        "startedAt" to LocalDate.now(),
    )
)
val addedComplexActivity = user1.addActivity(complexActivity);

// Remove an activity by its id
user1.removeActivity(RemoveActivityById("e561de8f-00f1-11e4-b400-0cc47a024be0"))

// or remove by the foreign id
user1.removeActivity(RemoveActivityByForeignId("tweet:1"))

// Follow another feed
user1.follow { targetFeedID = FeedID("flat", "42") }

// Stop following another feed
user1.unfollow { targetFeedID = FeedID("flat", "42") }

// Stop following another feed while keeping previously published activities from that feed
user1.unfollow { 
    targetFeedID = FeedID("flat", "42")
    keepHistory = true
}

// Follow another feed without copying the history
user1.follow {
    targetFeedID = FeedID("flat", "42")
    activityCopyLimit = 0
}

// List followers, following
user1.followers { 
    limit = 10
    offset = 10
}
user1.followed { 
    limit = 10
    offset = 10
}

// adding multiple activities
val activities = listOf(
    Activity(actor = "1", verb = "tweet", `object` = "1"),
    Activity(actor = "2", verb = "tweet", `object` = "3"),
)
user1.addActivities(activities)

// specifying additional feeds to push the activity to using the `to` param especially useful for notification style feeds
val activityTo = Activity(
    to = listOf(FeedID("user", "2"), FeedID("user", "3")),
    actor = "1",
    verb = "tweet",
    `object` = "1",
    foreignId = "tweet:1",
)
user1.addActivity(activityTo);

// Updating parts of an activity
val newData = mapOf(
    "prodcut.price" to 19.99,
    "shares" to mapOf(
        facebook to "...",
        twitter to "..."
    )
)
val removeData = listOf("dailyLikes", "popularity")

// ...by ID
client.activityClient.partialUpdateActivityById { 
    activityId = "54a60c1e-4ee3-494b-a1e3-50c06acb5ed4"
    set = newData
    unset = removeData
}

// ...or by combination of foreign ID and time
client.activityClient.partialUpdateActivityByForeignId {
    time = "${Date().time}"
    foreignId = "product:123"
    set = newData
    unset = removeData
}
```


## 👩‍💻 Free for Makers 👨‍💻

Stream is free for most side and hobby projects. To qualify, your project/company needs to have < 5 team members and < $10k in monthly revenue.
For complete pricing details, visit our [Chat Pricing Page](https://getstream.io/chat/pricing/).

## 💼 We are hiring!

We've recently closed a [\$38 million Series B funding round](https://techcrunch.com/2021/03/04/stream-raises-38m-as-its-chat-and-activity-feed-apis-power-communications-for-1b-users/) and we keep actively growing.
Our APIs are used by more than a billion end-users, and you'll have a chance to make a huge impact on the product within a team of the strongest engineers all over the world.
Check out our current openings and apply via [Stream's website](https://getstream.io/team/#jobs).