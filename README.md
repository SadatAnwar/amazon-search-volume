## Solution 

In order to get an estimate on how frequent a search term is seen on Amazon by relying on the autocomplete api, 
the idea is to "start typing" and see when the autocomplete api suggests the intended word. 

If the intended keyword appears on the first keystroke, it's probably very popular. The more keystrokes needed to get to 
the keyword, the less frequent it is.


In order to map the frequency of occurrence to a the range `[0 -> 100]` I simply do 
`100 - (% of keyword used to search)` so, if its the first keystroke its `100-0` and if its the complete word 
its `100 - ((lengthOfKeyword-1)/lengthOfKeyword * 100)` which will be very close to zero, and if the keyword is never found
the score is `0`. 


----

### 1. What assumptions did you make?

The most important assumption is that the auto-complete API will ALWAYS return keywords that users are searching for,
for rare keywords, they will appear when we search for the whole string. And that also means that if the API does not suggest
a word, users are not searching for it. 

### 2. How does the algorithm work? 

This is explained in the [Solution](#solution) section.

Also not sure if it will be any faster but we could try a "fail fast" approach, i.e start with the whole keyword 
and start clipping chars from the end, this way, if a long word is searched, we will know if its ever found or not, and 
can say `score=0` faster. But sure, that too might have certain drawbacks. :shrug: 

### 3. Do you think the (*hint) that we gave you earlier is correct and if so ­ why?

Its not always correct. easier to explain with an example (currently working on the amazon.com website). 

* If you want to search for "game of thrones season 3" <- this is a rather unpopular search team at the moment

  * if you type in "game of thrones season" you will see "season 3" towards the end of the list and "season 7" at the top.
  Based on the assumption, these 2 would have similar traffic, but "game of thrones season 7" start showing up as soon
  as you have written "game", so its much more popular than season 3. So, its not ALWAYS true. All NEW suggestions that
  show up after a keypress might have similar traffic.
  
### 4. How precise do you think your outcome is and why?

This is hard to tell IMO, being a search engineer, and having developed a similar autocomplete feature, I would not be 
surprised if the sorting is based on some more complicated logic like CTR of the suggestion and also factor in the transactions 
that happen after the search.

 
### Implementation

This is a Spring boot application using only spring-web for the REST Endpoint. The project uses Maven for the build and 
dependency management.

(on project root)

Execute `$ mvn test` to run the unit tests

Execute `$ mvn spring-boot:run` to start the application
