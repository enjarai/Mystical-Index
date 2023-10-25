```json
{
  "title": "Attribute: Magnetism",
  "icon": "arcane_repository:magnetism_attribute_page",
  "category": "arcane_repository:item_storage",
  "associated_items": [
    "arcane_repository:magnetism_attribute_page"
  ],
  "ordinal": 6
}
```

The magnetism attribute page can attract dropped items in the world to a storage book.
The storage book has to be explicitly filtered to an item type for it to be attracted.


This page also doesn't insert the items into the book, only bringing them close enough for the player to pick up.
<|item-spotlight@lavender:book_components|item=arcane_repository:magnetism_attribute_page|>

;;;;;

You'll usually want to use this page in conjunction with a
[pickup attribute page](^arcane_repository:item_storage/attribute_pickup) to also insert the 
attracted items into your book, though this is not required.

<recipe;arcane_repository:page/attribute/magnetism>
