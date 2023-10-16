```json
{
  "title": "Page Types",
  "icon": "arcane_repository:indexing_type_page",
  "category": "arcane_repository:1_general"
}
```

Pages in repository books can have many differing and complex abilities, 
so they are categorized into three main types based on functionality:


- Type pages
- Attribute pages
- Action pages

;;;;;

<|item-tag@arcane_repository:templates|tag-id=arcane_repository:type_pages|>

---

A single type page is required for any book to function, 
as these broadly define the general behaviour of the book, 
and usually dictate what other pages you can use in the first place.


E.g. an [Item Storage Page](^arcane_repository:item_storage/type_item_storage) will create a book that holds items, 
while an [Indexing Page](^arcane_repository:indexing/type_indexing) will create a book 
to [index your library](^arcane_repository:indexing).

;;;;;

Some type pages also have variant types, which may be required as a base for action or attribute pages. 


For example, the Item Storage Type Page has a variant called 
[Block Storage](^arcane_repository:item_storage/type_block_storage), 
which only stores items that can be placed as blocks. 
This restriction however means it can be used in conjunction with a 
[Building Action Page](^arcane_repository:item_storage/action_building) to place blocks directly from a book.

;;;;;

<|item-tag@arcane_repository:templates|tag-id=arcane_repository:attribute_pages|>

---

Attribute pages can be used to add to or improve functionality of a type page, 
increasing capacity, range, etc. or granting additional passive effects.


You can add as many attribute pages as your book can fit,
so long as they are compatible with the type page.

;;;;;

<|item-tag@arcane_repository:templates|tag-id=arcane_repository:action_pages|>

---

Action pages are similar to attribute pages, in that they grant additional functionality,
but there are some key distinctions. Firstly, you can only have one action page per book,
and secondly, they are not passive, but rather bound to a click action to be activated by the player.

;;;;;

An example of an action page is the [Feeding Action Page](^arcane_repository:item_storage/action_feeding),
which allows you to directly consume food items from your book.


It should be noted that action pages are almost always exclusively compatible with a single kind of type page.
