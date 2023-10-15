```json
{
  "title": "Book Crafting",
  "icon": "minecraft:writable_book",
  "category": "arcane_repository:1_general",
  "associated_items": [
    "arcane_repository:repository_book"
  ]
}
```

Repository Books are the end result of combining all other components outlined in this book,
as they serve as both the storage medium for, and the means of interacting with your repository.


Before creating a Repository Book, it is important to consider what function it should serve, 
and use the appropriate [pages](^arcane_repository:general/page_types),
as these dictate the book's behaviour.

;;;;;

To create a Repository Book, you will need to combine the following items in a crafting table:

- 1 Leather
- A Catalyst (see the following page)
- 1 Type page
- 0-many Attribute pages
- 0-1 Action pages

;;;;;

The Catalyst is a gem that is used as a focus for the magic housed in the pages.
Depending on the type of gem used, the resulting book will have a different page limit:

- Amethyst: 2 pages
- Emerald: 4 pages
- Diamond: 6 pages

When the page limit is reached, no more pages of any kind can be added to the book.
It should be noted that the type page *does* count towards the page limit.

;;;;;

An example recipe would be as follows:

<recipe;arcane_repository:custom_book>

A book must contain at least one type page,
and optionally supports any number of attribute pages and a single action page.
*Pages cannot be added or removed later.*

