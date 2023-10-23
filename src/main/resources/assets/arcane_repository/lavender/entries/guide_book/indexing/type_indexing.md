```json
{
  "title": "Type: Indexing",
  "icon": "arcane_repository:indexing_type_page",
  "category": "arcane_repository:indexing",
  "associated_items": [
    "arcane_repository:indexing_type_page"
  ]
}
```

An indexing book forms the heart of any repository.
It is used to store and retrieve items from storage books in Chiseled Bookshelves.





<|item-spotlight@lavender:book_components|item=arcane_repository:indexing_type_page|>

;;;;;

They can connect to bookshelves in two different ways, [area indexing](^arcane_repository:indexing/mode_area)
and [linked indexing](^arcane_repository:indexing/mode_linked). 
These are described in more detail in their respective sections, but they share the same base concepts.


When either holding an indexing book in your hand, or standing near one on a lectern,
any chat messages you send will be intercepted and interpreted as requests for the book.

;;;;;

This method can be used to query the book for information about its contents,
or to request items from it using (mostly) natural language.


Some common examples are:
- diamond
- 5 diamonds
- 1 stack of diamonds
- diamond?
- ?

*Requests ending with question marks are used to count items matching the query.*

;;;;;

The book will respond to these requests with a message in chat,
and either drop the requested items onto the lectern, or into your inventory if you're holding it.


Items can also be inserted into the indexing book by right-clicking them on the lectern,
or on the book in your inventory. This will place the items in the repository,
prioritizing books that are filtered for them.

;;;;;

Before crafting an indexing book, consider increasing its range and/or link count by adding
[Range Pages](^arcane_repository:indexing/attribute_range) and/or [Link Pages](^arcane_repository:indexing/attribute_links),
as these values are quite low by default.


That being said, the recipe for an indexing page is as follows:

<recipe;arcane_repository:page/type/indexing>