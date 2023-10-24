```json
{
  "title": "Attribute: Stacks",
  "icon": "arcane_repository:stacks_page",
  "category": "arcane_repository:item_storage",
  "associated_items": [
    "arcane_repository:stacks_page"
  ],
  "ordinal": 3
}
```

This page is used to increase the absolute maximum amount of items an 
[item storage](^arcane_repository:item_storage/type_item_storage) book can hold.


It *does not* affect the types limit of the book though, so you'll usually want to combine it with
[Types Pages](^arcane_repository:item_storage/attribute_types) to achieve a balanced result.

<|item-spotlight@lavender:book_components|item=arcane_repository:stacks_page|>

;;;;;

Every stacks page added to a book will *double* the amount of entries.


This effect will compound, so adding 2 stacks pages will quadruple the amount of entries,
adding 3 will octuple it, and so on.

<recipe;arcane_repository:page/attribute/stacks>