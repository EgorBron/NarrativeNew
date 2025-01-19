# Narrative format compared to Twine

("Twine" in this doc means a generic look to popular Twine formats)


## Similarities

* both formats use extensible markup of the text
* both can have lots of format variants: there's only reference one and many based on it

## Differences

* Twine uses plain text parsed by a story format
  * While the Narrative uses JSON with strict schema, even for text formatting
* Twine uses *passages* wired with hyperlinks to define story flow, and each passage can be configured only wia markup
  * Narrative, in contrast, uses independent *labels* to define story flow, each formed by a list of elements, which maintain the logical structure of the story
* Twine doesn't require a "speaker" to display text — all stories can be told only by the narrator
  * In the Narrative, we have the concept of *actors* — each phrase is attached to an actor (but you still can pass `null` reference to each phrase to display it by the narrator)
* Twine uses *macros* integrated to the markup to perform actions
  * But the Narrative introduces *signals* and *jumps* to do so, and also supports **Flowy Staje** tasks to support custom macros (with extension)
* Twine is **strictly wired to the web** — while it opens all power of the web environment, it is not possible to use it in the console or on the desktop unless you rewrite the whole Twine (or use Node.js? why?)
  * And as the Narrative is not tied to any environment, you can use it in any place you want, just implement the environment which is easy